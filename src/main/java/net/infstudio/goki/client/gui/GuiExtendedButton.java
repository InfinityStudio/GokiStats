package net.infstudio.goki.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;

public class GuiExtendedButton extends Button {
    public static final int BORDER_COLOR = -16777216;

    private int backgroundColor;
    private boolean pressed = false;
    public boolean disabled = false;
    public final int id;

    public GuiExtendedButton(int id, int x, int y, int width, int height, TextComponent text, int color, OnPress onPress) {
        super(x, y, width, height, text, onPress);
        this.id = id;
        this.backgroundColor = color;
    }


    @Override
    public void renderButton(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
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

    private void drawBorder(PoseStack stack) {
        hLine(stack, -1, this.width, 0, BORDER_COLOR);
        hLine(stack, -1, this.width, this.height, BORDER_COLOR);
        vLine(stack, -1, 0, this.height, BORDER_COLOR);
        vLine(stack, this.width, 0, this.height, BORDER_COLOR);
    }

    private void drawDisabled(PoseStack stack, Minecraft mc, int mouseX, int mouseY) {
        stack.pushPose();
        stack.translate(this.x, this.y, 0.0F);
        fill(stack, 0, 0, this.width, this.height, -2011028958);
        drawCenteredString(stack, mc.font,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.font.lineHeight / 2 + 1,
                3355443);
        drawBorder(stack);
        stack.popPose();
    }

    private void drawIdle(PoseStack stack, Minecraft mc, int mouseX, int mouseY) {
        stack.pushPose();
        stack.translate(this.x, this.y, 0.0F);
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
        stack.popPose();
    }

    private void drawHover(PoseStack stack, Minecraft mc, int mouseX, int mouseY) {
        stack.pushPose();
        stack.translate(this.x, this.y, 0.0F);
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
        stack.popPose();
    }

    private void drawDown(PoseStack stack, Minecraft mc, int mouseX, int mouseY) {
        stack.pushPose();
        stack.translate(this.x, this.y, 0.0F);
        fill(stack, 0, 0, this.width, this.height, -16777216);
        drawCenteredString(stack, mc.font,
                this.getMessage(),
                this.width / 2,
                this.height / 2 - mc.font.lineHeight / 2 + 1,
                16763904);
        drawBorder(stack);
        stack.popPose();
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
