package net.infstudio.goki.client.gui;

import net.infstudio.goki.common.config.GokiConfig;
import net.infstudio.goki.client.GokiKeyHandler;
import net.infstudio.goki.common.network.GokiPacketHandler;
import net.infstudio.goki.common.network.message.C2SRequestStatSync;
import net.infstudio.goki.common.network.message.C2SStatSync;
import net.infstudio.goki.api.stat.StatBase;
import net.infstudio.goki.common.utils.DataHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;

public class GuiStats extends GuiScreen {
    public static final int STATUS_BUTTON_WIDTH = 24;
    public static final int STATUS_BUTTON_HEIGHT = 24;
    // private static final int HORIZONTAL_SPACING = 8;
    // private static final int VERTICAL_SPACING = 12;
    public static final int IMAGE_ROWS = 10;
    private static final int[] COLUMNS =
            {4, 4, 5, 4, 5, 4};
    public static float SCALE = 1.0F;

    private EntityPlayer player;
    private int currentColumn = 0;
    private int currentRow = 0;
    private GuiStatTooltip toolTip = null;
    private FontRenderer fontRenderer;

    public GuiStats(EntityPlayer player) {
        GokiPacketHandler.CHANNEL.sendToServer(new C2SRequestStatSync());
        this.player = player;
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        int ttx = 0;
        int tty = 0;
        this.toolTip = null;
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, par3);
        for (int i = 0; i < this.buttonList.size(); i++) {
            if ((this.buttonList.get(i) instanceof GuiStatButton)) {
                GuiStatButton button = (GuiStatButton) this.buttonList.get(i);
                if (button.isUnderMouse(mouseX, mouseY)) {
                    this.toolTip = new GuiStatTooltip(StatBase.stats.get(i), this.player);
                    ttx = button.x + 12;
                    tty = button.y - 1;
                    break;
                }
            }
        }
        drawCenteredString(fontRenderer,
                I18n.format("ui.currentxp.name") + DataHelper.getXPTotal(player.experienceLevel,
                        player.experience) + "xp",
                width / 2,
                this.height - 16,
                0xFFFFFFFF);

        if (this.toolTip != null)
            this.toolTip.draw(ttx, tty, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        for (int stat = 0; stat < StatBase.totalStats; stat++) {
            Vector2f pos = getButton(stat);
            this.buttonList.add(new GuiStatButton(stat, (int) pos.x, (int) pos.y, 24, 24, StatBase.stats.get(stat), this.player));
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

    private Vector2f getButton(int n) {
        Vector2f vec = new Vector2f();
        int columns = COLUMNS[this.currentRow];
        int x = n % columns;
        int y = this.currentRow;
        int rows = COLUMNS.length;
        float width = columns * 32 * SCALE;
        float height = rows * 36 * SCALE;
        vec.x = (width / columns * x + (this.width - width + 8.0F) / 2.0F);
        vec.y = (height / rows * y + (this.height - height + 12.0F) / 2.0F);
        return vec;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if ((button.id >= 0) && (button.id <= StatBase.totalStats)) {
            if ((button instanceof GuiStatButton)) {
                GuiStatButton statButton = (GuiStatButton) button;
                if (!GuiScreen.isCtrlKeyDown())
                    GokiPacketHandler.CHANNEL.sendToServer(new C2SStatSync(StatBase.stats.indexOf(statButton.stat), 1));
                else // Downgrade
                    GokiPacketHandler.CHANNEL.sendToServer(new C2SStatSync(StatBase.stats.indexOf(statButton.stat), -1));
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        // 1 is the Esc key, and we made our keybinding array public and static
        // so we can access it here
        if (c == 1 || (GokiConfig.keyBindingEnabled && keyCode == GokiKeyHandler.statsMenu.getKeyCode())) {
            mc.player.closeScreen();
        }
    }
}
