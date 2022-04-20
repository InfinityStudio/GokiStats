package net.infstudio.goki.client;

import net.infstudio.goki.client.gui.GuiStats;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GokiKeyHandler {
    public static KeyMapping statsMenu;

    public GokiKeyHandler() {
        statsMenu = new KeyMapping(I18n.get("ui.openmenu"), 89, "Goki Stats");
        ClientRegistry.registerKeyBinding(statsMenu);
    }

    @SubscribeEvent
    public void keyDown(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (statsMenu.isDown()) {
            mc.setScreen(new GuiStats());

        }
    }
}
