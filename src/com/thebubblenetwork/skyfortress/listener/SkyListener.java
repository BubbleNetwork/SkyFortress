package com.thebubblenetwork.skyfortress.listener;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.crown.CapManager;
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
    private Map<UUID,Boolean> kingqueenmap = new HashMap<>();

    public SkyListener(SkyFortress fortress){
        this.fortress = fortress;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        if(fortress.getState() == BubbleGameAPI.State.LOBBY){
            Player p = e.getPlayer();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        final Player died = e.getEntity();
        if(fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == died){
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
    }
}
