package net.infstudio.goki.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class GuiExtendedButton extends Button {
    public static final int BORDER_COLOR = -16777216;

    private int backgroundColor;
    private boolean pressed = false;
    public boolean disabled = false;
    public final int id;

    public GuiExtendedButton(int id, int x, int y, int width, int height, ITextComponent text, int color, IPressable onPress) {
        super(x, y, width, height, text, onPress);
        this.id = id;
        this.backgroundColor = color;
    }


    @Override
    public void renderButton(@Nonnull MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (!this.disabled) {
            if (!isUnderMouse(mouseX, mouseY)) {
                drawIdle(stack, Minecraft.getInstance(), mouseX, mouseY);
            } else if (this.pressed) {
                drawDown(stack, Minecraft.getInstance(), mouseX, mouseY);
            } else {
                drawHover(stack, Minecraft.getInstance(), mouseX, mouseY);
            }
        } else
            drawDisabled(stack, Minecraft.getInstance(), mouseX, mouseY);
    }

    public boolean isUnderMouse(int mouseX, int mouseY) {
        return (mouseX >= this.x) && (mouseY >= this.y) && (mouseX < this.x + this.width) && (mouseY < this.y + this.height);
    }

    private void drawBorder(MatrixStack stack) {
        hLine(stack, -1, this.width, 0, BORDER_COLOR);
        hLine(stack, -1, this.width, this.height, BORDER_COLOR);
        vLine(stack, -1, 0, this.height, BORDER_COLOR);
        vLine(stack, this.width, 0, this.height, BORDER_COLOR);
    }

    private void drawDisabled(MatrixStack stack, Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(stack, 0, 0, this.width, this.height, -2011028958);
        drawCenteredString(stack, mc.font,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.font.lineHeight / 2 + 1,
                3355443);
        drawBorder(stack);
        GL11.glPopMatrix();
    }

    private void drawIdle(MatrixStack stack, Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(stack, 0,
                0,
                this.width,
                this.height,
                this.backgroundColor + -2013265920);
        drawCenteredString(stack, mc.font,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.font.lineHeight / 2 + 1,
                16777215);
        drawBorder(stack);
        GL11.glPopMatrix();
    }

    private void drawHover(MatrixStack stack, Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(stack, 0,
                0,
                this.width,
                this.height,
                this.backgroundColor + -16777216);
        drawCenteredString(stack, mc.font,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.font.lineHeight / 2 + 1,
                16763904);
        drawBorder(stack);
        GL11.glPopMatrix();
    }

    private void drawDown(MatrixStack stack, Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(this.x, this.y, 0.0F);
        fill(stack, 0, 0, this.width, this.height, -16777216);
        drawCenteredString(stack, mc.font,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.font.lineHeight / 2 + 1,
                16763904);
        drawBorder(stack);
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
