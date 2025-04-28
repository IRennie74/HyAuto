package studio.dreamys.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {
    private static List<Macro> activeMacros = new ArrayList<>();  // List of active macros

    // Registers a new macro
    public static void registerMacro(Macro macro) {
        activeMacros.add(macro);
    }

    // Updates all active macros
    public static void tick() {
        for (Macro macro : activeMacros) {
            if (!macro.isFinished()) {
                macro.tick();  // Call tick on each macro
            }
        }
    }

    // Optional: A method to stop a macro
    public static void stopMacro(Macro macro) {
        activeMacros.remove(macro);
    }

    // Optional: A method to clear all active macros
    public static void clearMacros() {
        activeMacros.clear();
    }
}
