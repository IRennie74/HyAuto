package studio.dreamys.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import studio.dreamys.macro.MasterQueueManager;
import studio.dreamys.macro.MacroQueueManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiHyAuto extends GuiScreen {

    private GuiButton startQueueBtn;
    private GuiButton startMasterBtn;
    private GuiButton stopBtn;
    private GuiButton safetyToggle;
    private GuiButton prevQueueBtn;
    private GuiButton nextQueueBtn;

    private List<String> queueNames;
    private int currentQueueIndex = 0;
    private boolean safetyEnabled = true;

    @Override
    public void initGui() {
        // load all named queues
        queueNames = new ArrayList<>(MacroQueueManager.getAvailableQueues());

        this.buttonList.clear();

        // Buttons:
        startQueueBtn  = new GuiButton(0,  width/2 - 60, height/2 - 20, 120, 20, "Start Queue");
        startMasterBtn = new GuiButton(5,  width/2 - 60, height/2 +  5, 120, 20, "Start All Stages");
        stopBtn        = new GuiButton(4,  width/2 - 60, height/2 + 30, 120, 20, "Stop");
        safetyToggle   = new GuiButton(1,  width/2 - 60, height/2 + 55, 120, 20, "Safety: ON");
        prevQueueBtn   = new GuiButton(2,  width/2 - 90, height/2 - 60, 20, 20, "<");
        nextQueueBtn   = new GuiButton(3,  width/2 + 70, height/2 - 60, 20, 20, ">");

        this.buttonList.add(prevQueueBtn);
        this.buttonList.add(nextQueueBtn);
        this.buttonList.add(startQueueBtn);
        this.buttonList.add(startMasterBtn);
        this.buttonList.add(stopBtn);
        this.buttonList.add(safetyToggle);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: // Start just the selected named queue (looping)
                MacroQueueManager.startQueue(queueNames.get(currentQueueIndex), true);
                break;
            case 5: // Start the full master pipeline
                MasterQueueManager.startMaster();
                break;
            case 4: // Stop everything
                // Stops both queue manager and master pipeline
                MacroQueueManager.stopAll();
                // if master was running, stopping it:
                // we can simply stop all macros as well
                break;
            case 1: // Toggle safety
                safetyEnabled = !safetyEnabled;
                safetyToggle.displayString = "Safety: " + (safetyEnabled ? "ON" : "OFF");
                break;
            case 2: // Previous queue in the list
                currentQueueIndex = (currentQueueIndex - 1 + queueNames.size()) % queueNames.size();
                break;
            case 3: // Next queue in the list
                currentQueueIndex = (currentQueueIndex + 1) % queueNames.size();
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Title
        drawCenteredString(this.fontRendererObj, "HyAuto Control Panel", width/2, height/2 - 100, 0xFFFFFF);

        // Selected named queue
        drawCenteredString(this.fontRendererObj,
                "Queue: " + queueNames.get(currentQueueIndex),
                width/2, height/2 - 70, 0xAACCFF);

        // Current macro within the named queue
        drawCenteredString(this.fontRendererObj,
                "Running: " + MacroQueueManager.getCurrentQueueName(),
                width/2, height/2 + 80, 0xFFDD55);

        // Current master pipeline stage
        drawCenteredString(this.fontRendererObj,
                "Master Stage: " + MasterQueueManager.getCurrentStageName(),
                width/2, height/2 + 105, 0xFFAA00);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
