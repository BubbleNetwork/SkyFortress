package com.thebubblenetwork.skyfortress.chest.gen.items;

import com.thebubblenetwork.skyfortress.chest.gen.NewChestGeneration;
import com.thebubblenetwork.skyfortress.chest.gen.NewChestItem;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ChanceItem extends NewChestItem{
    private static Random random = new Random();

    private float chance;
    private ItemStack[] stacks;

    public ChanceItem(float chance, ItemStack[] stacks) {
        this.chance = chance;
        this.stacks = stacks;
    }

    public boolean shouldAdd(NewChestGeneration chestGeneration) {
        return random.nextFloat() < chance;
    }

    public ItemStack[] getStack() {
        return stacks;
    }
}
