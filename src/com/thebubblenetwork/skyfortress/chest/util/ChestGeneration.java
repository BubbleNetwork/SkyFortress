package com.thebubblenetwork.skyfortress.chest.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestGeneration{
    private Multimap<ChestSlot,ChestItem> original;
    private Set<ChestItem> items = new HashSet<>();

    public ChestGeneration(Multimap<ChestSlot,ChestItem> multimap){
        original = multimap;
        fill();
    }

    public Set<ChestItem> getItems() {
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
        for(Collection<ChestItem> genItemCollection:original.asMap().values()){
            getItems().add(chose(genItemCollection));
        }
    }

    public void fill(Collection<ChestType> types){
        for(Map.Entry<ChestSlot,Collection<ChestItem>> genItemCollection:original.asMap().entrySet()){
            if(types.contains(genItemCollection.getKey().getType())){
                getItems().add(chose(genItemCollection.getValue()));
            }
        }
    }

    private float chance(double percentsize,float currentchance){
        return (float)((double)currentchance/percentsize);
    }

    public ItemStack[] generate(com.thebubblenetwork.skyfortress.chest.ChestType type) {
        //Checking for missing types
        Set<ChestType> types = new HashSet<>(Arrays.asList(ChestType.values()));
        for(ChestItem item:getItems()){
            if(types.contains(item.getSlot().getType()))types.remove(item.getSlot().getType());
        }
        fill(types);

        //Getting chosen items
        Set<ChestItem> chosen = new HashSet<>();
        for(ChestType itemtype: ChestType.values()){
            for(ChestItem item:getItems()){
                if(item.getSlot().getType() == itemtype){
                    chosen.add(item);
                    chosen.addAll(Arrays.asList(item.getPairs()));
                }
            }
        }

        getItems().removeAll(chosen);

        //Creating random slots
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < type.getSize();i++)list.add(i);
        Collections.shuffle(list);
        ItemStack[] itemStacks = new ItemStack[type.getSize()];
        int i = 0;

        //Copying into slots
        for(ChestItem item:chosen){
            itemStacks[list.get(i)] = item.create();
            i++;
        }
        return itemStacks;
    }
}
