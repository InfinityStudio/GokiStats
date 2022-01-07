package net.infstudio.goki.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.infstudio.goki.api.stat.Stats;
import net.infstudio.goki.common.stat.tool.ToolSpecificStat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nonnull;

public class GuiCompatibilityHelper extends Screen {
    private static final int BUTTON_WIDTH = 240;
    private static final int BUTTON_HEIGHT = 15;
    private final ToolSpecificStat[] compatibleStats =
            {Stats.MINING, Stats.DIGGING, Stats.CHOPPING, Stats.TRIMMING, Stats.SWORDSMANSHIP, Stats.BOWMANSHIP};
    private final Player player = minecraft.player;

    public GuiCompatibilityHelper() {
        super(new TextComponent(""));
        minecraft = Minecraft.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        int x = this.width / 2;
        int y = this.height / 2 - 45;
        addRenderableWidget(new GuiExtendedButton(0, x - 120, y - 15, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent("Add item to Mining list"), 3355443, this::actionPerformed));
        addRenderableWidget(new GuiExtendedButton(1, x - 120, y - 15 + 15, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent("Add item to Digging list"), 3355443, this::actionPerformed));
        addRenderableWidget(new GuiExtendedButton(2, x - 120, y - 15 + 15 * 2, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent("Add item to Chopping list"), 3355443, this::actionPerformed));
        addRenderableWidget(new GuiExtendedButton(3, x - 120, y - 15 + 15 * 3, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent("Add item to Trimming list"), 3355443, this::actionPerformed));
        addRenderableWidget(new GuiExtendedButton(4, x - 120, y - 15 + 15 * 4, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent("Add item to Swordsmanship list"), 3355443, this::actionPerformed));
        addRenderableWidget(new GuiExtendedButton(5, x - 120, y - 15 + 15 * 5, BUTTON_WIDTH, BUTTON_HEIGHT, new TextComponent("Add item to Bowmanship list"), 3355443, this::actionPerformed));
        checkStatus();
    }

    @Override
    public void render(@Nonnull PoseStack matrixStack, int par1, int par2, float par3) {
        super.render(matrixStack, par1, par2, par3);
        drawCenteredString(matrixStack, minecraft.font,
                "This is CLIENT-SIDE. Copy the config to the server and /reloadGokiStats.",
                this.width / 2,
                16,
                16777215);
    }

    protected void actionPerformed(Button btn) {
        if (!(btn instanceof GuiExtendedButton)) return;
        GuiExtendedButton button = (GuiExtendedButton) btn;
        if (this.compatibleStats[button.id].isEffectiveOn(this.player.getMainHandItem())) {
            this.compatibleStats[button.id].removeSupportForItem(this.player.getMainHandItem());
        } else {
            this.compatibleStats[button.id].addSupportForItem(this.player.getMainHandItem());
        }
//        StatBase.stats.forEach(StatBase::saveConfig);
        checkStatus();
    }

    public void checkStatus() {
//        StatBase.stats.forEach(StatBase::reloadConfig);
        for (int i = 0; i < this.compatibleStats.length; i++) {
            if (renderables.get(i) instanceof GuiExtendedButton button) {
                if (this.compatibleStats[i].isEffectiveOn(this.player.getMainHandItem())) {
                    button.setMessage(new TextComponent("Remove item from " + this.compatibleStats[i].getLocalizedName() + " list."));
                    button.setBackgroundColor(3381555);
                } else {
                    button.setMessage(new TextComponent("Add item to " + this.compatibleStats[i].getLocalizedName() + " list."));
                    button.setBackgroundColor(10040115);
                }
            }
        }
    }

    @Override
    public void onClose() {
//        StatBase.stats.forEach(StatBase::saveConfig);
    }
}
