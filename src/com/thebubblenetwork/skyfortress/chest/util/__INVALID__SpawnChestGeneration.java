package com.thebubblenetwork.skyfortress.chest.util;

import com.google.common.collect.ImmutableMap;
import com.thebubblenetwork.skyfortress.chest.gen.__INVLAID__ChestGeneration;
import com.thebubblenetwork.skyfortress.chest.gen.__INVALID__ChestItem;
import com.thebubblenetwork.skyfortress.chest.gen.__INVALID__ChestSlot;
import com.thebubblenetwork.skyfortress.chest.gen.__INVALID__ChestSlotType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@Deprecated
public class __INVALID__SpawnChestGeneration extends __INVLAID__ChestGeneration {
    private static final Set<__INVALID__ChestItem> chestItems = new HashSet<>();

    private static void doSimple(Material[] materials, float f1, float f2, int... enchants) {
        for (Material material : materials) {
            __INVALID__ChestSlot cleverslot = __INVALID__SimpleINVALIDChestItem.cleverSlot(material);
            chestItems.add(new __INVALID__SimpleINVALIDChestItem(material, new HashMap<Enchantment, Integer>(), f1));
            for (int i : enchants) {
                Enchantment enchantment = getEnchant(cleverslot);
                if (enchantment == null) {
                    continue;
                }
                chestItems.add(new __INVALID__SimpleINVALIDChestItem(material, new ImmutableMap.Builder<Enchantment, Integer>().put(enchantment, i).build(), f1 - (f2 * i)));
            }
        }
    }

    private static Enchantment getEnchant(__INVALID__ChestSlot slot) {
        if (slot.getType() == __INVALID__ChestSlotType.ARMOR) {
            return Enchantment.PROTECTION_ENVIRONMENTAL;
        }
        if (slot.getType() == __INVALID__ChestSlotType.TOOL) {
            if (slot == __INVALID__ChestSlot.SWORD) {
                return Enchantment.DAMAGE_ALL;
            } else {
                return Enchantment.DIG_SPEED;
            }
        }
        return null;
    }

    static {
        //ARMOR
        doSimple(new Material[]{Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS}, 0.30F, 0.1F, 1, 2);
        doSimple(new Material[]{Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS}, 0.60F, 0.1F, 1, 2);

        //TOOLS
        doSimple(new Material[]{Material.IRON_SWORD, Material.IRON_PICKAXE, Material.DIAMOND_AXE}, 0.3F, 0.1F, 1);
        doSimple(new Material[]{Material.STONE_SWORD, Material.STONE_PICKAXE, Material.STONE_AXE}, 0.6F, 0.1F, 1, 2);

        //BOW
        __INVALID__ChestItem arrows = new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.ARROW, new int[]{8, 16, 32}, (short) 0, 0F);

        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[]{arrows}, Material.BOW, new int[]{1}, (short) 0, 0.2F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.ARROW_DAMAGE, 1).build(), new __INVALID__ChestItem[]{arrows}, Material.BOW, new int[]{1}, (short) 0, 0.1F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.ARROW_DAMAGE, 2).build(), new __INVALID__ChestItem[]{arrows}, Material.BOW, new int[]{1}, (short) 0, 0.05F));

        //ROD
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.FISHING_ROD, new int[]{1}, (short) 0, 0.30F));

        //SNOWBALLS/EGGS
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.EGG, new int[]{2, 4, 6}, (short) 0, 0.2F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.SNOW_BALL, new int[]{2, 4, 6}, (short) 0, 0.2F));

        //BUCKETS - USED AS A BLOCK TOOL
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.WATER_BUCKET, new int[]{1}, (short) 0, 0.2F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.LAVA_BUCKET, new int[]{1}, (short) 0, 0.2F));

        //FNS
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.OTHER_WEAPON, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.FLINT_AND_STEEL, new int[]{1}, (short) ((int) Material.FLINT_AND_STEEL.getMaxDurability() - 10), 0.20F));

        //Blocks
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.COBBLESTONE, new int[]{8, 16, 32, 64}, (short) 0, 0.40F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.LOG, new int[]{8, 16}, (short) 0, 0.20F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.BLOCK, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.WOOD, new int[]{8, 16, 32, 64}, (short) 0, 0.30F));

        //Food
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.FOOD, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.GRILLED_PORK, new int[]{4,8,16,32}, (short)0, 0.10F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.FOOD, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.PUMPKIN_PIE, new int[]{4,8,16,32}, (short)0, 0.10F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.FOOD, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.COOKED_BEEF, new int[]{4,8,16,32}, (short)0, 0.10F));
        chestItems.add(new __INVALID__ChestItem(__INVALID__ChestSlot.FOOD, new HashMap<Enchantment, Integer>(), new __INVALID__ChestItem[0], Material.GOLDEN_CARROT, new int[]{4,8,16,32}, (short)0, 0.5F));
    }

    public __INVALID__SpawnChestGeneration() {
        super(chestItems);
    }
}
