package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.util.mc.config.LocationUtil;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.*;

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

        List<SkyIsland> islands = new ArrayList<>();
        for(String island:configurationSection.getConfigurationSection("islands").getKeys(false)){
            String section = "islands." + island;
            LocationObject spawn = LocationUtil.fromConfig(configurationSection.getConfigurationSection(section + ".spawn"));
            Set<LocationObject> chests = new HashSet<>();
            for(String chest:configurationSection.getConfigurationSection(section + ".chests").getKeys(false)){
                chests.add(LocationUtil.fromConfig(configurationSection.getConfigurationSection(section + ".chests." + chest)));
            }
            islands.add(new SkyIsland(chests,spawn));
        }
        map.put("islands",islands);

        LocationObject crownlocation = LocationUtil.fromConfig(configurationSection.getConfigurationSection("crown"));
        map.put("crown",crownlocation);

        return map;
    }
    @SuppressWarnings("unchecked")
    public List<SkyIsland> getIslands(){
        return (List<SkyIsland>) getSettings().get("islands");
    }

    @SuppressWarnings("unchecked")
    public LocationObject getCrownLocation(){
        return (LocationObject) getSettings().get("crown");
    }
}
