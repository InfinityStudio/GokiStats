package net.goki.handlers;

import net.goki.GokiStats;
import net.goki.client.gui.GuiStats;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class GokiKeyHandler
{
	public static KeyBinding statsMenu = new KeyBinding(StatCollector.translateToLocal("ui.opmenu.name"), 21, "Goki Stats");
	public static KeyBinding compatibilityMenu = new KeyBinding(StatCollector.translateToLocal("ui.openhelper.name"), 35, "Goki Stats");

	public GokiKeyHandler()
	{
		ClientRegistry.registerKeyBinding(statsMenu);
		ClientRegistry.registerKeyBinding(compatibilityMenu);
	}

	@SubscribeEvent
	public void keyDown(InputEvent.KeyInputEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.thePlayer;
		if (statsMenu.isPressed())
		{
			player.closeScreen();
			player.openGui(	GokiStats.instance,
							0,
							player.worldObj,
							(int) player.posX,
							(int) player.posY,
							(int) player.posZ);

		}
		else if (compatibilityMenu.isPressed())
		{
			player.openGui(	GokiStats.instance,
							1,
							player.worldObj,
							(int) player.posX,
							(int) player.posY,
							(int) player.posZ);
		}
	}
}