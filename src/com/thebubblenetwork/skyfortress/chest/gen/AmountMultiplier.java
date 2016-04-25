package com.thebubblenetwork.skyfortress.chest.gen;

import org.bukkit.inventory.ItemStack;

public class AmountMultiplier {
    private ItemStack stack;
    private int amt[];

    public AmountMultiplier(ItemStack stack, int[] amt) {
        this.stack = stack;
        this.amt = amt;
    }
}
