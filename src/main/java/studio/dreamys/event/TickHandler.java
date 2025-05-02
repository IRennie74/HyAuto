package studio.dreamys.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import studio.dreamys.gui.GuiHyAuto;
import studio.dreamys.macro.MacroQueueManager;
import studio.dreamys.macro.MasterQueueManager;
import studio.dreamys.network.StatusReporter;

public class TickHandler {
    private int tickCounter = 0;
    private int queueStartupDelay = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        // Wait until world & player are ready
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (event.phase != TickEvent.Phase.END) return;

        // Open GUI
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            mc.displayGuiScreen(new GuiHyAuto());
        }

        // Delayed start after connection
        if (ConnectionHandler.masterShouldStart) {
            queueStartupDelay++;
            if (queueStartupDelay >= 40) {
                System.out.println("[HyAuto] Launching master pipeline now.");
                MasterQueueManager.startMaster();
                ConnectionHandler.masterShouldStart = false;
                queueStartupDelay = 0;
            }
        }

        // Run the master "queue of queues"
        MasterQueueManager.tick();

        // Status report every ~5 seconds
        tickCounter++;
        if (tickCounter >= 100) {
            tickCounter = 0;

            final String uuid      = mc.getSession().getPlayerID();
            final String username  = mc.getSession().getUsername();
            final String stage     = MasterQueueManager.getCurrentStageName();
            final String macroName = MasterQueueManager.isRunning()
                    ? MacroQueueManager.getCurrentMacroName()
                    : "None";
            final String status    = MasterQueueManager.isRunning() ? "Running" : "Idle";
            final long   ram       = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            final double tps       = 20.0;

            // Offload network I/O to avoid hitches
            new Thread(() -> {
                StatusReporter.sendStatusUpdate(
                        uuid,
                        username,
                        status,
                        tps,
                        ram,
                        stage + " → " + macroName   // report stage and macro
                );
            }).start();
        }
    }
}
