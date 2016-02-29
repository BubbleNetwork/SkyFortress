package com.thebubblenetwork.skyfortress.map;

import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.skyfortress.chest.PregeneratedChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.logging.Level;

public class SkyIsland {
    private LocationObject spawn;
    private Set<LocationObject> chests;
    private Player ifassigned;

    public SkyIsland(Set<LocationObject> chests, LocationObject spawn) {
        this.chests = chests;
        this.spawn = spawn;
    }

    public LocationObject getSpawn() {
        return spawn;
    }

    public Player getIfassigned() {
        return ifassigned;
    }

    public void setIfassigned(Player ifassigned) {
        this.ifassigned = ifassigned;
    }

    public Set<LocationObject> getChests() {
        return chests;
    }

    public void fillChests(World world, PregeneratedChest generation){
        for(LocationObject object:getChests()){
            try {
                Block b = object.toLocation(world).getBlock();
                if (b != null && b.getType() == Material.CHEST) {
                    Chest c = (Chest)b.getState();
                    generation.apply(c);
                }
                else throw new IllegalArgumentException("An island was missing a chest at " + object.toString());
            }
            catch (Throwable throwable){
                Bukkit.getLogger().log(Level.SEVERE,"An error occurred whilst filling chests",throwable);
            }
        }
    }
}
