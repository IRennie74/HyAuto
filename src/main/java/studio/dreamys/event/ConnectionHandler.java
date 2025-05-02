package studio.dreamys.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import studio.dreamys.macro.MacroQueueManager;

public class ConnectionHandler {
    public static boolean queueShouldStart = false;
    public static boolean queueCanStart = true;

    @SubscribeEvent
    public void onConnect(ClientConnectedToServerEvent event) {
        String ip = net.minecraft.client.Minecraft.getMinecraft().getCurrentServerData().serverIP;
        if (ip != null && ip.contains("hypixel.net")) {
            System.out.println("[HyAuto] Connected to Hypixel — macro queue will start shortly.");
            if(queueCanStart) {
                queueShouldStart = true; // trigger in a safe place
                queueCanStart = false;
            }
        }
    }
    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        System.out.println("Disconnected from server!");
            queueCanStart = true; // trigger in a safe place
    }
}

