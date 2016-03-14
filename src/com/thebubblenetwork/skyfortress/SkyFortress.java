package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.BoardPreset;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.kit.KitManager;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake.JoinableUpdate;
import com.thebubblenetwork.api.global.file.FileUTIL;
import com.thebubblenetwork.skyfortress.chest.ChestType;
import com.thebubblenetwork.skyfortress.chest.PregeneratedChest;
import com.thebubblenetwork.skyfortress.chest.util.MiddleChestGeneration;
import com.thebubblenetwork.skyfortress.chest.util.SpawnChestGeneration;
import com.thebubblenetwork.skyfortress.crown.CapManager;
import com.thebubblenetwork.skyfortress.crown.CrownItem;
import com.thebubblenetwork.skyfortress.kit.DefaultKit;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.map.SkyIsland;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import com.thebubblenetwork.skyfortress.mobai.ai.GuardManager;
import com.thebubblenetwork.skyfortress.scoreboard.SkyFortressBoard;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {14:36}
 * Created February 2016
 */
public class SkyFortress extends BubbleGameAPI {
    private static Field invulnerable;

    static {
        try{
            invulnerable = net.minecraft.server.v1_8_R3.Entity.class.getDeclaredField("invulnerable");
            invulnerable.setAccessible(true);
        }
        catch (Exception ex){
            BubbleNetwork.getInstance().getLogger().log(Level.WARNING,"Could not setup invunerable field",ex);
        }
    }

    public static final int VERSION = 1;

    public static SkyFortress getInstance() {
        return instance;
    }

    private static SkyFortress instance;
    private SkyFortressBoard board;
    private Set<PregeneratedChest> pregens = new HashSet<>();
    private PregeneratedChest middlechests;
    private CrownItem item = null;
    private GuardManager guards = null;
    private CapManager capManager = new CapManager();
    private Set<SkyIsland> islands = null;
    private SkyListener listener = new SkyListener(this);

    public SkyFortress() {
        super("SkyFortress", GameMode.SURVIVAL, "Farmer", 1);
        instance = this;
        board = new SkyFortressBoard();
        long millis = System.currentTimeMillis();
        middlechests = new PregeneratedChest(ChestType.SINGLE, new MiddleChestGeneration(), 30);
        for (int i = 0; i < getType().getMaxPlayers(); i++) {
            pregens.add(new PregeneratedChest(ChestType.SINGLE, new SpawnChestGeneration(), 3));
        }
        long diff = System.currentTimeMillis() - millis;
        BubbleNetwork.getInstance().getPlugin().getLogger().log(Level.INFO, "Genning chests took {0}seconds", (double) diff / 1000D);
        capManager = new CapManager();
        listener = new SkyListener(this);
    }

    public void cleanup() {
        if (item != null) {
            item.cancel();
        }
        pregens.clear();
        if(item != null)item.cancel();
        if(capManager.isCapped())capManager.endCap();
        if(guards != null)guards.deleteAll();
    }

    public void onStateChange(State oldstate, State newstate) {
        try {
            BubbleNetwork.getInstance().getPacketHub().sendMessage(BubbleNetwork.getInstance().getProxy(), new JoinableUpdate(newstate == State.LOBBY));
        } catch (IOException e) {
            BubbleNetwork.getInstance().getPlugin().getLogger().log(Level.WARNING, "Could not send joinable update for skyfortress", e);
        }
        if(newstate == State.HIDDEN){
            registerListener(getListener());
        }
        if (newstate == State.LOBBY) {
            KitManager.getKits().add(new DefaultKit());
        }
        if (newstate == State.RESTARTING) {
            KitManager.getKits().clear();
        }
    }

    public BoardPreset getScorePreset() {
        return board;
    }

    public GameMap loadMap(String s, MapData mapData, File file, File file1) {
        return new SkyFortressMap(s, mapData, file, file1);
    }

    public void teleportPlayers(GameMap gameMap, World world) {
        if (!(gameMap instanceof SkyFortressMap)) {
            throw new IllegalArgumentException("Invalid map");
        }
        SkyFortressMap map = (SkyFortressMap) gameMap;
        Iterator<PregeneratedChest> chestGenerationIterator = pregens.iterator();
        Iterator<? extends Player> playerIterator = Bukkit.getOnlinePlayers().iterator();
        for (SkyIsland island : map.getIslands()) {
            if (!playerIterator.hasNext()) {
                break;
            }
            if (!chestGenerationIterator.hasNext()) {
                throw new IllegalArgumentException("Not enough chestgens");
            }
            Player p = playerIterator.next();
            PregeneratedChest generation = chestGenerationIterator.next();
            island.fillChests(world, generation);
            island.setIfassigned(p);
            Location l = island.getSpawn().toLocation(world);
            l.setX(l.getBlockX() + 0.5D);
            l.setZ(l.getBlockZ() + 0.5D);
            l.setYaw(0F);
            l.setPitch(l.toVector().angle(((SkyFortressMap) gameMap).getCrownLocation().toLocation(world).toVector()));
            p.teleport(l);
        }
        Set<SkyIsland> islands = new HashSet<>();
        islands.addAll(map.getIslands());
        this.islands = islands;
        listener.getLoaded().addAll(map.getCordSet());
        resetCrown();
        pregens.clear();
        guards = new GuardManager(world, map.getGuardLocations());
    }

    public SkyIsland getIfAssigned(Player p) {
        if (islands == null) {
            throw new IllegalArgumentException("Islands are null");
        }
        for (SkyIsland island : islands) {
            if (island.getIfassigned() == p) {
                return island;
            }
        }
        return null;
    }

    public PregeneratedChest getMiddlechests() {
        return middlechests;
    }

    public SkyFortressBoard getBoard() {
        return board;
    }

    public GuardManager getGuards() {
        return guards;
    }

    public CapManager getCapManager() {
        return capManager;
    }

    public long finishUp() {
        //TODO
        return Long.MAX_VALUE;
    }

    public void resetCrown() {
        if (item != null) {
            item.cancel();
        }
        item = new CrownItem(new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL).build(), ((SkyFortressMap) getChosenGameMap()).getCrownLocation().toLocation(getChosen())) {
            public boolean pickup(Player p) {
                if (!getGame().isSpectating(p)) {
                    if (!getCapManager().isCapped()) {
                        getCapManager().startCap(p);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public CraftArmorStand spawnHologram(Location l,String text){
        SkyListener.BYPASS = true;
        CraftArmorStand stand = (CraftArmorStand) l.getWorld().spawn(l,ArmorStand.class);
        SkyListener.BYPASS = false;
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setSmall(true);
        stand.setCustomNameVisible(true);
        stand.setCustomName(text);
        stand.getHandle().setSize(0F,0F);
        try {
            invulnerable.set(stand.getHandle(),true);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        return stand;
    }

    public SkyListener getListener() {
        return listener;
    }

    public CrownItem getItem() {
        return item;
    }

    public int getVersion() {
        return VERSION;
    }
}
