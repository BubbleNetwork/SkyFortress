package com.thebubblenetwork.skyfortress.chest;

import org.bukkit.inventory.ItemStack;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {15:48}
 * Created February 2016
 */
public interface ItemGen {
    ItemStack[] generate(ChestGeneration.Type type);
}
