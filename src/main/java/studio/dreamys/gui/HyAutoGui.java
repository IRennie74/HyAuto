package studio.dreamys.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import studio.dreamys.macro.Macro;
import studio.dreamys.macro.MacroManager;

import java.io.IOException;
import java.util.List;

public class HyAutoGui extends GuiScreen {
    private GuiButton startButton;
    private GuiButton stopButton;
    private GuiButton cycleMacroButton;
    private GuiButton toggleSafetyButton;

    private int selectedMacroIndex = 0;
    private boolean safetyEnabled = true;

    private List<Macro> availableMacros;

    @Override
    public void initGui() {
        buttonList.clear();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        availableMacros = MacroManager.getAvailableMacros(); // Fetch macros from manager

        // Buttons
        startButton = new GuiButton(0, centerX - 100, centerY - 40, 200, 20, "Start Macro");
        stopButton = new GuiButton(1, centerX - 100, centerY - 10, 200, 20, "Stop Macro");
        cycleMacroButton = new GuiButton(2, centerX - 100, centerY + 20, 200, 20, "Macro: " + getSelectedMacroName());
        toggleSafetyButton = new GuiButton(3, centerX - 100, centerY + 50, 200, 20, "Safety: ON");

        buttonList.add(startButton);
        buttonList.add(stopButton);
        buttonList.add(cycleMacroButton);
        buttonList.add(toggleSafetyButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: // Start
                Macro selected = getSelectedMacro();
                if (selected != null) {
                    MacroManager.startMacro(selected);
                    System.out.println("[HyAuto] Started macro: " + selected.getName());
                }
                break;
            case 1: // Stop
                MacroManager.stopCurrentMacro();
                System.out.println("[HyAuto] Stopped macro.");
                break;
            case 2: // Cycle Macro
                selectedMacroIndex++;
                if (selectedMacroIndex >= availableMacros.size()) {
                    selectedMacroIndex = 0;
                }
                cycleMacroButton.displayString = "Macro: " + getSelectedMacroName();
                break;
            case 3: // Toggle Safety
                safetyEnabled = !safetyEnabled;
                toggleSafetyButton.displayString = "Safety: " + (safetyEnabled ? "ON" : "OFF");
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int centerX = this.width / 2;
        int topY = 40;

        // Title
        drawCenteredString(this.fontRendererObj, "HyAuto Macro Control", centerX, topY, 0xFFFFFF);

        // Status
        String status = MacroManager.isRunning()
                ? "Running: " + MacroManager.getCurrentMacroName()
                : "No Macro Running";
        drawCenteredString(this.fontRendererObj, status, centerX, topY + 15, 0xAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private Macro getSelectedMacro() {
        if (availableMacros.isEmpty()) return null;
        return availableMacros.get(selectedMacroIndex);
    }

    private String getSelectedMacroName() {
        Macro macro = getSelectedMacro();
        return macro != null ? macro.getName() : "None";
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
