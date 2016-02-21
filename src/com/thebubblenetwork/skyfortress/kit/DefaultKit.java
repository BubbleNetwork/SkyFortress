package com.thebubblenetwork.skyfortress.kit;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.game.kit.Kit;
import com.thebubblenetwork.api.global.java.ArrayBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {17:50}
 * Created February 2016
 */
public class DefaultKit extends Kit{
    public static ChatColor COLOR = ChatColor.GRAY;

    public static ItemStackBuilder SWORD = new ItemStackBuilder(Material.WOOD_SWORD).withName(COLOR + "A default sword");
    public static ItemStackBuilder SHOVEL = new ItemStackBuilder(Material.WOOD_SPADE).withName(COLOR + "A default shovel");
    public static ItemStackBuilder PICK = new ItemStackBuilder(Material.WOOD_PICKAXE).withName(COLOR + "A default pick");
    public static ItemStackBuilder STEAK = new ItemStackBuilder(Material.GRILLED_PORK).withAmount(5);
    public static ItemStackBuilder HELM = new ItemStackBuilder(Material.LEATHER_HELMET).withName(COLOR + "A default helm");
    public static ItemStackBuilder CHEST = new ItemStackBuilder(Material.LEATHER_CHESTPLATE).withName(COLOR + "A default chestplate");
    public static ItemStackBuilder PANTS = new ItemStackBuilder(Material.LEATHER_LEGGINGS).withName(COLOR + "Some default pants");
    public static ItemStackBuilder BOOTS = new ItemStackBuilder(Material.LEATHER_BOOTS).withName(COLOR + "Some default boots");

    public static ArrayBuilder<ItemStack> DEFAULTBUILD =
            newBuilder(4*9)
                    .withT(0,SWORD.build())
                    .withT(1,SHOVEL.build())
                    .withT(2,PICK.build())
                    .withT(3,STEAK.build());

    public static ArrayBuilder<ItemStack> DEFAULTARMORBUILD =
            newBuilder(8)
                    .withT(5,HELM.build())
                    .withT(6,CHEST.build())
                    .withT(7,PANTS.build())
                    .withT(8,BOOTS.build());

    public DefaultKit() {
        super(Material.STAINED_GLASS_PANE,
                Arrays.asList(
                        DEFAULTBUILD
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).build())
                                .withT(3,STEAK.clone().withAmount(10).build())
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).build())
                                .withT(1,SHOVEL.clone().withType(Material.STONE_SPADE).build())
                                .withT(2,PICK.clone().withType(Material.STONE_PICKAXE).build())
                                .withT(3,STEAK.clone().withAmount(20).build())
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(1,SHOVEL.clone().withType(Material.STONE_SPADE).build())
                                .withT(2,PICK.clone().withType(Material.STONE_PICKAXE).build())
                                .withT(3,STEAK.clone().withAmount(20).build())
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(1,SHOVEL.clone().withType(Material.STONE_SPADE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(2,PICK.clone().withType(Material.STONE_PICKAXE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(3,STEAK.clone().withAmount(20).build())
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(1,SHOVEL.clone().withType(Material.STONE_SPADE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(2,PICK.clone().withType(Material.STONE_PICKAXE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(3,STEAK.clone().withAmount(20).build())
                                .withT(4,new ItemStackBuilder(Material.GOLDEN_APPLE).build())
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(1,SHOVEL.clone().withType(Material.STONE_SPADE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(2,PICK.clone().withType(Material.STONE_PICKAXE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(3,new ItemStackBuilder(Material.GOLDEN_CARROT).withAmount(10).build())
                                .withT(4,new ItemStackBuilder(Material.GOLDEN_APPLE).withAmount(4).build())
                                .build(),
                        DEFAULTBUILD
                                .withT(0,SWORD.clone().withType(Material.STONE_SWORD).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(1,SHOVEL.clone().withType(Material.STONE_SPADE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(2,PICK.clone().withType(Material.STONE_PICKAXE).withEnchantment(Enchantment.DAMAGE_ALL,1).build())
                                .withT(3,new ItemStackBuilder(Material.GOLDEN_CARROT).withAmount(20).build())
                                .withT(4,new ItemStackBuilder(Material.GOLDEN_APPLE).withAmount(4).build())
                                .build()
                )
                ,
                Arrays.asList(
                        DEFAULTARMORBUILD
                                .build(),
                        DEFAULTARMORBUILD.clone()
                                .withT(5,HELM.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(8,BOOTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .build(),
                        DEFAULTARMORBUILD.clone()
                                .withT(5,HELM.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(7,PANTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(8,BOOTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .build(),
                        DEFAULTARMORBUILD.clone()
                                .withT(5,HELM.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(6,CHEST.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(7,PANTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(8,BOOTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .build(),
                        DEFAULTARMORBUILD.clone()
                                .withT(5,HELM.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .withT(6,CHEST.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(7,PANTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(8,BOOTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .build(),
                        DEFAULTARMORBUILD.clone()
                                .withT(5,HELM.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .withT(6,CHEST.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,1).build())
                                .withT(7,PANTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .withT(8,BOOTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .build(),
                        DEFAULTARMORBUILD.clone()
                                .withT(5,HELM.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .withT(6,CHEST.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .withT(7,PANTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .withT(8,BOOTS.clone().withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2).build())
                                .build()
                ), "Default Kit", new String[]{"Consists of leather armor and a set of tools"}, 7, 200);
    }

    public static ArrayBuilder<ItemStack> newBuilder(int size){
        return new ArrayBuilder<>(ItemStack.class,size);
    }
}
