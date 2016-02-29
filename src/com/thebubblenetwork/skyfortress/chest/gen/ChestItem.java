package com.thebubblenetwork.skyfortress.chest.gen;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ChestItem {
    private ChestSlot slot;
    private float percent;
    private int stacksize[];
    private short data;
    private Material material;
    private ChestItem[] pairs;
    private Map<Enchantment,Integer> enchantmentIntegerMap;

    public ChestItem(ChestSlot slot, Map<Enchantment, Integer> enchantmentIntegerMap, ChestItem[] pairs, Material material, int[] stacksize, short data, float percent) {
        this.slot = slot;
        this.enchantmentIntegerMap = enchantmentIntegerMap;
        this.pairs = pairs;
        this.material = material;
        this.stacksize = stacksize;
        this.data = data;
        this.percent = percent;
    }

    public ItemStack create(){
        return new ItemStackBuilder(getMaterial()).withAmount(getRandomStackSize()).withData(getData()).withEnchantments(getEnchantmentIntegerMap()).build();
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

    public ChestSlot getSlot() {
        return slot;
    }

    public float getPercent() {
        return percent;
    }

    public ChestItem[] getPairs() {
        return pairs;
    }

    public Map<Enchantment, Integer> getEnchantmentIntegerMap() {
        return enchantmentIntegerMap;
    }

}
