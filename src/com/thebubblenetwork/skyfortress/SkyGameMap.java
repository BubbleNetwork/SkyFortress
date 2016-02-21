package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.util.mc.config.LocationUtil;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {15:30}
 * Created February 2016
 */
public class SkyGameMap extends GameMap{
    public SkyGameMap(String name, MapData data, File yml, File zip) {
        super(name, data, yml, zip);
    }

    @SuppressWarnings("unchecked")
    public Map loadSetting(ConfigurationSection configurationSection) {
        Map map = new HashMap<>();
        Set<LocationObject> spawns = new HashSet<>();
        for(String section:configurationSection.getConfigurationSection("spawns").getKeys(false)){
            spawns.add(LocationUtil.fromConfig(configurationSection.getConfigurationSection("spawns." + section)));
        }
        LocationObject crownlocation = LocationUtil.fromConfig(configurationSection.getConfigurationSection("crown"));
        map.put("spawns",spawns);
        map.put("crown",crownlocation);
        return map;
    }

    @SuppressWarnings("unchecked")
    public Set<LocationObject> getSpawns(){
        return (Set<LocationObject>) getSettings().get("spawns");
    }

    @SuppressWarnings("unchecked")
    public LocationObject getCrownLocation(){
        return (LocationObject) getSettings().get("crown");
    }
}
