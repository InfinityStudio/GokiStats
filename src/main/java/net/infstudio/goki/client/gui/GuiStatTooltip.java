package net.infstudio.goki.client.gui;

import net.infstudio.goki.lib.DataHelper;
import net.infstudio.goki.stats.Stat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

public class GuiStatTooltip extends Gui {
    private Stat stat;
    private EntityPlayer player;
    private Minecraft mc = Minecraft.getMinecraft();
    private int padding = 4;

    public GuiStatTooltip(Stat stat, EntityPlayer player) {
        this.stat = stat;
        this.player = player;
    }

    public void draw(int drawX, int drawY, int mouseButton) {
        int level = DataHelper.getPlayerStatLevel(this.player, this.stat);
        String header = (this.stat.getLocalizedName()) + " L" + level;
        String message = this.stat.getLocalizedDes(this.player);
        String cost = I18n.translateToLocal("ui.cost.name") + this.stat.getCost(level) + "xp";
        if (level >= this.stat.getLimit())
            cost = I18n.translateToLocal("ui.max.name");
        int width = Math.max(this.mc.fontRenderer.getStringWidth(message),
                this.mc.fontRenderer.getStringWidth(header)) + this.padding * 2;
        int height = this.mc.fontRenderer.FONT_HEIGHT * 3 + this.padding * 2;
        int h = height / 3;
        int x = drawX - width / 2;
        int y = drawY;
        int leftEdge = 0;
        int left = x;
        if (left < leftEdge) {
            x -= leftEdge - left + 1;
        }
        int rightEdge = this.mc.currentScreen.width;
        int right = x + width;
        if (right > rightEdge) {
            x += rightEdge - right - 1;
        }
        drawRect(x, y, x + width, y - height, -872415232);
        drawString(this.mc.fontRenderer,
                header,
                x + this.padding / 2,
                y - h * 3 + this.padding / 2,
                -13312);
        drawString(this.mc.fontRenderer,
                message,
                x + this.padding / 2,
                y - h * 2 + this.padding / 2,
                -1);
        drawString(this.mc.fontRenderer,
                cost,
                x + this.padding / 2,
                y - h + this.padding / 2,
                -16724737);
        drawBorder(x, y, width, height, -1);
    }

    private void drawBorder(int x, int y, int width, int height, int borderColor) {
        drawHorizontalLine(x - 1, x + width, y, borderColor);
        drawHorizontalLine(x - 1, x + width, y - height, borderColor);
        drawVerticalLine(x - 1, y, y - height, borderColor);
        drawVerticalLine(x + width, y, y - height, borderColor);
    }
}