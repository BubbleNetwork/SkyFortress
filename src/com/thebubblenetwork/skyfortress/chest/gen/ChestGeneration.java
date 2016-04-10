package com.thebubblenetwork.skyfortress.chest.gen;

import com.google.common.collect.Iterables;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.skyfortress.chest.ChestType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestGeneration {
    /*
    How the chestgen works:
    Each chest contains an item of a ChestSlotType
    The itemlist contains 1 random item from each list originally submitted.
     */

    private final Map<ChestSlot, List<ChestItem>> original = new HashMap<>();
    private final Map<ChestSlotType, List<ChestItem>> items = new HashMap<>();

    public ChestGeneration(Set<ChestItem> items) {
        for (ChestItem item : items) {
            List<ChestItem> list = original.containsKey(item.getSlot()) ? original.get(item.getSlot()) : new ArrayList<ChestItem>();
            list.add(item);
            original.put(item.getSlot(), list);
        }
        fill();
    }

    public Map<ChestSlotType, List<ChestItem>> getItems() {
        return items;
    }

    private ChestItem chose(Collection<ChestItem> items) {
        int size = items.size();
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Not enough items");
        }
        if (size == 1) {
            return Iterables.getFirst(items, null);
        }
        double percentsize = 0.0D;
        //Find the total percentage
        for (ChestItem item : items) {
            percentsize += item.getPercent();
        }
        double currentchance = 0.0D;
        //Get a number between 0 and percentsize
        double chosen = BubbleNetwork.getRandom().nextDouble() * percentsize;
        for (ChestItem item : items) {
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
        for (Collection<ChestItem> genItemCollection : original.values()) {
            ChestItem item = chose(genItemCollection);
            List<ChestItem> list = items.containsKey(item.getSlot().getType()) ? items.get(item.getSlot().getType()) : new ArrayList<ChestItem>();
            list.add(item);
            items.putIfAbsent(item.getSlot().getType(), list);
        }
    }

    public void fill(List<ChestItem> addto, ChestSlotType type) {
        for (Map.Entry<ChestSlot, List<ChestItem>> entry : original.entrySet()) {
            if (entry.getKey().getType() == type) {
                ChestItem item = chose(entry.getValue());
                addto.add(item);
            }
        }
    }

    public ItemStack[] generate(ChestType type) {
        //Find our items
        Set<ChestItem> chosen = new HashSet<>();
        for (Map.Entry<ChestSlotType, List<ChestItem>> entry : items.entrySet()) {
            List<ChestItem> items = entry.getValue();
            if (items.isEmpty()) {
                continue;
            }
            Collections.shuffle(items);

            //Removing from original
            ChestItem item = items.remove(0);

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
        for (ChestItem item : chosen) {
            itemStacks[list.remove(0)] = item.create();
        }
        return itemStacks;
    }
}
