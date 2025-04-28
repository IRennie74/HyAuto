package studio.dreamys.macro;

public class ExampleMacro implements Macro {
    private boolean done = false;
    private int ticksPassed = 0;

    @Override
    public void start() {
        System.out.println("[HyAuto] ExampleMacro starting...");
        // Prepare any setup needed before starting
    }

    @Override
    public void tick() {
        // Every tick, we can simulate action
        ticksPassed++;

        if (ticksPassed > 20) { // Example: after 1 second at 20 TPS
            done = true;
        }
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public String getName() {
        return "Example Macro";
    }
}
