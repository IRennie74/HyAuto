package studio.dreamys.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
import studio.dreamys.macro.Macro;
import studio.dreamys.macro.MacroManager;

import java.io.IOException;
import java.util.List;

public class HyAutoGui extends GuiScreen {

    private GuiButton startButton;
    private GuiButton stopButton;
    private GuiButton safetyToggle;
    private GuiButton macroDropdown;

    private boolean safetyMode = true;
    private List<Macro> macros = MacroManager.getAvailableMacros();
    private int selectedMacro = 0;

    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int y = this.height / 4;

        macroDropdown = new GuiButton(0, centerX - 100, y, 200, 20, "Macro: " + macros.get(selectedMacro).getName());
        startButton = new GuiButton(1, centerX - 100, y + 25, 98, 20, "Start");
        stopButton = new GuiButton(2, centerX + 2, y + 25, 98, 20, "Stop");
        safetyToggle = new GuiButton(3, centerX - 100, y + 50, 200, 20, "Safety: ON");

        this.buttonList.clear();
        this.buttonList.add(macroDropdown);
        this.buttonList.add(startButton);
        this.buttonList.add(stopButton);
        this.buttonList.add(safetyToggle);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                // Cycle macros
                selectedMacro = (selectedMacro + 1) % macros.size();
                macroDropdown.displayString = "Macro: " + macros.get(selectedMacro).getName();
                break;
            case 1:
                MacroManager.startMacro(macros.get(selectedMacro));
                break;
            case 2:
                MacroManager.stopCurrentMacro();
                break;
            case 3:
                safetyMode = !safetyMode;
                safetyToggle.displayString = "Safety: " + (safetyMode ? "ON" : "OFF");
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "HyAuto Control Panel", this.width / 2, 15, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "Currently Running: " + MacroManager.getCurrentMacroName(), this.width / 2, 40 + 60, 0xAAAAAA);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
