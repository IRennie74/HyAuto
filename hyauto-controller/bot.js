require('dotenv').config(); // Load .env file

const { Client, GatewayIntentBits } = require('discord.js');
const client = new Client({ intents: [GatewayIntentBits.Guilds] });

client.on('ready', () => {
  console.log(`🤖 Logged in as ${client.user.tag}`);
});

client.on('messageCreate', (message) => {
  if (message.content === '!status') {
    message.channel.send('All accounts reporting normally!');
  }
});

client.login(process.env.DISCORD_TOKEN);
