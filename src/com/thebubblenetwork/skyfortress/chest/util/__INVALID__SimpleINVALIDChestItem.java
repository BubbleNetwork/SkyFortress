package com.thebubblenetwork.skyfortress.chest.util;

import com.thebubblenetwork.skyfortress.chest.gen.__INVALID__ChestItem;
import com.thebubblenetwork.skyfortress.chest.gen.__INVALID__ChestSlot;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

@Deprecated
public class __INVALID__SimpleINVALIDChestItem extends __INVALID__ChestItem {
    public static __INVALID__ChestSlot cleverSlot(Material material) {
        for (__INVALID__ChestSlot slot : __INVALID__ChestSlot.values()) {
            if (material.toString().contains(slot.toString())) {
                return slot;
            }
        }
        return null;
    }

    public __INVALID__SimpleINVALIDChestItem(Material material, Map<Enchantment, Integer> enchantmentIntegerMap, float percent) {
        super(cleverSlot(material), enchantmentIntegerMap, new __INVALID__ChestItem[0], material, new int[]{1}, (short) 0, percent);
    }
}
