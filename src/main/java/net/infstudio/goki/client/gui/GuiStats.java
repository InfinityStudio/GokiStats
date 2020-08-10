package net.infstudio.goki.client.gui;

import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.C2SRequestStatSync;
import net.infstudio.goki.common.network.message.C2SStatSync;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.text.StringTextComponent;

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

    private final PlayerEntity player = Minecraft.getInstance().player;
    private final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    private int currentColumn = 0;
    private int currentRow = 0;

    /**
     * Tooltip for the skill under mouse hover
     */
    private GuiStatTooltip toolTip = null;


    public GuiStats() {
        super(new StringTextComponent(""));
    }

    /**
     * Should the screen pauses Singleplayer game
     */
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float par3) {
        int toolTipX = 0;
        int toolTipY = 0;
        this.toolTip = null;
        renderBackground();
        super.render(mouseX, mouseY, par3);
        for (int i = 0; i < this.buttons.size(); i++) {
            if ((this.buttons.get(i) instanceof GuiStatButton)) {
                GuiStatButton button = (GuiStatButton) this.buttons.get(i);
                if (button.isUnderMouse(mouseX, mouseY)) {
                    this.toolTip = new GuiStatTooltip(StatBase.stats.get(i), this.player);
                    toolTipX = button.x + 12;
                    toolTipY = button.y - 1;
                    break;
                }
            }
        }
        drawCenteredString(fontRenderer,
                I18n.format("ui.currentxp", DataHelper.getXPTotal(player)),
                width / 2,
                this.height - 16,
                0xFFFFFFFF);

        if (this.toolTip != null)
            this.toolTip.draw(toolTipX, toolTipY, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        // Sync the stat
        GokiPacketHandler.CHANNEL.sendToServer(new C2SRequestStatSync());
        for (int stat = 0; stat < StatBase.totalStats.orElse(0); stat++) {
            Vec2f pos = getButton(stat);
            this.buttons.add(new GuiStatButton(stat, (int) pos.x, (int) pos.y, 24, 24, StatBase.stats.get(stat), this.player, this::actionPerformed));
            this.currentColumn += 1;
            if (this.currentColumn >= COLUMNS[this.currentRow]) {
                this.currentRow += 1;
                this.currentColumn = 0;
            }
            if (this.currentRow >= COLUMNS.length) {
                this.currentRow = (COLUMNS.length - 1);
            }
        }
        children.addAll(buttons);
    }

    private Vec2f getButton(int n) {
        int columns = COLUMNS[this.currentRow];
        int x = n % columns;
        int y = this.currentRow;
        int rows = COLUMNS.length;
        float width = columns * 32 * SCALE;
        float height = rows * 36 * SCALE;
        return new Vec2f((width / columns * x + (this.width - width + 8.0F) / 2.0F),
                (height / rows * y + (this.height - height + 12.0F) / 2.0F));
    }

    protected void actionPerformed(Button btn) {
        if (!(btn instanceof GuiStatButton))
            return;

        GuiStatButton button = (GuiStatButton) btn;
        if ((button.id >= 0) && (button.id <= StatBase.totalStats.orElse(0))) {
            GuiStatButton statButton = (GuiStatButton) button;
            if (!hasControlDown())
                GokiPacketHandler.CHANNEL.sendToServer(new C2SStatSync(StatBase.stats.indexOf(statButton.stat), 1));
            else // Downgrade
                GokiPacketHandler.CHANNEL.sendToServer(new C2SStatSync(StatBase.stats.indexOf(statButton.stat), -1));
        }
    }

}
