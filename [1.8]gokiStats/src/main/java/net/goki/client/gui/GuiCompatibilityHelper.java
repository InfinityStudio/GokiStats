package net.goki.client.gui;

import net.goki.lib.Reference;
import net.goki.stats.Stat;
import net.goki.stats.tool.ToolSpecificStat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class GuiCompatibilityHelper extends GuiScreen
{
	private EntityPlayer player = null;
	private final int BUTTON_WIDTH = 240;
	private final int BUTTON_HEIGHT = 15;
	private final ToolSpecificStat[] compatibleStats =
	{ Stat.STAT_MINING, Stat.STAT_DIGGING, Stat.STAT_CHOPPING, Stat.STAT_TRIMMING, Stat.STAT_SWORDSMANSHIP, Stat.STAT_BOWMANSHIP };

	public GuiCompatibilityHelper(EntityPlayer player)
	{
		this.player = player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui()
	{
		int button = 0;
		int x = this.width / 2;
		int y = this.height / 2 - 45;
		this.buttonList.add(new GuiExtendedButton(button, x - 120, y - 15 + 15 * button++, BUTTON_WIDTH, BUTTON_HEIGHT, "Add item to Mining list", 3355443));
		this.buttonList.add(new GuiExtendedButton(button, x - 120, y - 15 + 15 * button++, BUTTON_WIDTH, BUTTON_HEIGHT, "Add item to Digging list", 3355443));
		this.buttonList.add(new GuiExtendedButton(button, x - 120, y - 15 + 15 * button++, BUTTON_WIDTH, BUTTON_HEIGHT, "Add item to Chopping list", 3355443));
		this.buttonList.add(new GuiExtendedButton(button, x - 120, y - 15 + 15 * button++, BUTTON_WIDTH, BUTTON_HEIGHT, "Add item to Trimming list", 3355443));
		this.buttonList.add(new GuiExtendedButton(button, x - 120, y - 15 + 15 * button++, BUTTON_WIDTH, BUTTON_HEIGHT, "Add item to Swordsmanship list", 3355443));
		this.buttonList.add(new GuiExtendedButton(button, x - 120, y - 15 + 15 * button++, BUTTON_WIDTH, BUTTON_HEIGHT, "Add item to Bowmanship list", 3355443));
		checkStatus();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
		drawCenteredString(	this.mc.fontRendererObj,
							"This is CLIENT-SIDE. Copy the config to the server and /reloadGokiStats.",
							this.width / 2,
							16,
							16777215);
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (this.compatibleStats[button.id].needAffectedByStat(this.player.getHeldItem()))
		{
			this.compatibleStats[button.id].removeSupportForItem(this.player.getHeldItem());
		}
		else
		{
			this.compatibleStats[button.id].addSupportForItem(this.player.getHeldItem());
		}
		Reference.configuration.save();
		checkStatus();
	}

	public void checkStatus()
	{
		Reference.configuration.load();
		for (int i = 0; i < this.compatibleStats.length; i++)
		{
			if (this.compatibleStats[i].needAffectedByStat(this.player.getHeldItem()))
			{
				((GuiExtendedButton) this.buttonList.get(i)).displayString = ("Remove item from " + this.compatibleStats[i].getLocalizedName() + " list.");
				((GuiExtendedButton) this.buttonList.get(i)).setBackgroundColor(3381555);
			}
			else
			{
				((GuiExtendedButton) this.buttonList.get(i)).displayString = ("Add item to " + this.compatibleStats[i].getLocalizedName() + " list.");
				((GuiExtendedButton) this.buttonList.get(i)).setBackgroundColor(10040115);
			}
		}
	}

	@Override
	public void onGuiClosed()
	{
		Reference.configuration.save();
	}
}