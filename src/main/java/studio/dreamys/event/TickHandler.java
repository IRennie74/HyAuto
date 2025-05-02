package studio.dreamys.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import studio.dreamys.gui.GuiHyAuto;
import studio.dreamys.macro.MacroQueueManager;
import studio.dreamys.network.StatusReporter;

public class TickHandler {
    private int tickCounter = 0;
    private int queueStartupDelay = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (event.phase != TickEvent.Phase.END) return;

        // Open GUI
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            mc.displayGuiScreen(new GuiHyAuto());
        }

        // Delayed start after connect
        if (ConnectionHandler.queueShouldStart) {
            queueStartupDelay++;
            if (queueStartupDelay >= 40) {
                System.out.println("[HyAuto] Delayed start: launching default queue.");
                // Replace "Transition" with whichever queue you want as the default on connect
                MacroQueueManager.startQueue("Transition");
                ConnectionHandler.queueShouldStart = false;
                queueStartupDelay = 0;
            }
        }

        // Tick whatever queue is running
        MacroQueueManager.tick();

        // Status report every 5s
        tickCounter++;
        if (tickCounter >= 100) {
            tickCounter = 0;
            final String uuid = mc.getSession().getPlayerID();
            final String username = mc.getSession().getUsername();
            final String queue = MacroQueueManager.getCurrentQueueName();
            final String macro = MacroQueueManager.getCurrentMacroName();
            final String status = MacroQueueManager.isRunning() ? "Running" : "Idle";
            final long ram     = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            final double tps   = 20.0;

            new Thread(() -> {
                StatusReporter.sendStatusUpdate(uuid, username, status, tps, ram, macro);
            }).start();
        }
    }
}
