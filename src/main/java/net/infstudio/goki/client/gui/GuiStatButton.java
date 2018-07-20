package net.infstudio.goki.client.gui;

import net.infstudio.goki.lib.DataHelper;
import net.infstudio.goki.lib.Reference;
import net.infstudio.goki.stats.StatBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class GuiStatButton extends GuiButton {
    public StatBase stat;
    public EntityPlayer player;

    public GuiStatButton(int id, int x, int y, int width, int height, StatBase stat, EntityPlayer player) {
        super(id, x, y, width, height, "");
        this.stat = stat;
        this.player = player;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            int iconu = 0;
            int iconv = 24 * (this.stat.imageID % 10);
            int level = DataHelper.getPlayerStatLevel(this.player, this.stat);
            int cost = this.stat.getCost(level);
            int playerXP = DataHelper.getXPTotal(this.player);
            String message = level + "";
            int messageColor = 16777215;

            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = isUnderMouse(mouseX, mouseY);
            int which = getHoverState(this.hovered);

            if (which == 2) {
                iconu = 24;
            }
            if (playerXP < cost) {
                iconu = 48;
            }
            if (level >= this.stat.getLimit()) {
                iconu = 72;
            }
            if (!this.stat.enabled) {
                iconu = 48;
                message = "X";
            }
            if (level >= this.stat.getLimit()) {
                message = "*" + level + "*";
                messageColor = 16763904;
            }

            iconu += this.stat.imageID % 20 / 10 * 24 * 4;

            if (this.stat.imageID >= 20) {
                mc.getTextureManager().bindTexture(Reference.RPG_ICON_2_TEXTURE_LOCATION);
            } else {
                mc.getTextureManager().bindTexture(Reference.RPG_ICON_TEXTURE_LOCATION);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0.0F);
            GL11.glScalef(GuiStats.SCALE, GuiStats.SCALE, 0.0F);
            drawTexturedModalRect(0, 0, iconu, iconv, this.width, this.height);

            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x, this.y, 0.0F);
            drawCenteredString(fontrenderer,
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
    public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
        return (this.enabled) && (this.visible) && (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width * GuiStats.SCALE) && (mouseY < this.y + this.height * GuiStats.SCALE);
    }

    public String getHoverMessage(int which) {
        if (which == 0) {
            return this.stat.getLocalizedName() + " L" + DataHelper.getPlayerStatLevel(this.player,
                    this.stat);
        }

        return this.stat.getLocalizedDes(this.player);
    }
}