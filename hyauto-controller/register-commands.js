require('dotenv').config(); // Loads your .env file

const { REST, Routes, SlashCommandBuilder } = require('discord.js');

const commands = [
  new SlashCommandBuilder().setName('status').setDescription('Shows all bot statuses'),
  new SlashCommandBuilder()
    .setName('restart')
    .setDescription('Restart a bot instance')
    .addStringOption(option =>
      option.setName('uuid')
        .setDescription('UUID of the bot to restart')
        .setRequired(true)
    ),
  new SlashCommandBuilder().setName('commands').setDescription('List all available commands')
].map(cmd => cmd.toJSON());

// ✅ Debug line to confirm Client ID is loading
console.log("Registering with CLIENT_ID:", process.env.CLIENT_ID);

const rest = new REST({ version: '10' }).setToken(process.env.DISCORD_TOKEN);

(async () => {
  try {
    console.log('Registering slash commands...');
    await rest.put(
      Routes.applicationCommands(process.env.CLIENT_ID),
      { body: commands }
    );
    console.log('✅ Slash commands registered!');
  } catch (err) {
    console.error(err);
  }
})();
