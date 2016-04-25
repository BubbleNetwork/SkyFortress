package com.thebubblenetwork.skyfortress.chest.gen;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Deprecated
public class __INVALID__ChestItem {
    private __INVALID__ChestSlot slot;
    private double percent;
    private int stacksize[];
    private short data;
    private Material material;
    private __INVALID__ChestItem[] pairs;
    private Map<Enchantment, Integer> enchantmentIntegerMap;

    public __INVALID__ChestItem(__INVALID__ChestSlot slot, Map<Enchantment, Integer> enchantmentIntegerMap, __INVALID__ChestItem[] pairs, Material material, int[] stacksize, short data, double percent) {
        this.slot = slot;
        this.enchantmentIntegerMap = enchantmentIntegerMap;
        this.pairs = pairs;
        this.material = material;
        this.stacksize = stacksize;
        this.data = data;
        this.percent = percent;
    }

    public ItemStack create() {
        return new ItemStackBuilder(getMaterial()).withAmount(getRandomStackSize()).withData(getData()).withEnchantments(getEnchantmentIntegerMap()).build();
    }

    public int getRandomStackSize() {
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

    public __INVALID__ChestSlot getSlot() {
        return slot;
    }

    public double getPercent() {
        return percent;
    }

    public __INVALID__ChestItem[] getPairs() {
        return pairs;
    }

    public Map<Enchantment, Integer> getEnchantmentIntegerMap() {
        return enchantmentIntegerMap;
    }

}
