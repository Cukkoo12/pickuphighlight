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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
            at = @At("RETURN"))
    private void afterExtractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY,
                                         float delta, CallbackInfo ci) {
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        for (Slot slot : screen.getMenu().slots) {
            if (!(slot.container instanceof Inventory)) continue;
            int slotIndex = slot.getContainerSlot();
            if (!HighlightTracker.isHighlighted(slotIndex)) continue;

            // Clear on hover
            if (PickupHighlight.config.clearOnHover
                    && mouseX >= slot.x && mouseX < slot.x + 16
                    && mouseY >= slot.y && mouseY < slot.y + 16) {
                HighlightTracker.clearSlot(slotIndex);
                continue;
            }

            drawStar(extractor, slot.x, slot.y, slotIndex);
        }
    }

    private static void drawStar(GuiGraphicsExtractor extractor, int slotX, int slotY,
                                  int slotIndex) {
        float scale = HighlightTracker.getPulseScale(slotIndex);
        int color = 0xFF000000 | PickupHighlight.config.highlightColor;

        Matrix3x2fStack pose = extractor.pose();
        pose.pushMatrix();
        pose.translate(slotX + 11, slotY + 1);
        pose.scale(scale, scale);
        extractor.text(Minecraft.getInstance().font, "✦", 0, 0, color);
        pose.popMatrix();
    }
}
