package studio.dreamys.macro;

public class AnotherExampleMacro implements Macro {
    private boolean done = false;
    private int ticksPassed = 0;

    @Override
    public void start() {
        System.out.println("[HyAuto] AnotherExampleMacro starting...");
        // Setup anything needed
    }

    @Override
    public void tick() {
        // Simple tick-based macro
        ticksPassed++;

        if (ticksPassed > 40) { // Example: 2 seconds at 20 TPS
            done = true;
        }
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    @Override
    public String getName() {
        return "Another Example Macro";
    }
}
