package studio.dreamys.macro;

import java.util.LinkedList;
import java.util.Queue;

public class MacroQueueManager {
    private static Queue<Macro> macroQueue = new LinkedList<>();
    private static Macro currentMacro;
    private static boolean enabled = true; // Add this flag

    // Call this once at startup or when restarting
    public static void initializeQueue() {
        macroQueue.clear();

        macroQueue.add(new LoginTransitionMacro());
        macroQueue.add(new ExampleMacro());
        macroQueue.add(new AnotherExampleMacro());

        currentMacro = null;
    }

    public static void tick() {
        if (!enabled) return; // Ignore tick if disabled

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

        // Loop back to the beginning if queue is done
        if (currentMacro == null && macroQueue.isEmpty()) {
            System.out.println("[HyAuto] Restarting macro queue.");
            initializeQueue();
        }
    }

    public static String getCurrentMacroName() {
        return currentMacro != null ? currentMacro.getName() : "None";
    }

    public static boolean isRunning() {
        return enabled && currentMacro != null;
    }

    public static void stopAll() {
        if (currentMacro != null) currentMacro.stop();
        currentMacro = null;
        macroQueue.clear();
        enabled = false;
    }

    public static void enable() {
        enabled = true;
    }

    public static void disable() {
        enabled = false;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}