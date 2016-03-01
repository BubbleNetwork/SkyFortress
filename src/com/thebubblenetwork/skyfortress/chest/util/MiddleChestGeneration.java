package com.thebubblenetwork.skyfortress.chest.util;

import com.google.common.collect.ImmutableMap;
import com.thebubblenetwork.skyfortress.chest.gen.ChestGeneration;
import com.thebubblenetwork.skyfortress.chest.gen.ChestItem;
import com.thebubblenetwork.skyfortress.chest.gen.ChestSlot;
import com.thebubblenetwork.skyfortress.chest.gen.ChestSlotType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MiddleChestGeneration extends ChestGeneration {
    private static void doPotion(int[] stacks, PotionType type, boolean splash, int extrapower, boolean extralength, float chance) {
        Potion potion = new Potion(type);
        //NEEDED BECAUSE OF PRECHECKS
        if (splash) {
            potion.setSplash(true);
        }
        if (extrapower != 1) {
            potion.setLevel(extrapower);
        }
        if (extralength) {
            potion.setHasExtendedDuration(true);
        }
        short dur = potion.toDamageValue();
        chestItems.add(new ChestItem(ChestSlot.FOOD, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.POTION, stacks, dur, chance));
    }

    private static void doSimple(Material[] materials, float f1, float f2, int... enchants) {
        for (Material material : materials) {
            ChestSlot cleverslot = SimpleChestItem.cleverSlot(material);
            chestItems.add(new SimpleChestItem(material, new HashMap<Enchantment, Integer>(), f1));
            for (int i : enchants) {
                Set<Enchantment> enchantment = getEnchant(cleverslot);
                if (enchantment.isEmpty()) {
                    continue;
                }
                int size = enchantment.size();
                float chance = (f1 - (f2 * i)) / (size + 1);
                Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
                for (Enchantment e : enchantment) {
                    chestItems.add(new SimpleChestItem(material, new ImmutableMap.Builder<Enchantment, Integer>().put(e, i).build(), chance));
                    enchantmentIntegerMap.put(e, i);
                }
                chestItems.add(new SimpleChestItem(material, enchantmentIntegerMap, chance));
            }
        }
    }

    private static Set<Enchantment> getEnchant(ChestSlot slot) {
        Set<Enchantment> enchantments = new HashSet<>();
        if (slot.getType() == ChestSlotType.ARMOR) {
            enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
            if (slot == ChestSlot.BOOTS) {
                enchantments.add(Enchantment.PROTECTION_FALL);
            } else if (slot == ChestSlot.HELMET) {
                enchantments.add(Enchantment.WATER_WORKER);
            } else {
                enchantments.add(Enchantment.PROTECTION_PROJECTILE);
            }
        }
        if (slot.getType() == ChestSlotType.TOOL) {
            if (slot == ChestSlot.SWORD) {
                enchantments.add(Enchantment.DAMAGE_ALL);
            } else {
                enchantments.add(Enchantment.DIG_SPEED);
                enchantments.add(Enchantment.LOOT_BONUS_BLOCKS);
            }
        }
        return enchantments;
    }

    private static Set<ChestItem> chestItems = new HashSet<>();

    static {
        //TOOLS
        doSimple(new Material[]{Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SWORD}, 0.3F, 0.05F, 2, 3, 4);
        doSimple(new Material[]{Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SWORD}, 0.4F, 0.05F, 3, 4, 5);

        //ARMOR
        doSimple(new Material[]{Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS}, 0.3F, 0.05F, 2, 3, 4);
        doSimple(new Material[]{Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS}, 0.4F, 0.05F, 3, 4, 5);

        //FNS
        ChestItem FNS = new ChestItem(ChestSlot.EXTRA, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.FLINT_AND_STEEL, new int[]{1}, (short) 0, 0F);

        //TNT
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[]{FNS}, Material.TNT, new int[]{16, 32, 48, 64}, (short) 0, 0.5F));

        //CRAPPLES
        chestItems.add(new ChestItem(ChestSlot.FOOD, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.GOLDEN_APPLE, new int[]{2, 3, 4, 5, 6}, (short) 0, 0.3F));

        //BLOCKS
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.COBBLESTONE, new int[]{64}, (short) 0, 0.3F));
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.WOOD, new int[]{64}, (short) 0, 0.3F));
        chestItems.add(new ChestItem(ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.LOG, new int[]{32, 64}, (short) 0, 0.1F));

        //PEARLS
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.ENDER_PEARL, new int[]{1, 2, 3}, (short) 0, 0.05F));

        //EGGS/SNOWBALLS
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.EGG, new int[]{64}, (short) 0, 0.25F));
        chestItems.add(new ChestItem(ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new ChestItem[0], Material.SNOW_BALL, new int[]{64}, (short) 0, 0.25F));

        //POTIONS
        doPotion(new int[]{3}, PotionType.INSTANT_HEAL, true, 1, false, 0.2F);
        doPotion(new int[]{3}, PotionType.INSTANT_HEAL, true, 2, false, 0.1F);
        doPotion(new int[]{1}, PotionType.FIRE_RESISTANCE, false, 1, false, 0.2F);

    }

    public MiddleChestGeneration() {
        super(chestItems);
    }
}
