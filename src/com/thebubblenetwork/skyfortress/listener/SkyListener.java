package com.thebubblenetwork.skyfortress.listener;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.map.Cord;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.map.SkyIsland;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashSet;
import java.util.Set;

public class SkyListener implements Listener{


    private SkyFortress fortress;

    public SkyListener(SkyFortress fortress){
        this.fortress = fortress;
    }

    private Set<Cord> loaded = new HashSet<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        final Player died = e.getEntity();
        if(fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == died){
            e.setDeathMessage(null);
            e.setKeepInventory(true);
            e.setKeepLevel(true);
            fortress.getCapManager().endCap(true);
            final World w = died.getWorld();
            new BubbleRunnable(){
                public void run() {
                    died.spigot().respawn();
                    SkyIsland island = fortress.getIfAssigned(died);
                    if(island != null){
                        Location location = island.getSpawn().toLocation(w);
                        Block b = location.getBlock().getRelative(BlockFace.DOWN);
                        if(!b.getType().isSolid())b.setType(Material.DIRT);
                        died.teleport(location);
                    }
                }
            }.runTask(SkyFortress.getInstance());
        }
        else{
            e.setKeepInventory(false);
            e.setKeepLevel(false);
            e.setNewExp(0);
            final Location l =
                    died.getLocation().getBlockY() > 0
                    ?
                    died.getLocation()
                    :
                    ((SkyFortressMap)SkyFortress.getInstance().getChosenGameMap()).getCrownLocation().toLocation(SkyFortress.getInstance().getChosen());
            new BubbleRunnable(){
                public void run() {
                    died.spigot().respawn();
                    SkyFortress.getInstance().getGame().setSpectating(died,true);
                    died.teleport(l);
                }
            }.runTask(SkyFortress.getInstance());
        }
    }

    public Set<Cord> getLoaded() {
        return loaded;
    }

    public boolean isContained(Cord c){
        for(Cord cord:getLoaded()){
            if(cord.equals(c))return true;
        }
        return false;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        switch (e.getSpawnReason()){
            case DEFAULT:
            case NATURAL:
            case LIGHTNING:
            case CHUNK_GEN:
            case NETHER_PORTAL:
            case SPAWNER:
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreOpenChest(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block b = e.getClickedBlock();
            if(b != null){
                if(b.getType() == Material.CHEST){
                    Cord c = Cord.fromBlock(b);
                    if(!isContained(c)) {
                        if(fortress.getMiddlechests().getUses() < 1){
                            fortress.getMiddlechests().gen(1);
                        }
                        fortress.getMiddlechests().apply((InventoryHolder) b.getState());
                        loaded.add(c);
                    }
                }
            }
        }
    }
}
