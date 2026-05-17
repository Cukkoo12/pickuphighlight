package com.cukkoo.pickuphighlight;

import com.cukkoo.pickuphighlight.config.PickupHighlightConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
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

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    private static class ClientTickHandler {
        @SubscribeEvent
        static void onClientTick(ClientTickEvent.Post event) {
            HighlightTracker.onClientTick(net.minecraft.client.Minecraft.getInstance());
        }
    }
}
