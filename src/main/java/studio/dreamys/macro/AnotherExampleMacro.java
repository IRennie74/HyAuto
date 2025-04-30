package studio.dreamys.macro;

public class AnotherExampleMacro extends Macro {
    private boolean done = false;
    private int ticksPassed = 0;

    @Override
    public void start() {
        System.out.println("[HyAuto] AnotherExampleMacro starting...");
        done = false;
        ticksPassed = 0;
    }

    @Override
    public void tick() {
        ticksPassed++;
        if (ticksPassed > 40) {
            done = true;
        }
    }

    @Override
    public void stop() {
        System.out.println("[HyAuto] AnotherExampleMacro stopped.");
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
