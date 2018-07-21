package net.infstudio.goki.client.gui;

import net.infstudio.goki.lib.DataHelper;
import net.infstudio.goki.stats.StatBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiStatTooltip extends Gui {
    private StatBase stat;
    private EntityPlayer player;
    private Minecraft mc = Minecraft.getMinecraft();
    private int padding = 4;

    public GuiStatTooltip(StatBase stat, EntityPlayer player) {
        this.stat = stat;
        this.player = player;
    }

    public void draw(int drawX, int drawY, int mouseButton) {
        Map<String, Integer> messageColorMap = new LinkedHashMap<>();

        AtomicInteger widthAtomic = new AtomicInteger(), heightAtomic = new AtomicInteger();

        int level = DataHelper.getPlayerStatLevel(this.player, this.stat);

        messageColorMap.put(this.stat.getLocalizedName() + " L" + level, -13312); // Header
        messageColorMap.put(this.stat.getLocalizedDes(this.player), -1); // Message
        if (level >= this.stat.getLimit())
            messageColorMap.put(I18n.translateToLocal("ui.max.name"), -16724737);
        else
            messageColorMap.put(I18n.translateToLocal("ui.cost.name") + this.stat.getCost(level) + "xp", -16724737); // Cost
        messageColorMap.put(I18n.translateToLocal("ui.hover.name"), 0xffffffff-0x98fb9800);

        messageColorMap.forEach((text, color) -> {
            widthAtomic.set(Math.max(widthAtomic.get(), this.mc.fontRenderer.getStringWidth(text)));
            heightAtomic.addAndGet(this.mc.fontRenderer.FONT_HEIGHT);
        });

        int width = widthAtomic.get() + this.padding * 2;
        int height = heightAtomic.get() + this.padding * 2;
        int h = height / messageColorMap.size();
        int x = drawX - width / 2;
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
        drawRect(x, drawY, x + width, drawY - height, -872415232);

        for (int i = messageColorMap.size(); i >= 1; i--) {
            Map.Entry<String, Integer> entry = messageColorMap.entrySet().toArray(new Map.Entry[0])[messageColorMap.size() - i];
            drawString(this.mc.fontRenderer,
                    entry.getKey(),
                    x + this.padding / 2,
                    drawY - h * i + this.padding / 2,
                    entry.getValue());
        }

        drawBorder(x, drawY, width, height, -1);
    }

    private void drawBorder(int x, int y, int width, int height, int borderColor) {
        drawHorizontalLine(x - 1, x + width, y, borderColor);
        drawHorizontalLine(x - 1, x + width, y - height, borderColor);
        drawVerticalLine(x - 1, y, y - height, borderColor);
        drawVerticalLine(x + width, y, y - height, borderColor);
    }
}