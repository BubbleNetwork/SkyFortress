package com.thebubblenetwork.skyfortress.kit;

import com.google.common.collect.ImmutableMap;
import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.game.kit.Kit;
import com.thebubblenetwork.api.global.java.ArrayBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;
import java.util.Map;

public class BlacksmithKit extends Kit {
    private static ItemStackBuilder ANVIL = new ItemStackBuilder(Material.ANVIL).withName(ChatColor.GRAY + "A blacksmith's anvil");
    private static ItemStackBuilder IRON = new ItemStackBuilder(Material.IRON_INGOT).withAmount(8);
    private static ItemStackBuilder EXP = new ItemStackBuilder(Material.EXP_BOTTLE).withAmount(6);
    private static ItemStackBuilder CRAFTINGBENCH = new ItemStackBuilder(Material.WORKBENCH).withName(ChatColor.GRAY + "A crafting bench");

    private static ItemStack getEnchantmentBook(Map<Enchantment,Integer> enchantmentIntegerMap){
        ItemStack stack = new ItemStackBuilder(Material.ENCHANTED_BOOK).build();
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)stack.getItemMeta();
        for(Map.Entry<Enchantment,Integer> entry: enchantmentIntegerMap.entrySet()){
            if(!meta.hasConflictingStoredEnchant(entry.getKey()))meta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    private static ArrayBuilder<ItemStack> DEFAULTBUILD = new ArrayBuilder<>(ItemStack.class,4*9)
            .withT(0,ANVIL.build())
            .withT(1, IRON.build())
            .withT(2, EXP.build())
            .withT(3, getEnchantmentBook(new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.PROTECTION_ENVIRONMENTAL, 2).put(Enchantment.DAMAGE_ALL, 1).build()))
            .withT(5, CRAFTINGBENCH.build());

    public BlacksmithKit() {
        super(Material.IRON_INGOT, Arrays.asList(
                DEFAULTBUILD.clone()
                .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(12).build())
                        .withT(2, EXP.clone().withAmount(14).build())
                        .withT(3, getEnchantmentBook(new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.PROTECTION_ENVIRONMENTAL, 2).put(Enchantment.DAMAGE_ALL, 1).build()))
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(16).build())
                        .withT(2, EXP.clone().withAmount(18).build())
                        .withT(3, getEnchantmentBook(new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.PROTECTION_ENVIRONMENTAL, 4).put(Enchantment.DAMAGE_ALL, 2).put(Enchantment.THORNS, 1).build()))
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(20).build())
                        .withT(2, EXP.clone().withAmount(22).build())
                        .withT(3, getEnchantmentBook(new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.PROTECTION_ENVIRONMENTAL, 3).put(Enchantment.DAMAGE_ALL, 2).put(Enchantment.THORNS, 1).put(Enchantment.ARROW_DAMAGE, 1).build()))
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(24).build())
                        .withT(2, EXP.clone().withAmount(26).build())
                        .withT(3, getEnchantmentBook(new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.PROTECTION_ENVIRONMENTAL, 4).put(Enchantment.DAMAGE_ALL, 2).put(Enchantment.THORNS, 1).put(Enchantment.ARROW_DAMAGE, 2).build()))
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(32).build())
                        .withT(2, EXP.clone().withAmount(30).build())
                        .withT(3, getEnchantmentBook(new ImmutableMap.Builder<Enchantment, Integer>().put(Enchantment.PROTECTION_ENVIRONMENTAL, 5).put(Enchantment.DAMAGE_ALL, 3).put(Enchantment.THORNS, 2).put(Enchantment.ARROW_DAMAGE, 3).build()))
                        .build()
        )
                , "BlackSmith", new String[]{"Contains some iron, xp, an anvil and and some enchanted books"}, 1000);
    }
}
