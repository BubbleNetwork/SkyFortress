package com.thebubblenetwork.skyfortress.chest.gen;

import org.bukkit.inventory.ItemStack;

public abstract class NewChestItem {
    public abstract boolean shouldAdd(NewChestGeneration chestGeneration);

    public abstract ItemStack[] getStack();
}
