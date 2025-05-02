package studio.dreamys;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import studio.dreamys.config.ConfigManager;
import studio.dreamys.event.ConnectionHandler;
import studio.dreamys.event.TickHandler;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = HyAutoMod.MODID, name = HyAutoMod.NAME, version = HyAutoMod.VERSION)
public class HyAutoMod {

    public static final String MODID = "hyauto";
    public static final String NAME = "HyAuto";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static HyAutoMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("[HyAuto] PreInit event triggered.");

        // Load config
        ConfigManager.preInit(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[HyAuto] Init event triggered.");

        // Register tick event listener
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new ConnectionHandler());


        // No need to register macros manually anymore!
        // Macros are listed inside MacroManager.getAvailableMacros()
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("[HyAuto] PostInit event triggered.");
    }
}
