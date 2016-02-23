package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.BoardPreset;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.kit.KitManager;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import com.thebubblenetwork.skyfortress.chest.ChestGeneration;
import com.thebubblenetwork.skyfortress.chest.ChestType;
import com.thebubblenetwork.skyfortress.chest.util.SpawnChest;
import com.thebubblenetwork.skyfortress.crown.CrownItem;
import com.thebubblenetwork.skyfortress.kit.DefaultKit;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import com.thebubblenetwork.skyfortress.mobai.MobManager;
import com.thebubblenetwork.skyfortress.mobai.ai.WitherGuards;
import com.thebubblenetwork.skyfortress.scoreboard.SkyFortressBoard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
    private Set<ChestGeneration> pregens = new HashSet<>();
    private CrownItem item = null;
    private WitherGuards guards = null;

    public SkyFortress() {
        super("SkyFortress", GameMode.SURVIVAL, "Default Kit", 1);
        instance = this;
    }

    public void cleanup() {
        if(item != null)item.cancel();
        pregens.clear();
        for(CreatureAI ai:manager.getCreatureAIs()){
            ai.remove();
        }
    }

    public void onStateChange(State oldstate, State newstate) {
        if(oldstate == null){
            pregens.clear();
            for(int i = 0;i < getType().getMaxPlayers();i ++){
                pregens.add(new ChestGeneration(ChestType.SINGLE,new SpawnChest(),3));
            }
        }
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
        return new SkyFortressMap(s,mapData,file,file1);
    }

    public void teleportPlayers(GameMap gameMap, World world) {
        if(!(gameMap instanceof SkyFortressMap))throw new IllegalArgumentException("Invalid map");
        SkyFortressMap map = (SkyFortressMap)gameMap;
        Iterator<ChestGeneration> chestGenerationIterator = pregens.iterator();
        Iterator<? extends Player> playerIterator = Bukkit.getOnlinePlayers().iterator();
        for(SkyIsland island:map.getIslands()){
            if(!playerIterator.hasNext())break;
            if(!chestGenerationIterator.hasNext())throw new IllegalArgumentException("Not enough chestgens");
            Player p = playerIterator.next();
            ChestGeneration generation = chestGenerationIterator.next();
            island.fillChests(world,generation);
            p.teleport(island.getSpawn().toLocation(world));
        }
        guards = new WitherGuards(world,map.getGuardLocations());
        item = new CrownItem(new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL).build(),map.getCrownLocation().toLocation(world)) {
            public boolean pickup(Player p) {
                return !getGame().isSpectating(p);
            }
        };
        pregens.clear();
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
