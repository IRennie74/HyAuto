package studio.dreamys.macro;

import java.util.*;

public class MacroQueueManager {
    private static final Map<String, List<Macro>> predefinedQueues = new LinkedHashMap<>();
    private static Queue<Macro> macroQueue = new LinkedList<>();
    private static Macro currentMacro;
    private static String currentQueueName = "None";
    private static boolean enabled = false;
    private static boolean loopCurrentQueue = true;

    static {
        setupQueues();
    }

    // Define your simple named queues here
    public static void setupQueues() {
        predefinedQueues.clear();
        predefinedQueues.put("Transition", Arrays.asList(
                new LoginTransitionMacro()
        ));
        predefinedQueues.put("FarmingHub", Arrays.asList(
//                new FarmingHubSetupMacro(),
//                new FarmingHubMacro()
        ));
        predefinedQueues.put("FarmingGarden", Arrays.asList(
//                new GardenSetupMacro(),
//                new GardenFarmingMacro()
        ));
        predefinedQueues.put("Mining", Arrays.asList(
//                new MiningMacro()
        ));
        // … add more as needed
    }

    /**
     * Start a named queue.
     * @param name name of the queue
     * @param loop whether to loop this queue forever
     */
    public static void startQueue(String name, boolean loop) {
        stopAll();
        List<Macro> queue = predefinedQueues.get(name);
        if (queue == null || queue.isEmpty()) {
            System.out.println("[HyAuto] Queue not found: " + name);
            return;
        }
        macroQueue.clear();
        macroQueue.addAll(queue);
        currentMacro = null;
        currentQueueName = name;
        enabled = true;
        loopCurrentQueue = loop;
        System.out.println("[HyAuto] Started queue: " + name + (loop ? " (looping)" : ""));
    }

    /** Shortcut to start & loop */
    public static void startQueue(String name) {
        startQueue(name, true);
    }

    /** Run one tick of the current queue */
    public static void tick() {
        if (!enabled) return;

        if (currentMacro == null && !macroQueue.isEmpty()) {
            currentMacro = macroQueue.poll();
            currentMacro.start();
            System.out.println("[HyAuto] Running macro: " + currentMacro.getName());
        }

        if (currentMacro != null) {
            if (!currentMacro.isFinished()) {
                currentMacro.tick();
            } else {
                currentMacro.stop();
                currentMacro = null;
            }
        }

        // if queue empty and no macro running → either loop or finish
        if (currentMacro == null && macroQueue.isEmpty()) {
            if (loopCurrentQueue) {
                System.out.println("[HyAuto] Looping queue: " + currentQueueName);
                startQueue(currentQueueName, true);
            } else {
                System.out.println("[HyAuto] Finished queue: " + currentQueueName);
                enabled = false;
                currentQueueName = "None";
            }
        }
    }

    public static boolean isRunning() {
        return enabled && currentMacro != null;
    }

    public static String getCurrentMacroName() {
        return currentMacro != null ? currentMacro.getName() : "None";
    }

    public static String getCurrentQueueName() {
        return enabled ? currentQueueName : "Stopped";
    }

    public static void stopAll() {
        if (currentMacro != null) currentMacro.stop();
        macroQueue.clear();
        currentMacro = null;
        currentQueueName = "None";
        enabled = false;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static Set<String> getAvailableQueues() {
        return predefinedQueues.keySet();
    }

    /** Return the names of the next few macros in the queue (without dequeueing). */
    public static List<String> peekUpcoming(int max) {
        List<String> upcoming = new ArrayList<>();
        if (!enabled) return upcoming;
        // copy queue
        Iterator<Macro> it = macroQueue.iterator();
        int count = 0;
        while (it.hasNext() && count < max) {
            upcoming.add(it.next().getName());
            count++;
        }
        return upcoming;
    }

}
