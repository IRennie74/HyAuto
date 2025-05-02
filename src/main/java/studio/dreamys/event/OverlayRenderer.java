package studio.dreamys.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import studio.dreamys.macro.MasterQueueManager;
import studio.dreamys.macro.MacroQueueManager;

import java.util.List;

public class OverlayRenderer {
    private final Minecraft mc = Minecraft.getMinecraft();

    public OverlayRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        // Only run once per frame, after all default HUD elements
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;

        FontRenderer fr = mc.fontRendererObj;
        int x = 5, y = 5;
        int bgWidth = 170, bgHeight = 60;
        int bgColor = 0x55000000; // semi-transparent black

        // Draw background box
        Gui.drawRect(x, y, x + bgWidth, y + bgHeight, bgColor);

        // Prepare text
        String stage    = "Stage: " + MasterQueueManager.getCurrentStageName();
        String macro    = "Macro: " + MacroQueueManager.getCurrentMacroName();
        List<String> next = MacroQueueManager.peekUpcoming(3);

        // Draw text
        int white = 0xFFFFFF;
        int grey  = 0xAAAAAA;
        fr.drawStringWithShadow(stage, x + 4,          y + 4, white);
        fr.drawStringWithShadow(macro, x + 4,          y + 16, white);

        // Draw upcoming macros
        int lineY = y + 28;
        for (int i = 0; i < next.size(); i++) {
            String prefix = (i == 0 ? "Next: " : "      ");
            fr.drawStringWithShadow(prefix + next.get(i), x + 4, lineY + i*10, grey);
        }
    }
}
