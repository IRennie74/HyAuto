package studio.dreamys.macro;

import java.util.*;

public class MacroQueueManager {
    private static final Map<String, List<Macro>> predefinedQueues = new LinkedHashMap<>();
    private static Queue<Macro> macroQueue = new LinkedList<>();
    private static Macro currentMacro;
    private static String currentQueueName = "None";
    private static boolean enabled = true;

    static {
        setupQueues();
    }

    public static void setupQueues() {
        predefinedQueues.clear();

        predefinedQueues.put("Transition", Arrays.asList(
                new LoginTransitionMacro()
        ));

        predefinedQueues.put("Mining", Arrays.asList(
                new ExampleMacro(),
                new AnotherExampleMacro()
        ));

//        predefinedQueues.put("Combat", Arrays.asList(
//                new CombatMacro()
//        ));
    }

    public static void startQueue(String name) {
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

        System.out.println("[HyAuto] Started queue: " + name);
    }

    public static void tick() {
        if (!enabled) return;

        if (currentMacro == null && !macroQueue.isEmpty()) {
            currentMacro = macroQueue.poll();
            currentMacro.start();
            System.out.println("[HyAuto] Starting macro: " + currentMacro.getName());
        }

        if (currentMacro != null) {
            if (!currentMacro.isFinished()) {
                currentMacro.tick();
            } else {
                currentMacro.stop();
                currentMacro = null;
            }
        }

        if (currentMacro == null && macroQueue.isEmpty()) {
            System.out.println("[HyAuto] Queue complete. Restarting: " + currentQueueName);
            startQueue(currentQueueName); // loop current queue
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
}
