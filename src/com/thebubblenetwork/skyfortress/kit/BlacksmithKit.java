package com.thebubblenetwork.skyfortress.kit;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.game.kit.Kit;
import com.thebubblenetwork.api.global.java.ArrayBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BlacksmithKit extends Kit {
    private static ItemStackBuilder ANVIL = new ItemStackBuilder(Material.ANVIL).withName(ChatColor.GRAY + "A blacksmith's anvil");
    private static ItemStackBuilder IRON = new ItemStackBuilder(Material.IRON_INGOT).withAmount(8);
    private static ItemStackBuilder EXP = new ItemStackBuilder(Material.EXP_BOTTLE).withAmount(8);
    private static ItemStackBuilder ENCHANTEDBOOK1 = new ItemStackBuilder(Material.ENCHANTED_BOOK).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
    private static ItemStackBuilder ENCHANTEDBOOK2 = new ItemStackBuilder(Material.ENCHANTED_BOOK).withEnchantment(Enchantment.DAMAGE_ALL, 1);

    private static ArrayBuilder<ItemStack> DEFAULTBUILD = new ArrayBuilder<>(ItemStack.class,4*9)
            .withT(0,ANVIL.build())
            .withT(1, IRON.build())
            .withT(2, EXP.build())
            .withT(3, ENCHANTEDBOOK1.build())
            .withT(4, ENCHANTEDBOOK2.build());

    public BlacksmithKit() {
        super(Material.IRON_INGOT, Arrays.asList(
                DEFAULTBUILD.clone()
                .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(16).build())
                        .withT(2, EXP.clone().withAmount(16).build())
                        .withT(3, ENCHANTEDBOOK1.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2).build())
                        .withT(4, ENCHANTEDBOOK2.clone().withEnchantment(Enchantment.DAMAGE_ALL, 2).build())
                .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(24).build())
                        .withT(2, EXP.clone().withAmount(24).build())
                        .withT(3, ENCHANTEDBOOK1.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3).build())
                        .withT(4, ENCHANTEDBOOK2.clone().withEnchantment(Enchantment.DAMAGE_ALL, 3).build())
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(32).build())
                        .withT(2, EXP.clone().withAmount(32).build())
                        .withT(3, ENCHANTEDBOOK1.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).build())
                        .withT(4, ENCHANTEDBOOK2.clone().withEnchantment(Enchantment.DAMAGE_ALL, 4).build())
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(64).build())
                        .withT(2, EXP.clone().withAmount(32).build())
                        .withT(3, ENCHANTEDBOOK1.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).withEnchantment(Enchantment.THORNS, 1).build())
                        .withT(4, ENCHANTEDBOOK2.clone().withEnchantment(Enchantment.DAMAGE_ALL, 4).withEnchantment(Enchantment.FIRE_ASPECT, 1).build())
                        .build(),
                DEFAULTBUILD.clone()
                        .withT(1, IRON.clone().withAmount(64).build())
                        .withT(2, EXP.clone().withAmount(32).build())
                        .withT(3, ENCHANTEDBOOK1.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5).withEnchantment(Enchantment.THORNS, 3).build())
                        .withT(4, ENCHANTEDBOOK2.clone().withEnchantment(Enchantment.DAMAGE_ALL, 4).withEnchantment(Enchantment.FIRE_ASPECT, 1).build())
                        .build()
        )
                , "BlackSmith", new String[]{"Contains some iron, xp, an anvil and and some enchanted books", "Cost: 1000 Tokens"}, 1000);
    }
}
