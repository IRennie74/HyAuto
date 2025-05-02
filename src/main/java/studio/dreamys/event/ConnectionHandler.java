package studio.dreamys.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import studio.dreamys.macro.MasterQueueManager;

public class ConnectionHandler {
    /** Set to true when we detect a Hypixel connect and haven’t already started. */
    public static boolean masterShouldStart = false;
    /** Prevent retrigger until we’ve disconnected. */
    public static boolean canTriggerMaster = true;

    @SubscribeEvent
    public void onConnect(ClientConnectedToServerEvent event) {
        // Only trigger on Hypixel
        String ip = net.minecraft.client.Minecraft.getMinecraft()
                .getCurrentServerData()
                .serverIP;
        if (ip != null && ip.contains("hypixel.net") && canTriggerMaster) {
            System.out.println("[HyAuto] Connected to Hypixel — master pipeline will start shortly.");
            masterShouldStart = true;
            canTriggerMaster = false;
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        System.out.println("[HyAuto] Disconnected from server — rearming master trigger.");
        canTriggerMaster = true;
    }
}
