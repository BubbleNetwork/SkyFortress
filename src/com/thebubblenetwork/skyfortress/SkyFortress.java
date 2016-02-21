package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.util.mc.scoreboard.BoardPreset;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.kit.KitManager;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import com.thebubblenetwork.skyfortress.kit.DefaultKit;
import com.thebubblenetwork.skyfortress.mobai.MobManager;
import com.thebubblenetwork.skyfortress.scoreboard.SkyFortressBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {14:36}
 * Created February 2016
 */
public class SkyFortress extends BubbleGameAPI{
    public static final int VERSION = 1;

    private static SkyFortress instance;

    public static SkyFortress getInstance() {
        return instance;
    }

    private SkyFortressBoard board = new SkyFortressBoard();
    private MobManager manager = new MobManager();

    public SkyFortress() {
        super("SkyFortress", GameMode.SURVIVAL, "Default Kit", 1);
        instance = this;
    }

    public void cleanup() {

    }

    public void onStateChange(State oldstate, State newstate) {
        if(newstate == State.LOBBY){
            KitManager.getKits().add(new DefaultKit());
        }
        if(newstate == State.RESTARTING){
            KitManager.getKits().remove(KitManager.getKit("Default Kit"));
        }
    }

    public BoardPreset getScorePreset() {
        return board;
    }

    public GameMap loadMap(String s, MapData mapData, File file, File file1) {
        return new SkyGameMap(s,mapData,file,file1);
    }

    public void teleportPlayers(GameMap gameMap, World world) {
        if(!(gameMap instanceof SkyGameMap))throw new IllegalArgumentException("Invalid map");
        SkyGameMap map = (SkyGameMap)gameMap;
        for(LocationObject object:map.getSpawns()){
            for(Player p: Bukkit.getOnlinePlayers()){
                p.teleport(object.toLocation(world));
            }
        }
    }

    public MobManager getManager() {
        return manager;
    }

    public SkyFortressBoard getBoard() {
        return board;
    }

    public long finishUp() {
        //TODO
        return Long.MAX_VALUE;
    }

    public int getVersion() {
        return VERSION;
    }
}
