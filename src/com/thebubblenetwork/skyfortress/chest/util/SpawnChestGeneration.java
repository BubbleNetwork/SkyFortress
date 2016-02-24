package com.thebubblenetwork.skyfortress.chest.util;

import com.google.common.collect.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;


public class SpawnChestGeneration extends ChestGeneration {

    private static Multimap<ChestSlot, ChestItem> genItemMultimap(){
        Multimap<ChestSlot, ChestItem> multimap = ArrayListMultimap.create(ChestSlot.values().length,5);

        //ARMOR
        doSimple(multimap,new Material[]{Material.DIAMOND_HELMET,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_BOOTS},15F,5F,1);
        doSimple(multimap,new Material[]{Material.IRON_HELMET,Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS,Material.IRON_BOOTS},30F,5F,1,2);
        doSimple(multimap,new Material[]{Material.CHAINMAIL_HELMET,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_LEGGINGS,Material.CHAINMAIL_BOOTS},60F,5F,1,2);

        //TOOLS
        doSimple(multimap,new Material[]{Material.DIAMOND_SWORD,Material.DIAMOND_PICKAXE,Material.DIAMOND_AXE},10F,5F,1);
        doSimple(multimap,new Material[]{Material.IRON_SWORD,Material.IRON_PICKAXE,Material.DIAMOND_AXE},30F,10F,1);
        doSimple(multimap,new Material[]{Material.STONE_SWORD,Material.STONE_PICKAXE,Material.STONE_AXE},50F,10F,1,2);

        //BOW
        ChestItem arrows = new ChestItem(ChestSlot.OTHER_WEAPON,new HashMap<Enchantment,Integer>(),new ChestItem[0],Material.ARROW,new int[]{8,16,32},(short)0,0F);

        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[]{arrows},Material.BOW,
                new int[]{1},
                (short)0,
                40F));
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.ARROW_DAMAGE,1).build(),
                new ChestItem[]{arrows},Material.BOW,
                new int[]{1},
                (short)0,
                20F));
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.ARROW_DAMAGE,2).build(),
                new ChestItem[]{arrows},
                Material.BOW,
                new int[]{1},
                (short)0,
                10F));

        //ROD
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.FISHING_ROD,
                new int[]{1},
                (short)0,
                30F
                ));

        //SNOWBALLS/EGGS
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.EGG,
                new int[]{8,16,32},
                (short)0,
                10F
                ));
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.SNOW_BALL,
                new int[]{8,16,32},
                (short)0,
                10F
        ));

        //BUCKETS
        doMap(multimap,new ChestItem(ChestSlot.META,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.WATER_BUCKET,
                new int[]{1},
                (short)0,
                20F
        ));
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.LAVA_BUCKET,
                new int[]{1},
                (short)0,
                20F
        ));

        //FNS
        doMap(multimap,new ChestItem(ChestSlot.OTHER_WEAPON,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.FLINT_AND_STEEL,
                new int[]{1},
                (short)((int)Material.FLINT_AND_STEEL.getMaxDurability()-10),
                20F
        ));

        //Blocks
        doMap(multimap,new ChestItem(ChestSlot.BLOCK,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.COBBLESTONE,
                new int[]{8,16,32,64},
                (short)0,
                40F
                ));
        doMap(multimap,new ChestItem(ChestSlot.BLOCK,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.LOG,
                new int[]{8,16},
                (short)0,
                20F
        ));
        doMap(multimap,new ChestItem(ChestSlot.BLOCK,
                new HashMap<Enchantment, Integer>(),
                new ChestItem[0],
                Material.WOOD,
                new int[]{8,16,32,64},
                (short)0,
                30F
        ));
        return multimap;
    }

    private static void doSimple(Multimap<ChestSlot, ChestItem> map, Material[] materials, float f1, float f2, int... enchants){
        for(Material material:materials) {
            ChestSlot cleverslot = SimpleChestItem.cleverSlot(material);
            doMap(map,
                    new SimpleChestItem(
                            material,
                            new HashMap<Enchantment, Integer>(),
                            f1
                    )
            );
            for (int i : enchants) {
                Enchantment enchantment = getEnchant(cleverslot);
                if(enchantment == null)continue;
                doMap(map,
                        new SimpleChestItem(
                                material,
                                new ImmutableMap.Builder<Enchantment, Integer>().put(enchantment, i).build(),
                                f1 - (f2 * i)
                        )
                );
            }
        }
    }

    private static Enchantment getEnchant(ChestSlot slot){
        if(slot.getType() == ChestType.ARMOR)return Enchantment.PROTECTION_ENVIRONMENTAL;
        if(slot.getType() == ChestType.TOOL){
            if(slot == ChestSlot.SWORD)return Enchantment.DAMAGE_ALL;
            else return Enchantment.DIG_SPEED;
        }
        return null;
    }

    private static void doMap(Multimap<ChestSlot, ChestItem> map, ChestItem... items){
        for(ChestItem item:items){
            map.put(item.getSlot(),item);
        }
    }

    public SpawnChestGeneration() {
        super(genItemMultimap());
    }
}
