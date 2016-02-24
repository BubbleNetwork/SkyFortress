package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.BoardPreset;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.kit.KitManager;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import com.thebubblenetwork.skyfortress.chest.PregeneratedChest;
import com.thebubblenetwork.skyfortress.chest.ChestType;
import com.thebubblenetwork.skyfortress.chest.util.SpawnChestGeneration;
import com.thebubblenetwork.skyfortress.crown.CapManager;
import com.thebubblenetwork.skyfortress.crown.CrownItem;
import com.thebubblenetwork.skyfortress.kit.DefaultKit;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.map.SkyIsland;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import com.thebubblenetwork.skyfortress.mobai.MobManager;
import com.thebubblenetwork.skyfortress.mobai.ai.WitherGuardManager;
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

    private SkyFortressBoard board;
    private MobManager mobManager;
    private Set<PregeneratedChest> pregens = new HashSet<>();
    private CrownItem item = null;
    private WitherGuardManager guards = null;
    private CapManager capManager;
    private Set<SkyIsland> islands = null;
    private SkyListener listener;

    public SkyFortress() {
        super("SkyFortress", GameMode.SURVIVAL, "Default Kit", 1);
        instance = this;
    }

    public void cleanup() {
        if(item != null)item.cancel();
        pregens.clear();
        for(CreatureAI ai: mobManager.getCreatureAIs()){
            ai.remove();
        }
    }

    public void onStateChange(State oldstate, State newstate) {
        if(oldstate == null){
            listener = new SkyListener();
            capManager = new CapManager();
            mobManager = new MobManager();
            board = new SkyFortressBoard();
            pregens.clear();
            for(int i = 0;i < getType().getMaxPlayers();i ++){
                pregens.add(new PregeneratedChest(ChestType.SINGLE,new SpawnChestGeneration(),3));
            }
        }
        if(newstate == State.LOBBY){
            KitManager.getKits().add(new DefaultKit());
        }
        if(newstate == State.RESTARTING){
            KitManager.getKits().clear();
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
        Iterator<PregeneratedChest> chestGenerationIterator = pregens.iterator();
        Iterator<? extends Player> playerIterator = Bukkit.getOnlinePlayers().iterator();
        for(SkyIsland island:map.getIslands()){
            if(!playerIterator.hasNext())break;
            if(!chestGenerationIterator.hasNext())throw new IllegalArgumentException("Not enough chestgens");
            Player p = playerIterator.next();
            PregeneratedChest generation = chestGenerationIterator.next();
            island.fillChests(world,generation);
            island.setIfassigned(p);
            p.teleport(island.getSpawn().toLocation(world));
        }
        Set<SkyIsland> islands = new HashSet<>();
        islands.addAll(map.getIslands());
        this.islands = islands;
        guards = new WitherGuardManager(world,map.getGuardLocations());
        resetCrown();
        pregens.clear();
    }

    public SkyIsland getIfAssigned(Player p){
        if(islands == null)throw new IllegalArgumentException("Islands are null");
        for(SkyIsland island:islands){
            if(island.getIfassigned() == p)return island;
        }
        return null;
    }

    public MobManager getMobManager() {
        return mobManager;
    }

    public SkyFortressBoard getBoard() {
        return board;
    }

    public WitherGuardManager getGuards() {
        return guards;
    }

    public CapManager getCapManager() {
        return capManager;
    }

    public long finishUp() {
        //TODO
        return Long.MAX_VALUE;
    }

    public void resetCrown(){
        if(item != null){
            item.cancel();
        }
        item = new CrownItem(new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL).build(),((SkyFortressMap)getChosenGameMap()).getCrownLocation().toLocation(getChosen())){
            public boolean pickup(Player p) {
                if(!getGame().isSpectating(p)){
                    if(!getCapManager().isCapped()){
                        getCapManager().startCap(p);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public CrownItem getItem() {
        return item;
    }

    public int getVersion() {
        return VERSION;
    }
}
