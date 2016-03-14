package com.thebubblenetwork.skyfortress.crown;

import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CrownItem implements Listener {
    private Item item;
    private CraftArmorStand stand;

    public CrownItem(ItemStack stack, final Location spawn) {
        SkyListener.BYPASS = true;
        item = spawn.getWorld().dropItem(spawn,stack);
        SkyListener.BYPASS = false;
        stand = SkyFortress.getInstance().spawnHologram(spawn,ChatColor.GOLD + ChatColor.BOLD.toString() + "Crown");
        item.teleport(spawn);
        item.setPassenger(stand);
        item.setCustomName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Crown");
        item.setCustomNameVisible(false);
        SkyFortress.getInstance().registerListener(this);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (e.getItem().getUniqueId() == getItem().getUniqueId()) {
            e.setCancelled(true);
        }
    }

    public boolean inRange(double d1, double d2, double range) {
        double diff = d1 - d2;
        return diff < range && diff > -range;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location loc = getItem().getLocation();
        if (to.getWorld().getUID() == loc.getWorld().getUID() && inRange(to.getX(), loc.getX(), 0.5D) && inRange(to.getZ(), loc.getZ(), 0.5D) && inRange(to.getY(), loc.getY(), 1.0D)) {
            if (pickup(e.getPlayer())) {
                cancel();
            }
        }
    }

    public abstract boolean pickup(Player p);

    public void cancel() {
        if (item != null) {
            item.setPassenger(null);
            item.remove();
        }
        if(stand != null){
            stand.remove();
        }
        stand = null;
        item = null;
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        if (e.getEntity().getUniqueId() == getItem().getUniqueId()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent e){
        if(e.getEntity().getUniqueId() == getItem().getUniqueId() || e.getTarget().getUniqueId() == getItem().getUniqueId()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemBurn(EntityCombustEvent e){
        if(e.getEntity().getUniqueId() == getItem().getUniqueId()){
            e.setCancelled(true);
        }
    }

    public Item getItem() {
        return item;
    }
}
