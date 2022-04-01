package net.infstudio.goki.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.infstudio.goki.common.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.opengl.GL11;

public class GuiStatButton extends Button {
    public StatBase<?> stat;
    public PlayerEntity player;

    public static final int INACTIVE_X = 0;
    public static final int ACTIVATED_X = 24;
    public static final int DISABLED_X = 48;
    public static final int MAXIMUM_X = 72;

    public final int id;
    private final Minecraft mc = Minecraft.getInstance();

    public GuiStatButton(int id, int x, int y, int width, int height, StatBase<?> stat, PlayerEntity player, IPressable onPress) {
        super(x, y, width, height, StringTextComponent.EMPTY, onPress);
        this.id = id;
        this.stat = stat;
        this.player = player;
    }

    @Override
    public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            int iconY = 24 * (this.stat.imageID % 10);
            int level = DataHelper.getPlayerStatLevel(this.player, this.stat);
            int cost = this.stat.getCost(level);
            int playerXP = DataHelper.getXPTotal(this.player);

            FontRenderer fontrenderer = mc.font;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.isHovered = isUnderMouse(mouseX, mouseY);

            int iconX = INACTIVE_X;
            if (isHovered()) { // Hovering
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

            if (this.stat.imageID >= 20) {
                mc.getTextureManager().bind(Reference.RPG_ICON_2_TEXTURE_LOCATION);
            } else {
                mc.getTextureManager().bind(Reference.RPG_ICON_TEXTURE_LOCATION);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0.0F);
            GL11.glScalef(GuiStats.SCALE, GuiStats.SCALE, 0.0F);
            blit(stack, 0, 0, iconX, iconY, this.width, this.height);

            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0.0F);
            drawCenteredString(stack, fontrenderer,
                    message,
                    (int) (this.width / 2 * GuiStats.SCALE),
                    (int) (this.height * GuiStats.SCALE) + 2,
                    messageColor);
            GL11.glPopMatrix();
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
