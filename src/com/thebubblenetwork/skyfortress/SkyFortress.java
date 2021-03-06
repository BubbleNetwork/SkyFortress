package com.thebubblenetwork.skyfortress;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.player.BukkitBubblePlayer;
import com.thebubblenetwork.api.framework.plugin.util.BubbleRunnable;
import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.kit.KitManager;
import com.thebubblenetwork.api.game.maps.GameMap;
import com.thebubblenetwork.api.game.maps.MapData;
import com.thebubblenetwork.api.global.bubblepackets.messaging.messages.handshake.JoinableUpdate;
import com.thebubblenetwork.api.global.player.BubblePlayer;
import com.thebubblenetwork.skyfortress.chest.__INVALID__ChestType;
import com.thebubblenetwork.skyfortress.chest.PregeneratedChest;
import com.thebubblenetwork.skyfortress.chest.util.__INVALID__MiddleChestGeneration;
import com.thebubblenetwork.skyfortress.chest.util.__INVALID__SpawnChestGeneration;
import com.thebubblenetwork.skyfortress.crown.CapManager;
import com.thebubblenetwork.skyfortress.crown.CrownItem;
import com.thebubblenetwork.skyfortress.kit.BlacksmithKit;
import com.thebubblenetwork.skyfortress.kit.FarmerKit;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.map.SkyIsland;
import com.thebubblenetwork.skyfortress.newmobai.GuardManager;
import com.thebubblenetwork.skyfortress.scoreboard.SkyFortressBoard;
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
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
    private Set<PregeneratedChest> pregens = new ConcurrentSet<>();
    private PregeneratedChest middlechests;
    private CrownItem item = null;
    private GuardManager guards = null;
    private CapManager capManager = new CapManager();
    private Set<SkyIsland> islands = null;
    private SkyListener listener = new SkyListener(this);

    public SkyFortress() {
        super("SkyFortress", GameMode.SURVIVAL, "Farmer", 2);
        instance = this;
        board = new SkyFortressBoard();
        long millis = System.currentTimeMillis();
        middlechests = new PregeneratedChest(__INVALID__ChestType.SINGLE, new __INVALID__MiddleChestGeneration(), 30);
        for (int i = 0; i < getType().getMaxPlayers(); i++) {
            pregens.add(new PregeneratedChest(__INVALID__ChestType.SINGLE, new __INVALID__SpawnChestGeneration(), 3));
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
        if(newstate == State.INGAME){
            getGuards().spawnAll();
        }
        else if(newstate == State.ENDGAME){
            for(BubblePlayer player: BukkitBubblePlayer.getPlayerObjectMap().values()){
                Player p = (Player)player.getPlayer();
                p.sendMessage("");
                p.sendMessage(ChatColor.GRAY + "                <----------------Stats---------------->");
                p.sendMessage(ChatColor.GREEN + "You have won " + ChatColor.GRAY + (int)player.getStats(getType().getName(), "win") + ChatColor.GREEN + " SkyFortress games");
                p.sendMessage(ChatColor.GREEN + "You have killed " + ChatColor.GRAY + (int)player.getStats(getType().getName(), "kill") + ChatColor.GREEN + " players");
                p.sendMessage(ChatColor.GREEN + "You have assassinated the king " + ChatColor.GRAY + (int)player.getStats(getType().getName(), "kingkill") + ChatColor.GREEN + " times");
                p.sendMessage(ChatColor.GREEN + "You have slain " + ChatColor.GRAY + (int)player.getStats(getType().getName(), "guardkill") + ChatColor.GREEN + " guards");
                p.sendMessage(ChatColor.GREEN + "You have died " + ChatColor.GRAY + (int)player.getStats(getType().getName(), "death") + ChatColor.GREEN + " times");
                p.sendMessage(ChatColor.GRAY + "                <----------------Stats---------------->");
            }
        }
        else if (newstate == State.LOBBY) {
            if(oldstate == State.RESTARTING){
                getListener().getLoaded().clear();
                new BubbleRunnable(){
                    public void run() {
                        long start = System.currentTimeMillis();
                        middlechests = new PregeneratedChest(__INVALID__ChestType.SINGLE, new __INVALID__MiddleChestGeneration(), 30);
                        pregens.clear();
                        for (int i = 0; i < getType().getMaxPlayers(); i++) {
                            pregens.add(new PregeneratedChest(__INVALID__ChestType.SINGLE, new __INVALID__SpawnChestGeneration(), 3));
                        }
                        System.out.println("Doing chests took " + (System.currentTimeMillis()-start)/1000 + "s");
                    }
                }.runTaskAsynchonrously(this);
            }
        }
    }

    public SkyFortressBoard getScorePreset() {
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
        Collections.shuffle(map.getIslands());
        Iterator<PregeneratedChest> chestGenerationIterator = pregens.iterator();
        Iterator<? extends Player> playerIterator = Bukkit.getOnlinePlayers().iterator();
        for (SkyIsland island : map.getIslands()) {
            if (!chestGenerationIterator.hasNext()) {
                throw new IllegalArgumentException("Not enough chestgens");
            }
            PregeneratedChest generation = chestGenerationIterator.next();
            island.fillChests(world, generation);
            if (playerIterator.hasNext()) {
                Player p = playerIterator.next();
                island.setIfassigned(p);
                Location l = island.getSpawn().toLocation(world);
                l.setX(l.getBlockX() + 0.5D);
                l.setZ(l.getBlockZ() + 0.5D);
                l.setYaw(0F);
                p.teleport(l);
            }
        }
        Set<SkyIsland> islands = new HashSet<>();
        islands.addAll(map.getIslands());
        this.islands = islands;
        listener.getLoaded().addAll(map.getCordSet());
        pregens.clear();
        guards = new GuardManager(world, map.getGuardLocations());
        world.setSpawnLocation((int)map.getCrownLocation().getX(), (int)map.getCrownLocation().getY(), (int)map.getCrownLocation().getZ());
        world.setKeepSpawnInMemory(true);
        resetCrown();
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

    @Override
    public void win(Player p){
        BukkitBubblePlayer player = BukkitBubblePlayer.getObject(p.getUniqueId());
        player.setTokens(player.getTokens() + 500);
        p.sendMessage(ChatColor.GOLD + "+500 Tokens (You now have " + ChatColor.RED + player.getTokens() + ChatColor.GOLD + ")");
        super.win(p);
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

    @Override
    public void onEnable() {
        super.onEnable();
        registerListener(getListener());
        KitManager.getKits().add(new FarmerKit());
        KitManager.getKits().add(new BlacksmithKit());
    }

    public CraftArmorStand spawnHologram(Location l, String text){
        CraftArmorStand stand = (CraftArmorStand) l.getWorld().spawn(l,ArmorStand.class);
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
