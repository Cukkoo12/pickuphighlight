package com.cukkoo.pickuphighlight.integration;

import com.cukkoo.pickuphighlight.PickupHighlight;
import com.cukkoo.pickuphighlight.config.PickupHighlightConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PickupHighlightConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("pickuphighlight.config.title"));

        ConfigCategory general = builder.getOrCreateCategory(
                Component.translatable("pickuphighlight.config.category.general"));
        ConfigEntryBuilder eb = builder.entryBuilder();

        PickupHighlightConfig cfg = PickupHighlight.config;

        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("pickuphighlight.config.clearOnHover"),
                        cfg.clearOnHover)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("pickuphighlight.config.clearOnHover.tooltip"))
                .setSaveConsumer(val -> cfg.clearOnHover = val)
                .build());

        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("pickuphighlight.config.clearOnClose"),
                        cfg.clearOnClose)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("pickuphighlight.config.clearOnClose.tooltip"))
                .setSaveConsumer(val -> cfg.clearOnClose = val)
                .build());

        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("pickuphighlight.config.clearOnSelect"),
                        cfg.clearOnSelect)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("pickuphighlight.config.clearOnSelect.tooltip"))
                .setSaveConsumer(val -> cfg.clearOnSelect = val)
                .build());

        general.addEntry(eb.startIntField(
                        Component.translatable("pickuphighlight.config.highlightColor"),
                        cfg.highlightColor)
                .setDefaultValue(0xFFD700)
                .setMin(0)
                .setMax(0xFFFFFF)
                .setTooltip(Component.translatable("pickuphighlight.config.highlightColor.tooltip"))
                .setSaveConsumer(val -> cfg.highlightColor = val)
                .build());

        general.addEntry(eb.startIntField(
                        Component.translatable("pickuphighlight.config.timeoutSeconds"),
                        cfg.timeoutSeconds)
                .setDefaultValue(0)
                .setMin(0)
                .setMax(3600)
                .setTooltip(Component.translatable("pickuphighlight.config.timeoutSeconds.tooltip"))
                .setSaveConsumer(val -> cfg.timeoutSeconds = val)
                .build());

        builder.setSavingRunnable(cfg::save);

        return builder.build();
    }
}
