package net.infstudio.goki.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class GuiStatButton extends Button {
    public StatBase stat;
    public Player player;

    public static final int INACTIVE_X = 0;
    public static final int ACTIVATED_X = 24;
    public static final int DISABLED_X = 48;
    public static final int MAXIMUM_X = 72;

    public final int id;
    private final Minecraft mc = Minecraft.getInstance();


    public GuiStatButton(int id, int x, int y, int width, int height, StatBase stat, Player player, OnPress onPress) {
        super(x, y, width, height, TextComponent.EMPTY, onPress);
        this.id = id;
        this.stat = stat;
        this.player = player;
    }

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            int iconY = 24 * (this.stat.imageID % 10);
            int level = DataHelper.getPlayerStatLevel(this.player, this.stat);
            int cost = this.stat.getCost(level);
            int playerXP = DataHelper.getXPTotal(this.player);

            var fontrenderer = mc.font;
            this.isHovered = isUnderMouse(mouseX, mouseY);

            int iconX = INACTIVE_X;
            if (isHovered) { // Hovering
                iconX = ACTIVATED_X;
            }
            if (playerXP < cost) {
                iconX = INACTIVE_X;
            }
            if (level >= this.stat.getLimit()) {
                iconX = MAXIMUM_X;
            }
            if (Screen.hasControlDown()) {
                if (DataHelper.canPlayerRevertStat(player, this.stat))
                    iconX = ACTIVATED_X;
                else
                    iconX = DISABLED_X;
            }
            if (!stat.isEnabled())
                iconX = DISABLED_X;

            String message = level + "";
            if (!this.stat.isEnabled()) {
                iconX = 48;
                message = "X";
            }

            int messageColor = 16777215;
            if (level >= this.stat.getLimit()) {
                message = "*" + level + "*";
                messageColor = 16763904;
            }

            iconX += this.stat.imageID % 20 / 10 * 24 * 4;


            stack.pushPose();
            stack.translate(this.x, this.y, 0);
            stack.scale(GuiStats.SCALE, GuiStats.SCALE, 0);
            bindTexture();
            blit(stack, 0, 0, iconX, iconY, this.width, this.height);

            stack.popPose();
            stack.pushPose();
            stack.translate(this.x, this.y, 0);
            drawCenteredString(stack, fontrenderer,
                    message,
                    (int) (this.width / 2 * GuiStats.SCALE),
                    (int) (this.height * GuiStats.SCALE) + 2,
                    messageColor);
            stack.popPose();
        }
    }

    public void bindTexture() {
        if (this.stat.imageID >= 20) {
            RenderSystem.setShaderTexture(0, Reference.RPG_ICON_2_TEXTURE_LOCATION);
        } else {
            RenderSystem.setShaderTexture(0, Reference.RPG_ICON_TEXTURE_LOCATION);
        }
    }

    public boolean isUnderMouse(int mouseX, int mouseY) {
        return (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width * GuiStats.SCALE) && (mouseY < this.y + this.height * GuiStats.SCALE);
    }

    @Override
    public void onClick(double p_onClick_1_, double p_onClick_3_) {
        super.onClick(p_onClick_1_, p_onClick_3_);
    }

    //    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
        return (this.active) && (this.visible) && (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width * GuiStats.SCALE) && (mouseY < this.y + this.height * GuiStats.SCALE);
    }

    public String getHoverMessage(int which) {
        if (which == 0) {
            return this.stat.getLocalizedName() + " L" + DataHelper.getPlayerStatLevel(this.player,
                    this.stat);
        }

        return this.stat.getLocalizedDescription(this.player);
    }
}
