package com.cukkoo.pickuphighlight.mixin;

import com.cukkoo.pickuphighlight.HighlightTracker;
import com.cukkoo.pickuphighlight.PickupHighlight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Shadow
    protected int leftPos;

    @Shadow
    protected int topPos;

    @Inject(method = "extractContents", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractSlotHighlightFront(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
            shift = At.Shift.AFTER))
    private void afterSlotRender(GuiGraphicsExtractor extractor, int mouseX, int mouseY,
                                 float delta, CallbackInfo ci) {
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        for (Slot slot : screen.getMenu().slots) {
            if (!(slot.container instanceof Inventory)) continue;
            int slotIndex = slot.getContainerSlot();
            if (!HighlightTracker.isHighlighted(slotIndex)) continue;

            // Clear on hover - mouse is screen-absolute, slot is GUI-relative
            if (PickupHighlight.config.clearOnHover
                    && mouseX >= leftPos + slot.x && mouseX < leftPos + slot.x + 16
                    && mouseY >= topPos + slot.y && mouseY < topPos + slot.y + 16) {
                HighlightTracker.clearSlot(slotIndex);
                continue;
            }

            drawStar(extractor, slot.x, slot.y, slotIndex);
        }
    }

    private static void drawStar(GuiGraphicsExtractor extractor, int slotX, int slotY,
                                  int slotIndex) {
        Minecraft client = Minecraft.getInstance();
        float scale = HighlightTracker.getPulseScale(slotIndex);
        int color = 0xFF000000 | PickupHighlight.config.highlightColor;

        // Pose is already translated by leftPos/topPos at this point, use slot-relative coords
        Matrix3x2fStack pose = extractor.pose();
        pose.pushMatrix();
        pose.translate(slotX + 11, slotY + 1);
        pose.scale(scale, scale);
        extractor.text(client.font, "✦", 0, 0, color);
        pose.popMatrix();

        // Draw count badge to the left of the star
        if (PickupHighlight.config.showCount) {
            int count = HighlightTracker.getCount(slotIndex);
            if (count > 0) {
                String text = "+" + count;
                int textWidth = client.font.width(text);
                pose.pushMatrix();
                pose.translate(slotX + 9 - textWidth * 0.5f, slotY + 3);
                pose.scale(0.5f, 0.5f);
                extractor.text(client.font, text, 0, 0, 0xFFFFFFFF);
                pose.popMatrix();
            }
        }
    }
}
