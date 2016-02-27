package com.thebubblenetwork.skyfortress.crown;

import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public abstract class CrownItem implements Listener{
    private Item item;

    public CrownItem (ItemStack stack, Location spawn){
        ItemMeta meta = stack.getItemMeta();
        if(meta.getLore() == null || meta.getLore().size() == 0){
            meta.setLore(Collections.singletonList("CrownMeta"));
        }
        stack.setItemMeta(meta);
        item = spawn.getWorld().dropItem(spawn,stack);
        SkyFortress.getInstance().registerListener(this);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        if(e.getItem().getUniqueId() == getItem().getUniqueId()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Location to = e.getTo();
        Location loc = getItem().getLocation();
        if(to.getWorld() == loc.getWorld() && to.getBlockX() == loc.getBlockX() && to.getBlockZ() == loc.getBlockZ() && to.getBlockY() == loc.getBlockY()){
            if(pickup(e.getPlayer())) {
                cancel();
            }
        }
    }

    public abstract boolean pickup(Player p);

    public void cancel(){
        if(item != null){
            item.teleport(new Location(Bukkit.getWorld("world"),0,-1,0));
            item.remove();
        }
        item = null;
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e){
        if(e.getEntity().getUniqueId() == getItem().getUniqueId())e.setCancelled(true);
    }

    public Item getItem() {
        return item;
    }
}
