package studio.dreamys.macro;

public interface Macro {
    void start();
    void tick();
    void stop();
    boolean isFinished();
    String getName();

    /** Return true if this macro still needs to run; false to skip it. */
    default boolean shouldRun() {
        return true;
    }
}
