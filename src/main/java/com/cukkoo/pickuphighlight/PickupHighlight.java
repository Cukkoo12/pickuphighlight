package com.cukkoo.pickuphighlight;

import com.cukkoo.pickuphighlight.config.PickupHighlightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PickupHighlight implements ClientModInitializer {

    public static final String MOD_ID = "pickuphighlight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static PickupHighlightConfig config;

    @Override
    public void onInitializeClient() {
        config = PickupHighlightConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(HighlightTracker::onClientTick);
        LOGGER.info("PickupHighlight initialized");
    }
}
