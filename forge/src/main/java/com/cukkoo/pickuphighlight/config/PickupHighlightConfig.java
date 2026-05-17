package com.cukkoo.pickuphighlight.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PickupHighlightConfig {

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("pickuphighlight.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean clearOnHover = true;
    public boolean clearOnClose = true;
    public boolean clearOnSelect = true;
    public int highlightColor = 0xFFD700;
    public int timeoutSeconds = 0;
    public boolean showCount = true;

    public static PickupHighlightConfig load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                return GSON.fromJson(Files.readString(CONFIG_PATH), PickupHighlightConfig.class);
            }
        } catch (IOException e) {
            // use defaults
        }
        return new PickupHighlightConfig();
    }

    public void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            // ignore
        }
    }
}
