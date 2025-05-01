require('dotenv').config();
const { Client, GatewayIntentBits, Events, EmbedBuilder, Collection } = require('discord.js');
const { REST, Routes } = require('discord.js');
const express = require('express');
const cors = require('cors');
const { exec } = require('child_process');

const app = express();
const PORT = 3000;

const STUCK_TIMEOUT_MS = 20_000;

// UUID to script path mapping
const restartScripts = {
  "e80c4ae4670b43da8242d2546a60faba": "scripts/restart-Worker1.bat", // wai1
  "c1a9b894d67f4d3eb86ee951c993cd56": "scripts/restart-Worker2.bat", // Lkws
};

const clientStatus = {};

const client = new Client({
  intents: [GatewayIntentBits.Guilds, GatewayIntentBits.GuildMessages, GatewayIntentBits.MessageContent]
});

// Register slash commands
const commands = [
  {
    name: 'status',
    description: 'Get the status of all HyAuto clients'
  },
  {
    name: 'restart',
    description: 'Restart a client instance by UUID',
    options: [
      {
        name: 'uuid',
        type: 3, // STRING
        description: 'UUID of the bot to restart',
        required: true
      }
    ]
  },
  {
    name: 'commands',
    description: 'List all available bot commands'
  }
];

const rest = new REST({ version: '10' }).setToken(process.env.DISCORD_TOKEN);
rest.put(Routes.applicationCommands(process.env.CLIENT_ID), { body: commands })
  .then(() => console.log('✅ Slash commands registered'))
  .catch(console.error);

app.use(cors());
app.use(express.json());

// POST endpoint for Minecraft clients
app.post('/api/update', (req, res) => {
  const { uuid, username, status, tps, ram, macro } = req.body;

  if (!uuid || !username) return res.status(400).send("Missing uuid or username");

  clientStatus[uuid] = {
    username,
    status,
    tps,
    ram,
    macro,
    lastUpdated: Date.now(),
    stuck: false
  };

  res.sendStatus(200);
});

app.get('/api/status', (req, res) => {
  res.json(clientStatus);
});

// Bot startup
client.once(Events.ClientReady, () => {
  console.log(`🤖 Bot ready as ${client.user.tag}`);
  app.listen(PORT, () => console.log(`🌐 Web server running at http://localhost:${PORT}`));

  // Stuck bot check
  setInterval(() => {
    const now = Date.now();
    for (const [uuid, data] of Object.entries(clientStatus)) {
      const inactive = now - data.lastUpdated > STUCK_TIMEOUT_MS;
      if (inactive && !data.stuck) {
        data.stuck = true;
        alertStuck(uuid, data);
      } else if (!inactive && data.stuck) {
        data.stuck = false;
      }
    }
  }, 10_000);
});

// Handle slash commands
client.on(Events.InteractionCreate, async interaction => {
  if (!interaction.isChatInputCommand()) return;

  const { commandName } = interaction;

  if (commandName === 'status') {
    const embed = new EmbedBuilder()
      .setTitle("📊 HyAuto Client Status")
      .setColor(0x3498db)
      .setTimestamp();

    for (const [uuid, data] of Object.entries(clientStatus)) {
      const color = data.stuck
        ? 0xFF0000
        : data.status === "Running"
          ? 0x00FF00
          : 0xFFFF00;

      embed.addFields({
        name: `${data.username} (${uuid})`,
        value: `**Status:** ${data.status}${data.stuck ? " ❌" : ""}\n**Macro:** ${data.macro}\n**TPS:** ${data.tps}\n**RAM:** ${Math.round(data.ram / 1024 / 1024)} MB`,
        inline: false
      }).setColor(color);
    }

    await interaction.reply({ embeds: [embed] });
  }

  if (commandName === 'restart') {
    const uuid = interaction.options.getString('uuid');
    const success = restartBot(uuid);

    await interaction.reply(success
      ? `♻️ Restarting \`${uuid}\`...`
      : `❌ Could not restart \`${uuid}\`. No script found.`);
  }

  if (commandName === 'commands') {
    const embed = new EmbedBuilder()
      .setTitle('📘 HyAutoBot Slash Commands')
      .setColor(0x00BFFF)
      .setDescription('Here are the slash commands you can use:')
      .addFields(
        { name: '/status', value: 'Show all client statuses' },
        { name: '/restart <uuid>', value: 'Restart a bot instance by UUID' },
        { name: '/commands', value: 'Display this help message' }
      )
      .setTimestamp();

    await interaction.reply({ embeds: [embed] });
  }
});

// Alert when stuck
function alertStuck(uuid, data) {
  const embed = new EmbedBuilder()
    .setTitle("🔴 Bot Stuck Detected")
    .setColor(0xFF0000)
    .addFields(
      { name: "UUID", value: uuid, inline: true },
      { name: "Username", value: data.username, inline: true },
      { name: "Last Status", value: data.status, inline: true },
      { name: "Macro", value: data.macro, inline: true }
    )
    .setTimestamp();

  const channelId = process.env.DISCORD_CHANNEL_ID;
  client.channels.fetch(channelId).then(channel => {
    if (channel && channel.isTextBased()) {
      channel.send({ embeds: [embed] });
    }
  });

  restartBot(uuid);
}

// Restart function
function restartBot(uuid) {
  const script = restartScripts[uuid];
  if (!script) {
    console.log(`[Restart] No script found for ${uuid}`);
    return false;
  }

  exec(`"${script}"`, (error, stdout, stderr) => {
    if (error) {
      console.error(`[Restart] Error restarting ${uuid}:`, error.message);
    } else {
      console.log(`[Restart] ${uuid} restarted.`);
    }
  });

  return true;
}

client.login(process.env.DISCORD_TOKEN);
