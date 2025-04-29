package studio.dreamys.macro;

public abstract class Macro {
    public abstract void start();
    public abstract void tick();
    public abstract void stop();
    public abstract boolean isFinished();
    public abstract String getName();
}
