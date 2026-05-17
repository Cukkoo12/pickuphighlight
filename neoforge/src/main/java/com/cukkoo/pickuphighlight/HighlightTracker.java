package com.cukkoo.pickuphighlight;

import com.cukkoo.pickuphighlight.config.PickupHighlightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HighlightTracker {

    private static final Map<Integer, Long> highlightedSlots = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> slotCounts = new ConcurrentHashMap<>();
    private static ItemStack[] previousItems;
    private static int previousSelectedSlot = -1;
    private static Screen previousScreen;
    private static boolean initialized;

    public static void onClientTick(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null) {
            reset();
            return;
        }

        Inventory inventory = player.getInventory();

        if (!initialized) {
            snapshotInventory(inventory);
            previousSelectedSlot = inventory.getSelectedSlot();
            previousScreen = client.screen;
            initialized = true;
            return;
        }

        NonNullList<ItemStack> items = inventory.getNonEquipmentItems();

        // Compare inventory with previous state to detect new/changed items
        if (previousItems != null) {
            int size = Math.min(previousItems.length, items.size());
            for (int i = 0; i < size; i++) {
                ItemStack prev = previousItems[i];
                ItemStack curr = items.get(i);
                boolean prevEmpty = prev == null || prev.isEmpty();
                boolean currEmpty = curr == null || curr.isEmpty();

                if (prevEmpty && currEmpty) continue;

                if (prevEmpty) {
                    // New item appeared
                    highlightedSlots.put(i, System.currentTimeMillis());
                    slotCounts.merge(i, curr.getCount(), Integer::sum);
                } else if (currEmpty) {
                    // Item removed (dropped, used, etc.)
                    clearSlot(i);
                } else if (prev.getItem() == curr.getItem()) {
                    if (curr.getCount() > prev.getCount()) {
                        // Count increased
                        highlightedSlots.put(i, System.currentTimeMillis());
                        int delta = curr.getCount() - prev.getCount();
                        slotCounts.merge(i, delta, Integer::sum);
                    } else if (curr.getCount() < prev.getCount()) {
                        // Count decreased (dropped some)
                        clearSlot(i);
                    }
                } else {
                    // Item type changed
                    clearSlot(i);
                }
            }
        }

        snapshotInventory(inventory);

        PickupHighlightConfig cfg = PickupHighlight.config;

        // Clear on inventory close
        if (cfg.clearOnClose
                && previousScreen instanceof AbstractContainerScreen
                && client.screen == null) {
            highlightedSlots.clear();
            slotCounts.clear();
        }

        // Clear on hotbar slot selection
        int selected = inventory.getSelectedSlot();
        if (cfg.clearOnSelect && selected != previousSelectedSlot) {
            highlightedSlots.remove(selected);
            slotCounts.remove(selected);
        }

        // Clear expired highlights (timeout)
        if (cfg.timeoutSeconds > 0) {
            long now = System.currentTimeMillis();
            long timeoutMs = cfg.timeoutSeconds * 1000L;
            highlightedSlots.entrySet().removeIf(e -> {
                boolean expired = now - e.getValue() > timeoutMs;
                if (expired) slotCounts.remove(e.getKey());
                return expired;
            });
        }

        previousSelectedSlot = selected;
        previousScreen = client.screen;
    }

    private static void snapshotInventory(Inventory inventory) {
        NonNullList<ItemStack> items = inventory.getNonEquipmentItems();
        int size = items.size();
        previousItems = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            previousItems[i] = items.get(i).copy();
        }
    }

    public static boolean isHighlighted(int slotIndex) {
        return highlightedSlots.containsKey(slotIndex);
    }

    public static void clearSlot(int slotIndex) {
        highlightedSlots.remove(slotIndex);
        slotCounts.remove(slotIndex);
    }

    public static int getCount(int slotIndex) {
        return slotCounts.getOrDefault(slotIndex, 0);
    }

    public static float getPulseScale(int slotIndex) {
        Long timestamp = highlightedSlots.get(slotIndex);
        if (timestamp == null) return 1.0f;
        long elapsed = System.currentTimeMillis() - timestamp;
        return 0.75f + 0.25f * (float) Math.abs(Math.sin(elapsed / 400.0));
    }

    private static void reset() {
        highlightedSlots.clear();
        slotCounts.clear();
        previousItems = null;
        previousSelectedSlot = -1;
        previousScreen = null;
        initialized = false;
    }
}
