package studio.dreamys.macro;

import java.util.Arrays;
import java.util.List;

public class MasterQueueManager {
    private static final List<Stage> stages = Arrays.asList(
            new Stage("Transition", false),
            new Stage("FarmingHub", false),
            new Stage("FarmingGarden", false),
            new Stage("Mining", true)    // final stage loops by itself
    );
    private static int currentStage = 0;
    private static boolean running = false;

    public static void startMaster() {
        currentStage = 0;
        running = true;
        launchCurrentStage();
    }

    private static void launchCurrentStage() {
        Stage s = stages.get(currentStage);
        MacroQueueManager.startQueue(s.queueName, s.loop);
        System.out.println("[HyAuto] Master: starting stage " + s.queueName);
    }

    public static void tick() {
        if (!running) return;
        MacroQueueManager.tick();
        // if that stage finished
        if (!MacroQueueManager.isEnabled()) {
            currentStage++;
            if (currentStage < stages.size()) {
                launchCurrentStage();
            } else {
                System.out.println("[HyAuto] Master complete. Stopping.");
                running = false;
            }
        }
    }

    public static boolean isRunning() {
        return running;
    }

    public static String getCurrentStageName() {
        return running ? stages.get(currentStage).queueName : "None";
    }

    // simple struct for a stage
    private static class Stage {
        String queueName;
        boolean loop;
        Stage(String q, boolean l) { queueName = q; loop = l; }
    }
}
