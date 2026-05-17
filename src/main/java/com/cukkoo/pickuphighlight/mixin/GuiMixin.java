package com.cukkoo.pickuphighlight.mixin;

import com.cukkoo.pickuphighlight.HighlightTracker;
import com.cukkoo.pickuphighlight.PickupHighlight;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;"
            + "Lnet/minecraft/client/DeltaTracker;)V",
            at = @At("RETURN"))
    private void afterExtractRenderState(GuiGraphicsExtractor extractor, DeltaTracker tracker,
                                         CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;

        int screenWidth = extractor.guiWidth();
        int screenHeight = extractor.guiHeight();
        int hotbarStartX = screenWidth / 2 - 91;
        int hotbarY = screenHeight - 22;
        int color = 0xFF000000 | PickupHighlight.config.highlightColor;

        for (int i = 0; i <= 8; i++) {
            if (!HighlightTracker.isHighlighted(i)) continue;

            float scale = HighlightTracker.getPulseScale(i);
            int slotX = hotbarStartX + i * 20;

            Matrix3x2fStack pose = extractor.pose();
            pose.pushMatrix();
            pose.translate(slotX + 11, hotbarY + 3);
            pose.scale(scale, scale);
            extractor.text(client.font, "✦", 0, 0, color);
            pose.popMatrix();

            // Draw count badge to the left of the star
            if (PickupHighlight.config.showCount) {
                int count = HighlightTracker.getCount(i);
                if (count > 0) {
                    String text = "+" + count;
                    int textWidth = client.font.width(text);
                    pose.pushMatrix();
                    pose.translate(slotX + 9 - textWidth * 0.5f, hotbarY + 5);
                    pose.scale(0.5f, 0.5f);
                    extractor.text(client.font, text, 0, 0, 0xFFFFFFFF);
                    pose.popMatrix();
                }
            }
        }
    }
}
