package net.infstudio.goki.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import org.lwjgl.opengl.GL11;

public class GuiExtendedButton extends Button {
    public static final int BORDER_COLOR = -16777216;

    private int backgroundColor;
    private boolean pressed = false;
    public boolean disabled = false;
    public final int id;

    public GuiExtendedButton(int id, int x, int y, int width, int height, String text, int color, IPressable onPress) {
        super(x, y, width, height, text, onPress);
        this.id = id;
        this.backgroundColor = color;
    }


    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        if (!this.disabled) {
            if (!isUnderMouse(mouseX, mouseY)) {
                drawIdle(Minecraft.getInstance(), mouseX, mouseY);
            } else if (this.pressed) {
                drawDown(Minecraft.getInstance(), mouseX, mouseY);
            } else {
                drawHover(Minecraft.getInstance(), mouseX, mouseY);
            }
        } else
            drawDisabled(Minecraft.getInstance(), mouseX, mouseY);
    }

    public boolean isUnderMouse(int mouseX, int mouseY) {
        return (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width) && (mouseY < this.y + this.height);
    }

    private void drawBorder() {
        hLine(-1, this.width, 0, BORDER_COLOR);
        hLine(-1, this.width, this.height, BORDER_COLOR);
        vLine(-1, 0, this.height, BORDER_COLOR);
        vLine(this.width, 0, this.height, BORDER_COLOR);
    }

    private void drawDisabled(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(0, 0, this.width, this.height, -2011028958);
        drawCenteredString(mc.fontRenderer,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                3355443);
        drawBorder();
        GL11.glPopMatrix();
    }

    private void drawIdle(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(0,
                0,
                this.width,
                this.height,
                this.backgroundColor + -2013265920);
        drawCenteredString(mc.fontRenderer,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                16777215);
        drawBorder();
        GL11.glPopMatrix();
    }

    private void drawHover(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(0,
                0,
                this.width,
                this.height,
                this.backgroundColor + -16777216);
        drawCenteredString(mc.fontRenderer,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                16763904);
        drawBorder();
        GL11.glPopMatrix();
    }

    private void drawDown(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(0, 0, this.width, this.height, -16777216);
        drawCenteredString(mc.fontRenderer,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.fontRenderer.FONT_HEIGHT / 2 + 1,
                16763904);
        drawBorder();
        GL11.glPopMatrix();
    }

    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible) {
            if (keyCode != 257 && keyCode != 32 && keyCode != 335) {
                return false;
            } else {
                this.onRelease();
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onPress() {
        super.onPress();
        this.pressed = true;
    }

    public void onRelease() {
        this.pressed = false;
    }

    public boolean isPressed() {
        return this.pressed;
    }

}
