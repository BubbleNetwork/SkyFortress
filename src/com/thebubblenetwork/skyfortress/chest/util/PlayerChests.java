package com.thebubblenetwork.skyfortress.chest.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.skyfortress.chest.ChestType;
import com.thebubblenetwork.skyfortress.chest.ItemGen;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerChests implements ItemGen {
    private Multimap<Slot,GenItem> original;
    private List<GenItem> items = new ArrayList<>();

    public PlayerChests(Multimap<Slot,GenItem> multimap){
        original = multimap;
        fill();
    }

    public List<GenItem> getItems() {
        return items;
    }

    private GenItem chose(Collection<GenItem> items){
        int size = items.size();
        if(size == 0)throw new IllegalArgumentException("Not enough items");
        if(size == 1)return Iterables.getFirst(items,null);
        double percentsize = 0D;
        for(GenItem item:items)percentsize += (double)item.getPercent();
        for(GenItem item:items){
            if(BubbleNetwork.getRandom().nextFloat() > chance(percentsize,item.getPercent())){
                return item;
            }
        }
        throw new IllegalArgumentException("Invalid percentages");
    }

    public void fill(){
        for(Collection<GenItem> genItemCollection:original.asMap().values()){
            getItems().add(chose(genItemCollection));
        }
    }

    public void fill(Collection<Type> types){
        for(Map.Entry<Slot,Collection<GenItem>> genItemCollection:original.asMap().entrySet()){
            if(types.contains(genItemCollection.getKey().getType())){
                getItems().add(chose(genItemCollection.getValue()));
            }
        }
    }

    private float chance(double percentsize,float currentchance){
        return (float)(currentchance/percentsize);
    }

    public ItemStack[] generate(ChestType type) {
        //Checking for missing types
        Set<Type> types = new HashSet<>(Arrays.asList(Type.values()));
        for(GenItem item:getItems()){
            types.remove(item.getSlot().getType());
        }
        fill(types);
        Collections.shuffle(getItems());

        //Getting chosen items
        Set<GenItem> chosen = new HashSet<>();
        for(Type itemtype:Type.values()){
            for(GenItem item:getItems()){
                if(item.getSlot().getType() == itemtype){
                    getItems().remove(item);
                    chosen.add(item);
                    chosen.addAll(Arrays.asList(item.getPairs()));
                }
            }
        }

        //Creating random slots
        List<Integer> list = new ArrayList<>();
        for(int i = 0;i < type.getSize();i++)list.add(i);
        Collections.shuffle(list);
        ItemStack[] itemStacks = new ItemStack[type.getSize()];
        int i = 0;

        //Copying into slots
        for(GenItem item:chosen){
            itemStacks[list.get(i)] = item.create();
            i++;
        }
        return itemStacks;
    }
}
