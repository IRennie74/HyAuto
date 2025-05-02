package studio.dreamys.macro;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreObjective;

public class LoginTransitionMacro implements Macro {
    private boolean done = false;
    private long startTime;
    private boolean sentLobby, sentSkyblock;

    /** Only run if we’re not already in SkyBlock */
    @Override
    public boolean shouldRun() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null) return true;      // world not loaded, allow transition
        try {
            Scoreboard sb = mc.theWorld.getScoreboard();
            ScoreObjective obj = sb.getObjective("sbinfo");
            return (obj == null);  // if no sbinfo objective → not in SkyBlock → run
        } catch (Exception e) {
            // In case scoreboard is null or objective name changed
            return true;
        }
    }

    @Override
    public void start() {
        done = false;
        sentLobby = sentSkyblock = false;
        startTime = System.currentTimeMillis();
        System.out.println("[HyAuto] LoginTransitionMacro started");
    }

    @Override
    public void tick() {
        Minecraft mc = Minecraft.getMinecraft();
        // don’t proceed until player & world exist
        if (mc.thePlayer == null || mc.theWorld == null) return;

        long now = System.currentTimeMillis();
        if (!sentLobby && now - startTime > 1000) {
            mc.thePlayer.sendChatMessage("/lobby");
            sentLobby = true;
            startTime = now;
            System.out.println("[HyAuto] Sent /lobby");
        }
        if (sentLobby && !sentSkyblock && now - startTime > 5000) {
            mc.thePlayer.sendChatMessage("/skyblock");
            sentSkyblock = true;
            startTime = now;
            System.out.println("[HyAuto] Sent /skyblock");
        }
        if (sentLobby && sentSkyblock && now - startTime > 5000) {
            done = true;
            System.out.println("[HyAuto] LoginTransitionMacro finished");
        }
    }

    @Override
    public void stop() {
        // nothing special
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public String getName() {
        return "LoginTransitionMacro";
    }
}
