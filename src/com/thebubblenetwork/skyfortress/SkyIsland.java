package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.skyfortress.chest.ChestGeneration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import java.util.Set;
import java.util.logging.Level;

public class SkyIsland {
    private LocationObject spawn;
    private Set<LocationObject> chests;

    public SkyIsland(Set<LocationObject> chests, LocationObject spawn) {
        this.chests = chests;
        this.spawn = spawn;
    }

    public LocationObject getSpawn() {
        return spawn;
    }

    public Set<LocationObject> getChests() {
        return chests;
    }

    public void fillChests(World world, ChestGeneration generation){
        for(LocationObject object:getChests()){
            try {
                Block b = object.toLocation(world).getBlock();
                if (b != null && b.getType() == Material.CHEST) {
                    Chest c = (Chest)b.getState();
                    generation.apply(c);
                }
                throw new IllegalArgumentException("An island was missing a chest");
            }
            catch (Throwable throwable){
                Bukkit.getLogger().log(Level.SEVERE,"An error occurred whilst filling chests in an island",throwable);
            }
        }
    }
}
