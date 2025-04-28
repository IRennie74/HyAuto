package studio.dreamys.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import studio.dreamys.macro.MacroManager; // Import MacroManager to access tick method

public class TickHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MacroManager.tick();  // Call MacroManager's tick method to manage all macros
        }
    }
}
