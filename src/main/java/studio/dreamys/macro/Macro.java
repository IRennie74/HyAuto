package studio.dreamys.macro;

public abstract class Macro {

    // Called when the macro is first started
    public abstract void start();

    // Called every game tick
    public abstract void tick();

    // Called when the macro is stopped
    public abstract void stop();

    // Returns whether the macro is finished
    public abstract boolean isFinished();

    // Optional: Name for displaying in GUI
    public abstract String getName();
}
