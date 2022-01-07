package net.infstudio.goki.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.C2SRequestStatSync;
import net.infstudio.goki.common.network.message.C2SStatSync;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;

import javax.annotation.Nonnull;

/**
 * Stats ui, where player upgrade/downgrade their skills
 */
public class GuiStats extends Screen {
    public static final int STATUS_BUTTON_WIDTH = 24;
    public static final int STATUS_BUTTON_HEIGHT = 24;
    // private static final int HORIZONTAL_SPACING = 8;
    // private static final int VERTICAL_SPACING = 12;
    public static final int IMAGE_ROWS = 10;
    private static final int[] COLUMNS =
            {4, 4, 5, 4, 5, 4};
    public static float SCALE = 1.0F;

    private final Player player = Minecraft.getInstance().player;
    private final Font fontRenderer = Minecraft.getInstance().font;

    private int currentColumn = 0;
    private int currentRow = 0;

    /**
     * Tooltip for the skill under mouse hover
     */
    private GuiStatTooltip toolTip = null;


    public GuiStats() {
        super(new TextComponent(""));
    }

    /**
     * Should the screen pauses Singleplayer game
     */
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float par3) {
        var toolTipX = 0;
        var toolTipY = 0;
        this.toolTip = null;
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, par3);
        for (var i = 0; i < this.renderables.size(); i++) {
            if ((this.renderables.get(i) instanceof GuiStatButton)) {
                var button = (GuiStatButton) this.renderables.get(i);
                if (button.isUnderMouse(mouseX, mouseY)) {
                    this.toolTip = new GuiStatTooltip(StatBase.stats.get(i), this.player);
                    toolTipX = button.x + 12;
                    toolTipY = button.y - 1;
                    break;
                }
            }
        }
        drawCenteredString(stack, fontRenderer,
                I18n.get("ui.currentxp", DataHelper.getXPTotal(player)),
                width / 2,
                this.height - 16,
                0xFFFFFFFF);

        if (this.toolTip != null)
            this.toolTip.render(stack, toolTipX, toolTipY, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        // Sync the stat
        GokiPacketHandler.CHANNEL.sendToServer(new C2SRequestStatSync());
        for (var stat = 0; stat < StatBase.totalStats.orElse(0); stat++) {
            var pos = getButton(stat);
            addRenderableWidget(new GuiStatButton(stat, (int) pos.x, (int) pos.y, 24, 24, StatBase.stats.get(stat), this.player, this::actionPerformed));
            this.currentColumn += 1;
            if (this.currentColumn >= COLUMNS[this.currentRow]) {
                this.currentRow += 1;
                this.currentColumn = 0;
            }
            if (this.currentRow >= COLUMNS.length) {
                this.currentRow = (COLUMNS.length - 1);
            }
        }
    }

    private Vec2 getButton(int n) {
        var columns = COLUMNS[this.currentRow];
        var x = n % columns;
        var y = this.currentRow;
        var rows = COLUMNS.length;
        var width = columns * 32 * SCALE;
        var height = rows * 36 * SCALE;
        return new Vec2((width / columns * x + (this.width - width + 8.0F) / 2.0F),
                (height / rows * y + (this.height - height + 12.0F) / 2.0F));
    }

    protected void actionPerformed(Button btn) {
        if (!(btn instanceof GuiStatButton))
            return;

        var button = (GuiStatButton) btn;
        if ((button.id >= 0) && (button.id <= StatBase.totalStats.orElse(0))) {
            var statButton = (GuiStatButton) button;
            if (!hasControlDown())
                GokiPacketHandler.CHANNEL.sendToServer(new C2SStatSync(StatBase.stats.indexOf(statButton.stat), 1));
            else // Downgrade
                GokiPacketHandler.CHANNEL.sendToServer(new C2SStatSync(StatBase.stats.indexOf(statButton.stat), -1));
        }
    }

}
