package com.thebubblenetwork.skyfortress.kit;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.game.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 07/04/2016 {18:20}
 * Created April 2016
 */
public class KnightKit extends Kit{
    private static final ItemStackBuilder SWORD = new ItemStackBuilder(Material.IRON_SWORD).withName(ChatColor.DARK_AQUA + "The Knight's trusty blade");

    public KnightKit() {
        super(Material.IRON_SWORD, Arrays.asList(

        ), ChatColor.DARK_AQUA + "Knight", new String[]{
                "Kill your enemies in the awesome Knight Kit"
        }, 3000);
    }
}
