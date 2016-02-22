package com.thebubblenetwork.skyfortress.chest.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

public class SimpleItem extends GenItem{
    public static Slot cleverSlot(Material material){
        for(Slot slot:Slot.values()){
            if(material.toString().contains(slot.toString()))return slot;
        }
        return null;
    }

    public SimpleItem(Material material, Map<Enchantment, Integer> enchantmentIntegerMap, float percent) {
        super(cleverSlot(material), enchantmentIntegerMap, new GenItem[0], material, new int[]{1}, (short)0, percent);
    }
}
