package net.infstudio.goki.client.gui;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiStatTooltip extends AbstractGui {
    private StatBase stat;
    private PlayerEntity player;
    private int padding = 4;

    private final Minecraft mc = Minecraft.getInstance();

    public GuiStatTooltip(StatBase stat, PlayerEntity player) {
        this.stat = stat;
        this.player = player;
    }

    public void draw(int drawX, int drawY, int mouseButton) {
        Map<String, Integer> messageColorMap = new LinkedHashMap<>();

        AtomicInteger widthAtomic = new AtomicInteger(), heightAtomic = new AtomicInteger();

        int level = DataHelper.getPlayerStatLevel(this.player, this.stat);

        messageColorMap.put(this.stat.getLocalizedName() + " L" + level, -13312); // Header
        messageColorMap.put(this.stat.getLocalizedDescription(this.player), -1); // Message
        if (level >= this.stat.getLimit())
            messageColorMap.put(I18n.format("ui.max"), -16724737);
        else
            messageColorMap.put(I18n.format("ui.cost") + this.stat.getCost(level) + "xp", -16724737); // Cost

        if (Screen.hasControlDown())
            messageColorMap.put(I18n.format("ui.return") + this.stat.getCost(level) * GokiConfig.SERVER.globalRevertFactor.get() + "xp", -16724737); // Cost

        int revertLevel = DataHelper.getPlayerRevertStatLevel(player, stat);
        if (revertLevel > 0) {
            if (GokiConfig.SERVER.globalMaxRevertLevel.get() != -1)
                messageColorMap.put(I18n.format("ui.reverted", revertLevel, String.valueOf(GokiConfig.SERVER.globalMaxRevertLevel.get())), -16724737); // Reverted
            else
                messageColorMap.put(I18n.format("ui.reverted", revertLevel, I18n.format("ui.infinite")), -16724737); // Reverted
        }

        if (Screen.hasControlDown()) { // Is player reverting?
            if (DataHelper.canPlayerRevertStat(player, stat))
                messageColorMap.put(I18n.format("ui.revert"), 0xff0467ff);
            else
                messageColorMap.put(I18n.format("ui.norevert"), 0xfffb1a00);
        } else
            messageColorMap.put(I18n.format("ui.hover"), 0xff0467ff);

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
        fill(x, drawY, x + width, drawY - height, -872415232);

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
        hLine(x - 1, x + width, y, borderColor);
        hLine(x - 1, x + width, y - height, borderColor);
        vLine(x - 1, y, y - height, borderColor);
        vLine(x + width, y, y - height, borderColor);
    }
}
