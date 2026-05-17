package com.cukkoo.pickuphighlight;

import com.cukkoo.pickuphighlight.config.PickupHighlightConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PickupHighlight.MOD_ID)
public class PickupHighlight {

    public static final String MOD_ID = "pickuphighlight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static PickupHighlightConfig config;

    public PickupHighlight() {
        config = PickupHighlightConfig.load();
        LOGGER.info("PickupHighlight initialized");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    private static class ClientTickHandler {
        @SubscribeEvent
        static void onClientTick(TickEvent.ClientTickEvent.Post event) {
            HighlightTracker.onClientTick(Minecraft.getInstance());
        }
    }
}
