package com.thebubblenetwork.skyfortress.listener;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.framework.util.mc.menu.BuyInventory;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class KingQueenMenu extends BuyInventory{
    public KingQueenMenu() {
        super(9, ChatColor.BLUE + "King" + ChatColor.YELLOW + " or " + ChatColor.LIGHT_PURPLE + "Queen",
                KingQueenMenu.class
                , new int[]{2}, new int[]{6},
                new ItemStackBuilder(Material.STAINED_GLASS_PANE).withColor(DyeColor.BLUE).withName(ChatColor.BLUE + ChatColor.BOLD.toString() + "King"),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE).withColor(DyeColor.PINK).withName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Queen"),
                new ItemStackBuilder(Material.STAINED_GLASS_PANE).withColor(DyeColor.WHITE)
        );
    }

    public void onCancel(Player player) {
        //Queen
    }

    public void onAllow(Player player) {
        //King
    }
}
