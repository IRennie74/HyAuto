package studio.dreamys;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import studio.dreamys.event.TickHandler;
import studio.dreamys.macro.AnotherExampleMacro;
import studio.dreamys.macro.ExampleMacro;
import studio.dreamys.macro.MacroManager;

@Mod(modid = HyAutoMod.MODID, name = HyAutoMod.NAME, version = HyAutoMod.VERSION)
public class HyAutoMod {

    public static final String MODID = "hyauto";
    public static final String NAME = "HyAuto";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static HyAutoMod instance;

    /**
     * Called first, before Minecraft is fully loaded
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("[HyAuto] PreInit event triggered.");
    }

    /**
     * Called when Minecraft is initializing
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("[HyAuto] Init event triggered.");

        // Register macros
        MacroManager.registerMacro(new ExampleMacro());
        MacroManager.registerMacro(new AnotherExampleMacro());

        // Hook into tick event
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new TickHandler());
    }

    /**
     * Called after everything is loaded
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("[HyAuto] PostInit event triggered.");
    }
}
