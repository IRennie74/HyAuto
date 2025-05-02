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
        //Checks to see if player is in game
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (event.phase != TickEvent.Phase.END) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiHyAuto());
        }

        MacroQueueManager.tick();

        // Handle delayed startup
        if (ConnectionHandler.queueShouldStart) {
            queueStartupDelay++;
            if (queueStartupDelay >= 40) {
                System.out.println("[HyAuto] Starting macro queue after safe delay.");
                MacroQueueManager.initializeQueue();
                ConnectionHandler.queueShouldStart = false;
                queueStartupDelay = 0;
            }
        }

        // Status reporting
        tickCounter++;
        if (tickCounter >= 100) {
            tickCounter = 0;

            String uuid = Minecraft.getMinecraft().getSession().getPlayerID();
            String username = Minecraft.getMinecraft().getSession().getUsername();
            String status = MacroQueueManager.isRunning() ? "Running" : "Idle";
            String macro = MacroQueueManager.getCurrentMacroName();
            long ram = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            double tps = 20.0;

            //causes game to lag if not on new thread
            new Thread(() -> {
                StatusReporter.sendStatusUpdate(uuid, username, status, tps, ram, macro);
            }).start();

        }
    }
}
