// Load environment variables from .env
require('dotenv').config();

// 🧠 Discord Bot Setup
const { Client, GatewayIntentBits, Events } = require('discord.js');
const client = new Client({
  intents: [GatewayIntentBits.Guilds, GatewayIntentBits.GuildMessages, GatewayIntentBits.MessageContent]
});

// 📡 Express + Web Server Setup
const express = require('express');
const cors = require('cors');
const app = express();
const PORT = 3000;

// In-memory data for client status updates
const clientStatus = {};

// Middleware
app.use(cors());
app.use(express.json());

// Endpoint: POST /api/update (from Minecraft clients)
app.post('/api/update', (req, res) => {
  const { uuid, status, tps, ram, macro } = req.body;

  if (!uuid) {
    return res.status(400).send('Missing UUID');
  }

  clientStatus[uuid] = {
    status,
    tps,
    ram,
    macro,
    lastUpdated: new Date()
  };

  // Send to Discord
  const channelId = process.env.DISCORD_CHANNEL_ID;
  if (channelId) {
    const message = `🟢 **[${uuid}]**
- **Status:** ${status}
- **Macro:** ${macro}
- **TPS:** ${tps}
- **RAM:** ${Math.round(ram / 1024 / 1024)} MB`;

    client.channels.fetch(channelId).then(channel => {
      if (channel && channel.isTextBased()) {
        channel.send(message);
      }
    }).catch(console.error);
  }

  res.sendStatus(200);
});

// Optional GET endpoint for checking current status
app.get('/api/status', (req, res) => {
  res.json(clientStatus);
});

// 🎮 Discord Bot Events
client.once(Events.ClientReady, () => {
  console.log(`🤖 Bot ready as ${client.user.tag}`);
});

// Respond to "!status" in chat
client.on(Events.MessageCreate, (message) => {
  if (message.content === '!status') {
    const response = Object.entries(clientStatus).map(([uuid, info]) => {
      return `**${uuid}**: ${info.status} | ${info.macro} | TPS: ${info.tps} | RAM: ${Math.round(info.ram / 1024 / 1024)} MB`;
    }).join('\n');

    message.channel.send(response || "No accounts are reporting yet.");
  }
});

// Start everything
client.login(process.env.DISCORD_TOKEN).then(() => {
  app.listen(PORT, () => {
    console.log(`🌐 Web server running at http://localhost:${PORT}`);
  });
});
