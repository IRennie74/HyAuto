package studio.dreamys.macro;

public class ExampleMacro extends Macro {
    private boolean done = false;
    private int ticksPassed = 0;

    @Override
    public void start() {
        System.out.println("[HyAuto] ExampleMacro starting...");
        done = false;
        ticksPassed = 0;
    }

    @Override
    public void tick() {
        ticksPassed++;
        if (ticksPassed > 400) {
            done = true;
        }
    }

    @Override
    public void stop() {
        System.out.println("[HyAuto] ExampleMacro stopped.");
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
