package com.thebubblenetwork.skyfortress.listener;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.crown.CapManager;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.map.SkyIsland;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyListener implements Listener{


    private SkyFortress fortress;

    public SkyListener(SkyFortress fortress){
        this.fortress = fortress;
    }

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
}
