package studio.dreamys.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import studio.dreamys.macro.Macro;
import studio.dreamys.macro.MacroManager;
import studio.dreamys.macro.MacroQueueManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiHyAuto extends GuiScreen {

    private GuiButton startButton;
    private GuiButton stopButton;
    private GuiButton safetyToggle;
    private GuiButton nextQueueButton;
    private GuiButton prevQueueButton;

    private List<String> queueNames;
    private int currentQueueIndex = 0;
    private boolean safetyEnabled = true;

    @Override
    public void initGui() {
        queueNames = new ArrayList<>(MacroQueueManager.getAvailableQueues());

        this.buttonList.clear();

        startButton = new GuiButton(0, width / 2 - 60, height / 2 - 20, 120, 20, "Start Queue");
        stopButton = new GuiButton(4, width / 2 - 60, height / 2 + 10, 120, 20, "Stop");
        safetyToggle = new GuiButton(1, width / 2 - 60, height / 2 + 40, 120, 20, "Safety: ON");
        prevQueueButton = new GuiButton(2, width / 2 - 90, height / 2 - 60, 20, 20, "<");
        nextQueueButton = new GuiButton(3, width / 2 + 70, height / 2 - 60, 20, 20, ">");

        this.buttonList.add(prevQueueButton);
        this.buttonList.add(nextQueueButton);
        this.buttonList.add(startButton);
        this.buttonList.add(stopButton);
        this.buttonList.add(safetyToggle);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: // Start selected queue
                MacroQueueManager.startQueue(queueNames.get(currentQueueIndex));
                break;

            case 4: // Stop
                MacroQueueManager.stopAll();
                break;

            case 1: // Toggle safety
                safetyEnabled = !safetyEnabled;
                safetyToggle.displayString = "Safety: " + (safetyEnabled ? "ON" : "OFF");
                break;

            case 2: // Prev queue
                currentQueueIndex = (currentQueueIndex - 1 + queueNames.size()) % queueNames.size();
                break;

            case 3: // Next queue
                currentQueueIndex = (currentQueueIndex + 1) % queueNames.size();
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, "HyAuto Control Panel", width / 2, height / 2 - 80, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "Queue: " + queueNames.get(currentQueueIndex), width / 2, height / 2 - 40, 0xAACCFF);
        drawCenteredString(this.fontRendererObj, "Running: " + MacroQueueManager.getCurrentQueueName(), width / 2, height / 2 + 60, 0xFFDD55);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
