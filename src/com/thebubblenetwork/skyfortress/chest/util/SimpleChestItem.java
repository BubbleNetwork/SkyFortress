package com.thebubblenetwork.skyfortress.chest.util;

import com.thebubblenetwork.skyfortress.chest.gen.ChestItem;
import com.thebubblenetwork.skyfortress.chest.gen.ChestSlot;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public class SimpleChestItem extends ChestItem {
    public static ChestSlot cleverSlot(Material material) {
        for (ChestSlot slot : ChestSlot.values()) {
            if (material.toString().contains(slot.toString())) {
                return slot;
            }
        }
        return null;
    }

    public SimpleChestItem(Material material, Map<Enchantment, Integer> enchantmentIntegerMap, float percent) {
        super(cleverSlot(material), enchantmentIntegerMap, new ChestItem[0], material, new int[]{1}, (short) 0, percent);
    }
}
