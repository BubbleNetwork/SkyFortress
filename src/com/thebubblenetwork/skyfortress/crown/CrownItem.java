package com.thebubblenetwork.skyfortress.crown;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
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
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public abstract class CrownItem implements Listener {
    private Item item;
    private BukkitTask task;

    public CrownItem(ItemStack stack, final Location spawn) {
        ItemMeta meta = stack.getItemMeta();
        if (meta.getLore() == null || meta.getLore().size() == 0) {
            meta.setLore(Collections.singletonList("CrownMeta"));
        }
        stack.setItemMeta(meta);
        item = spawn.getWorld().dropItem(spawn, stack);
        item.teleport(spawn);
        task = new BubbleRunnable() {
            public void run() {
                item.teleport(spawn);
            }
        }.runTaskTimer(SkyFortress.getInstance(), TimeUnit.MILLISECONDS, 100);
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
        if (to.getWorld().getUID() == loc.getWorld().getUID() && inRange(to.getX(), loc.getX(), 0.5D) && inRange(to.getZ(), loc.getZ(), 0.5D) && to.getBlockY() == loc.getBlockY()) {
            if (pickup(e.getPlayer())) {
                cancel();
            }
        }
    }

    public abstract boolean pickup(Player p);

    public void cancel() {
        if (item != null) {
            item.teleport(new Location(Bukkit.getWorld("world"), 0, -1, 0));
            item.remove();
        }
        item = null;
        HandlerList.unregisterAll(this);
        try{
            task.cancel();
        }
        catch (Exception ex){
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        if (e.getEntity().getUniqueId() == getItem().getUniqueId()) {
            e.setCancelled(true);
        }
    }

    public Item getItem() {
        return item;
    }
}
