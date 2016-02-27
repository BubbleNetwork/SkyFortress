package com.thebubblenetwork.skyfortress.map;

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
public class SkyFortressMap extends GameMap{
    public SkyFortressMap(String name, MapData data, File yml, File zip) {
        super(name, data, yml, zip);
    }

    public SkyFortressMap(){
        super();
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

        Set<LocationObject> guards = new HashSet<>();
        for(String guard:configurationSection.getConfigurationSection("guards.wither").getKeys(false)){
            guards.add(LocationUtil.fromConfig(configurationSection.getConfigurationSection("guards.wither." + guard)));
        }
        map.put("witherguards",guards);

        LocationObject crownlocation = LocationUtil.fromConfig(configurationSection.getConfigurationSection("crown"));
        map.put("crown",crownlocation);

        return map;
    }
    @SuppressWarnings("unchecked")
    public List<SkyIsland> getIslands(){
        return (List<SkyIsland>) getSettings().get("islands");
    }

    @SuppressWarnings("unchecked")
    public Set<LocationObject> getGuardLocations(){
        return (Set<LocationObject>) getSettings().get("witherguards");
    }

    @SuppressWarnings("unchecked")
    public LocationObject getCrownLocation(){
        return (LocationObject) getSettings().get("crown");
    }
}
