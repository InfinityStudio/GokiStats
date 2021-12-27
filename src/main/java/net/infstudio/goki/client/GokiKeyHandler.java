package net.infstudio.goki.client;

import net.infstudio.goki.client.gui.GuiCompatibilityHelper;
import net.infstudio.goki.client.gui.GuiStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class GokiKeyHandler {
    public static KeyBinding statsMenu;
    public static KeyBinding compatibilityMenu;

    public GokiKeyHandler() {
        statsMenu = new KeyBinding(I18n.get("ui.openmenu"), 89, "Goki Stats");
        compatibilityMenu = new KeyBinding(I18n.get("ui.openhelper"), -1, "Goki Stats");
        ClientRegistry.registerKeyBinding(statsMenu);
        ClientRegistry.registerKeyBinding(compatibilityMenu);
    }

    @SubscribeEvent
    public void keyDown(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (statsMenu.isDown()) {
            mc.setScreen(new GuiStats());

        } else if (compatibilityMenu.isDown()) {
            mc.setScreen(new GuiCompatibilityHelper());
        }
    }
}
