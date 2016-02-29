package com.thebubblenetwork.skyfortress.chest.gen;

import com.google.common.collect.Iterables;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.skyfortress.chest.ChestType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestGeneration{
    private final Map<ChestSlot,List<ChestItem>> original = new HashMap<>();
    private final Map<ChestSlotType,List<ChestItem>> items = new HashMap<>();

    public ChestGeneration(Set<ChestItem> items){
        for(ChestItem item:items){
            List<ChestItem> list = original.containsKey(item.getSlot()) ? original.get(item.getSlot()) : new ArrayList<ChestItem>();
            list.add(item);
            original.put(item.getSlot(),list);
        }
        fill();
    }

    public Map<ChestSlotType,List<ChestItem>> getItems() {
        return items;
    }

    private ChestItem chose(Collection<ChestItem> items){
        int size = items.size();
        if(items.isEmpty())throw new IllegalArgumentException("Not enough items");
        if(size == 1)return Iterables.getFirst(items,null);
        double percentsize = 0.0D;
        for(ChestItem item:items){
            percentsize += (double)item.getPercent();
        }
        float currentchance = 0.0F;
        float chosen = BubbleNetwork.getRandom().nextFloat();
        for(ChestItem item:items){
            currentchance += chance(percentsize,item.getPercent());
            if(chosen < currentchance){
                return item;
            }
        }
        //Made a mistake?
        throw new IllegalArgumentException("Invalid percentages");
    }

    public void fill(){
        for(Collection<ChestItem> genItemCollection:original.values()){
            ChestItem item = chose(genItemCollection);
            List<ChestItem> list = items.containsKey(item.getSlot().getType()) ? items.get(item.getSlot().getType()) : new ArrayList<ChestItem>();
            list.add(item);
            items.put(item.getSlot().getType(),list);
        }
    }

    public void fill(List<ChestItem> addto,ChestSlotType type){
        for(Map.Entry<ChestSlot,List<ChestItem>> entry:original.entrySet()) {
            if(entry.getKey().getType() == type) {
                ChestItem item = chose(entry.getValue());
                addto.add(item);
            }
        }
    }

    private float chance(double percentsize,float currentchance){
        return (float)((double)currentchance/percentsize);
    }

    public ItemStack[] generate(ChestType type) {
        //Find our items
        Set<ChestItem> chosen = new HashSet<>();
        for(Map.Entry<ChestSlotType,List<ChestItem>> entry: items.entrySet()){
            List<ChestItem> items = entry.getValue();
            if(items.isEmpty()){
                continue;
            }
            Collections.shuffle(items);

            //Removing from original
            ChestItem item = items.remove(0);

            //Adding to chosen
            chosen.add(item);
            chosen.addAll(Arrays.asList(item.getPairs()));

            //Checking for missing types
            if(items.isEmpty()){
                fill(items,entry.getKey());
            }
        }

        //Creating random slots
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < type.getSize();i++)list.add(i);
        Collections.shuffle(list);
        ItemStack[] itemStacks = new ItemStack[type.getSize()];

        //Copying into slots
        for(ChestItem item:chosen){
            itemStacks[list.remove(0)] = item.create();
        }
        return itemStacks;
    }
}
