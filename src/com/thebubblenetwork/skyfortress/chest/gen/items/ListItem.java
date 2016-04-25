package com.thebubblenetwork.skyfortress.chest.gen.items;

import com.thebubblenetwork.skyfortress.chest.gen.NewChestGeneration;
import com.thebubblenetwork.skyfortress.chest.gen.NewChestItem;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListItem extends NewChestItem{
    private int removepertime;
    private boolean refill;
    private ItemStack[] original;
    private List<ItemStack> items;

    public ListItem(int removepertime, boolean refill, ItemStack[] original) {
        this.removepertime = removepertime;
        this.refill = refill;
        this.items = new ArrayList<>(Arrays.asList(original));
        Collections.shuffle(items);
        if(refill){
            this.original = original;
        }
    }

    public boolean shouldAdd(NewChestGeneration chestGeneration) {
        return true;
    }

    public ItemStack[] getStack() {
        ItemStack stack[] = new ItemStack[getRemovepertime()];
        for(int i = 0; i < getRemovepertime(); i++){
            if(items.isEmpty()){
                if(refill) {
                    items.addAll(Arrays.asList(original));
                    Collections.shuffle(items);
                }
                else break;
            }
            stack[i] = items.remove(0);
        }
        return stack;
    }

    public int getRemovepertime() {
        return removepertime;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public boolean isRefill() {
        return refill;
    }
}
