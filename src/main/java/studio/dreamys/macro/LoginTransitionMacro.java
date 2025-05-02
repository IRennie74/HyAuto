package studio.dreamys.macro;

import net.minecraft.client.Minecraft;

public class LoginTransitionMacro extends Macro {
    private boolean done = false;
    private long startTime = 0;
    private boolean sentLobby = false;
    private boolean sentSkyblock = false;

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
        System.out.println("[HyAuto] LoginTransitionMacro started");
    }

    @Override
    public void tick() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null || mc.theWorld == null) return;

        long now = System.currentTimeMillis();

        // Prevent sending messages every tick
        if (!sentLobby && now - startTime > 1000) {
            System.out.println("[HyAuto] Sending /lobby");
            mc.thePlayer.sendChatMessage("/lobby");
            sentLobby = true;
            startTime = now;
        }

        if (sentLobby && !sentSkyblock && now - startTime > 5000) {
            System.out.println("[HyAuto] Sending /skyblock");
            mc.thePlayer.sendChatMessage("/skyblock");
            sentSkyblock = true;
            startTime = now;
        }

        if (sentLobby && sentSkyblock && now - startTime > 5000) {
            done = true;
            System.out.println("[HyAuto] LoginTransitionMacro done.");
        }
    }

    @Override
    public void stop() {
        System.out.println("[HyAuto] LoginTransitionMacro stopped");
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public String getName() {
        return "Login Transition";
    }
}
