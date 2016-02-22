package com.thebubblenetwork.skyfortress.chest.util;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GenItem {
    private Slot slot;
    private float percent;
    private int stacksize[];
    private short data;
    private Material material;
    private GenItem[] pairs;
    private Map<Enchantment,Integer> enchantmentIntegerMap;

    public GenItem(Slot slot, Map<Enchantment, Integer> enchantmentIntegerMap, GenItem[] pairs, Material material, int[] stacksize, short data, float percent) {
        this.slot = slot;
        this.enchantmentIntegerMap = enchantmentIntegerMap;
        this.pairs = pairs;
        this.material = material;
        this.stacksize = stacksize;
        this.data = data;
        this.percent = percent;
    }

    public ItemStack create(){
        return new ItemStack(getMaterial(),getRandomStackSize(),getData());
    }

    public int getRandomStackSize(){
        return getStacksize()[BubbleNetwork.getRandom().nextInt(getStacksize().length)];
    }

    public int[] getStacksize() {
        return stacksize;
    }

    public short getData() {
        return data;
    }

    public Material getMaterial() {
        return material;
    }

    public Slot getSlot() {
        return slot;
    }

    public float getPercent() {
        return percent;
    }

    public GenItem[] getPairs() {
        return pairs;
    }

    public Map<Enchantment, Integer> getEnchantmentIntegerMap() {
        return enchantmentIntegerMap;
    }

}
