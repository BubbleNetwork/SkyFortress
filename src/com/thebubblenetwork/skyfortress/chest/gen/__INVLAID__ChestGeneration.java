package com.thebubblenetwork.skyfortress.chest.gen;

import com.google.common.collect.Iterables;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.skyfortress.chest.__INVALID__ChestType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Deprecated
public class __INVLAID__ChestGeneration {
    /*
    How the chestgen works:
    Each chest contains an item of a ChestSlotType
    The itemlist contains 1 random item from each list originally submitted.
     */

    private final Map<__INVALID__ChestSlot, List<__INVALID__ChestItem>> original = new HashMap<>();
    private final Map<__INVALID__ChestSlotType, List<__INVALID__ChestItem>> items = new HashMap<>();

    public __INVLAID__ChestGeneration(Set<__INVALID__ChestItem> items) {
        for (__INVALID__ChestItem item : items) {
            List<__INVALID__ChestItem> list = original.containsKey(item.getSlot()) ? original.get(item.getSlot()) : new ArrayList<__INVALID__ChestItem>();
            list.add(item);
            original.put(item.getSlot(), list);
        }
        fill();
    }

    public Map<__INVALID__ChestSlotType, List<__INVALID__ChestItem>> getItems() {
        return items;
    }

    private __INVALID__ChestItem chose(Collection<__INVALID__ChestItem> items) {
        int size = items.size();
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Not enough items");
        }
        if (size == 1) {
            return Iterables.getFirst(items, null);
        }
        double percentsize = 0.0D;
        //Find the total percentage
        for (__INVALID__ChestItem item : items) {
            percentsize += item.getPercent();
        }
        double currentchance = 0.0D;
        //Get a number between 0 and percentsize
        double chosen = BubbleNetwork.getRandom().nextDouble() * percentsize;
        for (__INVALID__ChestItem item : items) {
            //Add to currentchance the total percentage
            currentchance += percentsize / item.getPercent();
            //If the currentchance is NOW above the random number it has been selected
            if (chosen < currentchance) {
                return item;
            }
        }
        //Made a mistake?
        throw new IllegalArgumentException("Invalid percentages");
    }

    public void fill() {
        for (Collection<__INVALID__ChestItem> genItemCollection : original.values()) {
            __INVALID__ChestItem item = chose(genItemCollection);
            List<__INVALID__ChestItem> list = items.containsKey(item.getSlot().getType()) ? items.get(item.getSlot().getType()) : new ArrayList<__INVALID__ChestItem>();
            list.add(item);
            items.put(item.getSlot().getType(), list);
        }
    }

    public void fill(List<__INVALID__ChestItem> addto, __INVALID__ChestSlotType type) {
        for (Map.Entry<__INVALID__ChestSlot, List<__INVALID__ChestItem>> entry : original.entrySet()) {
            if (entry.getKey().getType() == type) {
                __INVALID__ChestItem item = chose(entry.getValue());
                addto.add(item);
            }
        }
    }

    public ItemStack[] generate(__INVALID__ChestType type) {
        //Find our items
        Set<__INVALID__ChestItem> chosen = new HashSet<>();
        for (Map.Entry<__INVALID__ChestSlotType, List<__INVALID__ChestItem>> entry : items.entrySet()) {
            List<__INVALID__ChestItem> items = entry.getValue();
            if (items.isEmpty()) {
                continue;
            }
            Collections.shuffle(items);

            //Removing from original
            __INVALID__ChestItem item = items.remove(0);

            //Adding to chosen
            chosen.add(item);
            chosen.addAll(Arrays.asList(item.getPairs()));

            //Checking for missing types
            if (items.isEmpty()) {
                fill(items, entry.getKey());
            }
        }

        //Creating random slots
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < type.getSize(); i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        ItemStack[] itemStacks = new ItemStack[type.getSize()];

        //Copying into slots
        for (__INVALID__ChestItem item : chosen) {
            itemStacks[list.remove(0)] = item.create();
        }
        return itemStacks;
    }
}
