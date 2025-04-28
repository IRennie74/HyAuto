package studio.dreamys.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigManager {

    public static Configuration config;

    // Example settings
    public static boolean safetyEnabled;
    public static int macroDelayMs;
    public static String defaultMacro;

    public static void preInit(File configDir) {
        File configFile = new File(configDir, "hyauto.cfg");

        config = new Configuration(configFile);

        try {
            config.load();

            // Load settings
            safetyEnabled = config.getBoolean("safetyEnabled", "general", true, "Pause macros if safety triggers.");
            macroDelayMs = config.getInt("macroDelayMs", "general", 100, 10, 10000, "Delay (ms) between macro actions.");
            defaultMacro = config.getString("defaultMacro", "general", "TransitionMacro", "The macro to start first.");
        } catch (Exception e) {
            System.out.println("[HyAuto] Error loading config: " + e.getMessage());
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void save() {
        if (config != null && config.hasChanged()) {
            config.save();
        }
    }
}
