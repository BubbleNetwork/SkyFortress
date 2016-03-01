package com.thebubblenetwork.skyfortress.chest.util;

import com.google.common.collect.ImmutableMap;
import com.thebubblenetwork.skyfortress.chest.gen.ChestGeneration;
import com.thebubblenetwork.skyfortress.chest.gen.ChestItem;
import com.thebubblenetwork.skyfortress.chest.gen.ChestSlot;
import com.thebubblenetwork.skyfortress.chest.gen.ChestSlotType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class SpawnChestGeneration extends ChestGeneration {
    private static final Set<ChestItem> chestItems = new HashSet<>();

    private static void doSimple(Material[] materials, float f1, float f2, int... enchants) {
        for (Material material : materials) {
            ChestSlot cleverslot = SimpleChestItem.cleverSlot(material);
            chestItems.add(new SimpleChestItem(material, new HashMap<Enchantment, Integer>(), f1));
            for (int i : enchants) {
                Enchantment enchantment = getEnchant(cleverslot);
                if (enchantment == null) {
                    continue;
                }
                chestItems.add(new SimpleChestItem(material, new ImmutableMap.Builder<Enchantment, Integer>().put(enchantment, i).build(), f1 - (f2 * i)));
            }
        }
    }

    private static Enchantment getEnchant(ChestSlot slot) {
        if (slot.getType() == ChestSlotType.ARMOR) {
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        if (slot.getType() == ChestSlotType.TOOL) {
            if (slot == ChestSlot.SWORD) {
                return Enchantment.DAMAGE_ALL;
            } else {
                return Enchantment.DIG_SPEED;
            }
        }
        return null;
    }

    static {
        //ARMOR
        doSimple(new Material[]{Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS}, 0.15F, 0.05F, 1);
        doSimple(new Material[]{Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS}, 0.30F, 0.05F, 1, 2);
        doSimple(new Material[]{Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS}, 0.60F, 0.05F, 1, 2);

        //TOOLS
        doSimple(new Material[]{Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE}, 0.10F, 0.05F, 1);
        doSimple(new Material[]{Material.IRON_SWORD, Material.IRON_PICKAXE, Material.DIAMOND_AXE}, 0.30F, 0.10F, 1);
        doSimple(new Material[]{Material.STONE_SWORD, Material.STONE_PICKAXE, Material.STONE_AXE}, 0.50F, 0.10F, 1, 2);

        //BOW
        ChestItem arrows = new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.ARROW, new int[]{8, 16, 32}, (short) 0, 0F);

        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[]{arrows}, Material.BOW, new int[]{1}, (short) 0, 0.40F));
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.ARROW_DAMAGE, 1).build(), new ChestItem[]{arrows}, Material.BOW, new int[]{1}, (short) 0, 0.20F));
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.ARROW_DAMAGE, 2).build(), new ChestItem[]{arrows}, Material.BOW, new int[]{1}, (short) 0, 0.10F));

        //ROD
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.FISHING_ROD, new int[]{1}, (short) 0, 0.30F));

        //SNOWBALLS/EGGS
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.EGG, new int[]{8, 16, 32}, (short) 0, 0.10F));
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.SNOW_BALL, new int[]{8, 16, 32}, (short) 0, 0.10F));

        //BUCKETS - USED AS A BLOCK TOOL
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.WATER_BUCKET, new int[]{1}, (short) 0, 0.20F));
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.LAVA_BUCKET, new int[]{1}, (short) 0, 0.20F));

        //FNS
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.FLINT_AND_STEEL, new int[]{1}, (short) ((int) Material.FLINT_AND_STEEL.getMaxDurability() - 10), 0.20F));

        //Blocks
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.COBBLESTONE, new int[]{8, 16, 32, 64}, (short) 0, 0.40F));
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.LOG, new int[]{8, 16}, (short) 0, 0.20F));
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.WOOD, new int[]{8, 16, 32, 64}, (short) 0, 0.30F));
    }

    public SpawnChestGeneration() {
        super(chestItems);
    }
}
