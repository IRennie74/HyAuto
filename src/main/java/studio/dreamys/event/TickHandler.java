package studio.dreamys.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import studio.dreamys.gui.GuiHyAuto;
import studio.dreamys.macro.MacroManager; // Import MacroManager to access tick method

public class TickHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            //GUI/Controls
            if (Keyboard.isKeyDown(Keyboard.KEY_G)) { // G key
                Minecraft.getMinecraft().displayGuiScreen(new GuiHyAuto());
            }

            MacroManager.tick();  // Call MacroManager's tick method to manage all macro
        }
    }
}
