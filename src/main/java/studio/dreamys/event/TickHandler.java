package studio.dreamys.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import studio.dreamys.gui.GuiHyAuto;
import studio.dreamys.macro.MacroManager;
import studio.dreamys.network.StatusReporter;

public class TickHandler {
    private int tickCounter = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        //GUI/Controls
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) { // G key
            Minecraft.getMinecraft().displayGuiScreen(new GuiHyAuto());
        }

        MacroManager.tick();

        tickCounter++;
        if (tickCounter >= 100) { // every 5 seconds (assuming 20 TPS)
            tickCounter = 0;

            String uuid = net.minecraft.client.Minecraft.getMinecraft().getSession().getPlayerID();
            String status = MacroManager.isRunning() ? "Running" : "Idle";
            String macro = MacroManager.getCurrentMacroName();
            long ram = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            double tps = 20.0; // optionally replace with real TPS tracking

            StatusReporter.sendStatusUpdate(uuid, status, tps, ram, macro);
        }
    }
}
