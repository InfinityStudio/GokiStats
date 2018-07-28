package net.infstudio.goki.client.gui;

import net.infstudio.goki.common.stats.StatBase;
import net.infstudio.goki.common.stats.Stats;
import net.infstudio.goki.common.stats.tool.ToolSpecificStat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class GuiCompatibilityHelper extends GuiScreen {
    private final int BUTTON_WIDTH = 240;
    private final int BUTTON_HEIGHT = 15;
    private final ToolSpecificStat[] compatibleStats =
            {Stats.MINING, Stats.DIGGING, Stats.CHOPPING, Stats.TRIMMING, Stats.SWORDSMANSHIP, Stats.BOWMANSHIP};
    private EntityPlayer player;

    public GuiCompatibilityHelper(EntityPlayer player) {
        this.player = player;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
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
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        drawCenteredString(this.mc.fontRenderer,
                "This is CLIENT-SIDE. Copy the config to the server and /reloadGokiStats.",
                this.width / 2,
                16,
                16777215);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (this.compatibleStats[button.id].needAffectedByStat(this.player.getHeldItemMainhand())) {
            this.compatibleStats[button.id].removeSupportForItem(this.player.getHeldItemMainhand());
        } else {
            this.compatibleStats[button.id].addSupportForItem(this.player.getHeldItemMainhand());
        }
        StatBase.stats.forEach(StatBase::saveConfig);
        checkStatus();
    }

    public void checkStatus() {
        StatBase.stats.forEach(StatBase::reloadConfig);
        for (int i = 0; i < this.compatibleStats.length; i++) {
            if (this.compatibleStats[i].needAffectedByStat(this.player.getHeldItemMainhand())) {
                ((GuiExtendedButton) this.buttonList.get(i)).displayString = ("Remove item from " + this.compatibleStats[i].getLocalizedName() + " list.");
                ((GuiExtendedButton) this.buttonList.get(i)).setBackgroundColor(3381555);
            } else {
                ((GuiExtendedButton) this.buttonList.get(i)).displayString = ("Add item to " + this.compatibleStats[i].getLocalizedName() + " list.");
                ((GuiExtendedButton) this.buttonList.get(i)).setBackgroundColor(10040115);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        StatBase.stats.forEach(StatBase::saveConfig);
    }
}