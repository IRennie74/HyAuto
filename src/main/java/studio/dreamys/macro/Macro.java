package studio.dreamys.macro;

public interface Macro {
    void start();          // Called once when macro starts
    void tick();           // Called every client tick
    boolean isFinished();  // Returns true when the macro is done
    String getName();      // Returns macro name (for logging/GUI)
}
