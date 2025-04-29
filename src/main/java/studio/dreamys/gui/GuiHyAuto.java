package studio.dreamys.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import studio.dreamys.macro.Macro;
import studio.dreamys.macro.MacroManager;

import java.io.IOException;
import java.util.List;

public class GuiHyAuto extends GuiScreen {

    private GuiButton startButton;
    private GuiButton safetyToggle;
    private GuiButton nextMacroButton;
    private GuiButton prevMacroButton;

    private List<Macro> macros;
    private int currentIndex = 0;
    private boolean safetyEnabled = true;

    @Override
    public void initGui() {
        macros = MacroManager.getAvailableMacros();

        this.buttonList.clear();

        startButton = new GuiButton(0, width / 2 - 60, height / 2 - 20, 120, 20, "Start");
        safetyToggle = new GuiButton(1, width / 2 - 60, height / 2 + 10, 120, 20, "Safety: ON");
        prevMacroButton = new GuiButton(2, width / 2 - 90, height / 2 - 60, 20, 20, "<");
        nextMacroButton = new GuiButton(3, width / 2 + 70, height / 2 - 60, 20, 20, ">");

        this.buttonList.add(prevMacroButton);
        this.buttonList.add(nextMacroButton);
        this.buttonList.add(startButton);
        this.buttonList.add(safetyToggle);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: // Start
                MacroManager.startMacro(macros.get(currentIndex));
                break;

            case 1: // Toggle safety
                safetyEnabled = !safetyEnabled;
                button.displayString = "Safety: " + (safetyEnabled ? "ON" : "OFF");
                break;

            case 2: // Prev macro
                if (currentIndex > 0) currentIndex--;
                break;

            case 3: // Next macro
                if (currentIndex < macros.size() - 1) currentIndex++;
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Main box
        drawCenteredString(this.fontRendererObj, "HyAuto Control Panel", width / 2, height / 2 - 80, 0xFFFFFF);
        drawCenteredString(this.fontRendererObj, "Selected: " + macros.get(currentIndex).getName(), width / 2, height / 2 - 40, 0xAACCFF);
        drawCenteredString(this.fontRendererObj, "Currently Running: " + MacroManager.getCurrentMacroName(), width / 2, height / 2 + 40, 0xFFDD55);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
