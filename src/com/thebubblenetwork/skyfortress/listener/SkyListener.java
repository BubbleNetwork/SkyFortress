package com.thebubblenetwork.skyfortress.listener;

import com.thebubblenetwork.api.framework.player.BukkitBubblePlayer;
import com.thebubblenetwork.api.framework.plugin.util.BubbleRunnable;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.scoreboard.GameBoard;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.map.Cord;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.map.SkyIsland;
import com.thebubblenetwork.skyfortress.newmobai.PigmanGuard;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class SkyListener implements Listener {

    public static boolean BYPASS = false;

    private SkyFortress fortress;
    private Set<Cord> loaded = new HashSet<>();

    public SkyListener(SkyFortress fortress) {
        this.fortress = fortress;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        final Player died = e.getEntity();
        died.setNoDamageTicks(60);
        if (fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == died) {
            e.setDeathMessage(null);
            e.setKeepInventory(true);
            e.setKeepLevel(true);
            fortress.getCapManager().endCap();
            final World w = died.getWorld();
            new BubbleRunnable() {
                public void run() {
                    if(died.isOnline()) {
                        died.spigot().respawn();
                        SkyIsland island = fortress.getIfAssigned(died);
                        if (island != null) {
                            Location location = island.getSpawn().toLocation(w);
                            Block b = location.getBlock().getRelative(BlockFace.DOWN);
                            if (!b.getType().isSolid()) {
                                b.setType(Material.DIRT);
                            }
                            died.teleport(location);
                        }
                    }
                }
            }.runTask(SkyFortress.getInstance());
            String killer;
            if(died.getKiller() == null){
                killer = "PvE";
            }
            else{
                Player killplayer = died.getKiller();
                BukkitBubblePlayer player = BukkitBubblePlayer.getObject(killplayer.getUniqueId());
                killer = player.getNickName();
                player.incrementStat(SkyFortress.getInstance().getType().getName(), "kingkill", 1);
                killplayer.sendMessage(ChatColor.GOLD + "You killed the reining king! You have now assassinated " + ChatColor.RED + player.getStats(SkyFortress.getInstance().getType().getName(), "kingkill") + ChatColor.GOLD + " kings");
            }
            Bukkit.broadcastMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "King " + ChatColor.YELLOW + died.getName() + ChatColor.BLUE + " was assassinated by " + ChatColor.BLUE + killer);
        } else {
            e.setKeepInventory(false);
            e.setKeepLevel(false);
            e.setNewExp(0);
            final Location l = died.getLocation().getBlockY() > 0 ? died.getLocation() : ((SkyFortressMap) SkyFortress.getInstance().getChosenGameMap()).getCrownLocation().toLocation(SkyFortress.getInstance().getChosen());
            new BubbleRunnable() {
                public void run() {
                    died.spigot().respawn();
                    SkyFortress.getInstance().getGame().setSpectating(died, true);
                    died.teleport(l);
                }
            }.runTask(SkyFortress.getInstance());
            BukkitBubblePlayer deadbubble = BukkitBubblePlayer.getObject(died.getUniqueId());
            deadbubble.incrementStat(SkyFortress.getInstance().getType().getName(), "death", 1);
            String killer;
            if(died.getKiller() == null){
                killer = "PvE";
            }
            else{
                Player killplayer = died.getKiller();
                BukkitBubblePlayer player = BukkitBubblePlayer.getObject(killplayer.getUniqueId());
                player.incrementStat(SkyFortress.getInstance().getType().getName(), "kill", 1);
                killer = player.getNickName();
                killplayer.sendMessage(ChatColor.GOLD + "You killed " + ChatColor.RED + deadbubble.getNickName() + ChatColor.GOLD + "! You have now killed " + ChatColor.RED + player.getStats(SkyFortress.getInstance().getType().getName(), "kill") + ChatColor.GOLD + " players");
            }
            died.sendMessage(ChatColor.GOLD + "You were killed by " + ChatColor.RED + killer + ChatColor.GOLD + "! You have now died " + ChatColor.RED + deadbubble.getStats(SkyFortress.getInstance().getType().getName(), "death") + ChatColor.GOLD + " times");
            Bukkit.broadcastMessage(ChatColor.AQUA + deadbubble.getNickName() + ChatColor.BLUE + " was slain by " + ChatColor.AQUA + killer);
            if(fortress.getGame().getSpectatorList().size()+1 == Bukkit.getOnlinePlayers().size()-1 && fortress.getCapManager().isCapped()){
                fortress.win(fortress.getCapManager().getCapping());
            }
            else if(fortress.getGame().getSpectatorList().size()+1 == Bukkit.getOnlinePlayers().size()){
                fortress.endGame();
            }
        }
        new BubbleRunnable(){
            public void run() {
                for(GameBoard board: GameBoard.getBoards()){
                    if(board.getCurrentpreset() != null)board.enable(board.getCurrentpreset());
                }
            }
        }.runTaskAsynchonrously(fortress);
    }

    public Set<Cord> getLoaded() {
        return loaded;
    }

    public boolean isContained(Cord c) {
        for (Cord cord : getLoaded()) {
            if (cord.equals(c)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpecialSpawn(CreatureSpawnEvent e){
        if(!BYPASS){
            switch (e.getSpawnReason()){
                case NATURAL:
                case JOCKEY:
                case CHUNK_GEN:
                case EGG:
                case LIGHTNING:
                case BUILD_WITHER:
                case REINFORCEMENTS:
                case NETHER_PORTAL:
                case SILVERFISH_BLOCK:
                case DEFAULT:
                    e.setCancelled(true);
            }
        }
}

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreOpenChest(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b != null) {
                if (b.getType() == Material.CHEST) {
                    Cord c = Cord.fromBlock(b);
                    if (!isContained(c)) {
                        if (fortress.getMiddlechests().getUses() < 1) {
                            fortress.getMiddlechests().gen(1);
                        }
                        fortress.getMiddlechests().apply((InventoryHolder) b.getState());
                        loaded.add(c);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCrownBreakNear(BlockBreakEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrownBreakNear(BlockBurnEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrownBreakNear(BlockSpreadEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrownPlaceNear(BlockDamageEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrownPlaceNear(BlockExplodeEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCrownPlaceNear(BlockCanBuildEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setBuildable(false);
        }
    }

    @EventHandler
    public void onCrownPlaceNear(BlockPlaceEvent e){
        if(e.getBlock() != null && fortress.getChosenGameMap() != null && e.getBlock().getWorld() == fortress.getChosen()&& e.getBlock().getLocation().distance(((SkyFortressMap)fortress.getChosenGameMap()).getCrownLocation().toLocation(fortress.getChosen())) < 5){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCreatureAIDeath(EntityDeathEvent e){
        if(CitizensAPI.getNPCRegistry().isNPC(e.getEntity())){
            NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getEntity());
            if(npc.hasTrait(PigmanGuard.class)){
                e.getDrops().clear();
                e.setDroppedExp(0);
                if(e.getEntity().getKiller() != null){
                    Player killer = e.getEntity().getKiller();
                    BukkitBubblePlayer player = BukkitBubblePlayer.getObject(killer.getUniqueId());
                    player.incrementStat(SkyFortress.getInstance().getType().getName(), "guardkill", 1);
                    killer.sendMessage(ChatColor.GOLD + "You killed a guard! You now have " + ChatColor.RED + player.getStats(SkyFortress.getInstance().getType().getName(), "guardkill") + ChatColor.GOLD + " guard kills");
                }
                SkyFortress.getInstance().getGuards().respawn(npc);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(SkyFortress.getInstance().getCapManager().isCapped() && SkyFortress.getInstance().getCapManager().getCapping() == p){
            SkyFortress.getInstance().getCapManager().endCap();
        }

        if(!SkyFortress.getInstance().getGame().isSpectating(p) && SkyFortress.getInstance().getState() == BubbleGameAPI.State.INGAME) {
            BukkitBubblePlayer player = BukkitBubblePlayer.getObject(p.getUniqueId());
            player.incrementStat(SkyFortress.getInstance().getType().getName(), "death", 1);
            String killer;
            if(p.getKiller() == null){
                killer = "PvE";
            }
            else{
                Player killplayer = p.getKiller();
                BukkitBubblePlayer killbubble = BukkitBubblePlayer.getObject(killplayer.getUniqueId());
                killbubble.incrementStat(SkyFortress.getInstance().getType().getName(), "kill", 1);
                killer = killbubble.getNickName();
                killplayer.sendMessage(ChatColor.GOLD + "You killed " + ChatColor.RED + player.getNickName() + ChatColor.GOLD + "! You have now killed " + ChatColor.RED + killbubble.getStats(SkyFortress.getInstance().getType().getName(), "kill") + ChatColor.GOLD + " players");
            }
            p.sendMessage(ChatColor.GOLD + "You were killed by " + ChatColor.RED + killer + ChatColor.GOLD + "! You have now died " + ChatColor.RED + player.getStats(SkyFortress.getInstance().getType().getName(), "death") + ChatColor.GOLD + " times");
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getNickName() + ChatColor.BLUE + " was slain by " + ChatColor.AQUA + killer);
            Location l = p.getLocation();
            for(ItemStack is: p.getInventory()){
                l.getWorld().dropItem(l, is);
            }
        }

        new BubbleRunnable(){
            public void run() {
                for(GameBoard board: GameBoard.getBoards()){
                    if(board.getCurrentpreset() != null)board.enable(board.getCurrentpreset());
                }
            }
        }.runTaskAsynchonrously(fortress);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof ArmorStand)e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof ArmorStand)e.setCancelled(true);
    }

    @EventHandler
    public void onKingDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player && fortress.getCapManager().isCapped()){
            Player damaged = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();

            if(damaged == fortress.getCapManager().getCapping()){
                fortress.getGuards().targetIfInRange(damager, 10.0);
            }
            else if(damager == fortress.getCapManager().getCapping()){
                fortress.getGuards().targetIfInRange(damaged, 5.0);
            }
        }
    }

    @EventHandler
    public void onKingTarget(EntityTargetEvent event){
        if(fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == event.getEntity()){
            event.setCancelled(true);
        }
    }
}
