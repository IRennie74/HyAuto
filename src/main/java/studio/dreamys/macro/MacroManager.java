package studio.dreamys.macro;

import java.util.Arrays;
import java.util.List;

public class MacroManager {
    private static Macro currentMacro;  // Only one active macro at a time

    // Called every tick
    public static void tick() {
        if (currentMacro != null) {
            if (!currentMacro.isFinished()) {
                currentMacro.tick();  // Let the macro run its logic
            } else {
                stopCurrentMacro(); // Auto-stop if macro reports finished
            }
        }
    }

    // Returns all available macros (you can add more)
    public static List<Macro> getAvailableMacros() {
        return Arrays.asList(
                new ExampleMacro(),
                new AnotherExampleMacro()
        );
    }

    // Start a macro
    public static void startMacro(Macro macro) {
        stopCurrentMacro(); // Always stop previous one first
        currentMacro = macro;
        macro.start();
    }

    // Stop whatever macro is running
    public static void stopCurrentMacro() {
        if (currentMacro != null) {
            currentMacro.stop();
            currentMacro = null;
        }
    }

    // Check if something is running
    public static boolean isRunning() {
        return currentMacro != null;
    }

    // Get name of current macro
    public static String getCurrentMacroName() {
        return currentMacro != null ? currentMacro.getName() : "None";
    }
}
