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
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
            BukkitBubblePlayer deadking = BukkitBubblePlayer.getObject(died.getUniqueId());
            if(died.getKiller() == null){
                killer = "PvE";
            }
            else{
                Player killplayer = died.getKiller();
                BukkitBubblePlayer player = BukkitBubblePlayer.getObject(killplayer.getUniqueId());
                killer = player.getNickName();
                player.incrementStat(SkyFortress.getInstance().getType().getName(), "kingkill", 1);
                player.setTokens(player.getTokens() + 10);
                killplayer.sendMessage(ChatColor.GOLD + "+10 Tokens");
                killplayer.sendMessage(ChatColor.GOLD + "You assassinated the king! You have now assassinated " + ChatColor.RED + (int)player.getStats(SkyFortress.getInstance().getType().getName(), "kingkill") + ChatColor.GOLD + " kings");
            }
            Bukkit.broadcastMessage(ChatColor.YELLOW + deadking.getNickName() + ChatColor.BLUE + " was assassinated by " + ChatColor.AQUA + killer);
        } else {
            e.setKeepInventory(false);
            e.setKeepLevel(false);
            e.setNewExp(0);
            final Location l = died.getLocation().getBlockY() > 0 ? died.getLocation() : ((SkyFortressMap) SkyFortress.getInstance().getChosenGameMap()).getCrownLocation().toLocation(SkyFortress.getInstance().getChosen());
            new BubbleRunnable() {
                public void run() {
                    died.teleport(l);
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
                player.setTokens(player.getTokens() + 20);
                killplayer.sendMessage(ChatColor.GOLD + "+20 Tokens");
                killplayer.sendMessage(ChatColor.GOLD + "You killed " + ChatColor.RED + deadbubble.getNickName() + ChatColor.GOLD + "! You have now killed " + ChatColor.RED + (int)player.getStats(SkyFortress.getInstance().getType().getName(), "kill") + ChatColor.GOLD + " players");
            }
            died.sendMessage(ChatColor.GOLD + "You were killed by " + ChatColor.RED + killer + ChatColor.GOLD + "! You have now died " + ChatColor.RED + (int)deadbubble.getStats(SkyFortress.getInstance().getType().getName(), "death") + ChatColor.GOLD + " times");
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
                int watching = fortress.getGame().getSpectatorList().size();
                int playing = Bukkit.getOnlinePlayers().size()-watching;
                for(GameBoard board: GameBoard.getBoards()){
                    SkyFortress.getInstance().getBoard().updateSpectators(board, watching);
                    SkyFortress.getInstance().getBoard().updateLiving(board, playing);
                }
            }
        }.runTaskLaterAsynchronously(fortress, TimeUnit.MILLISECONDS, 100L);
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
                    if(fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == killer){
                        killer.sendMessage(ChatColor.GOLD + "You killed one of your own subjects!");
                    }
                    else {
                        player.incrementStat(SkyFortress.getInstance().getType().getName(), "guardkill", 1);
                        player.setTokens(player.getTokens() + 2);
                        killer.sendMessage(ChatColor.GOLD + "+2 Tokens");
                        killer.sendMessage(ChatColor.GOLD + "You killed a guard! You now have " + ChatColor.RED + (int) player.getStats(SkyFortress.getInstance().getType().getName(), "guardkill") + ChatColor.GOLD + " guard kills");
                    }
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
                killbubble.setTokens(killbubble.getTokens() + 20);
                killplayer.sendMessage(ChatColor.GOLD + "+20 Tokens");
                killplayer.sendMessage(ChatColor.GOLD + "You killed " + ChatColor.RED + player.getNickName() + ChatColor.GOLD + "! You have now killed " + ChatColor.RED + (int)killbubble.getStats(SkyFortress.getInstance().getType().getName(), "kill") + ChatColor.GOLD + " players");
            }
            p.sendMessage(ChatColor.GOLD + "You were killed by " + ChatColor.RED + killer + ChatColor.GOLD + "! You have now died " + ChatColor.RED + (int)player.getStats(SkyFortress.getInstance().getType().getName(), "death") + ChatColor.GOLD + " times");
            Bukkit.broadcastMessage(ChatColor.AQUA + player.getNickName() + ChatColor.BLUE + " was slain by " + ChatColor.AQUA + killer);
            Location l = p.getLocation();
            for(ItemStack is: p.getInventory()){
                l.getWorld().dropItem(l, is);
            }
        }

        new BubbleRunnable(){
            public void run() {
                int watching = fortress.getGame().getSpectatorList().size();
                int playing = Bukkit.getOnlinePlayers().size()-watching;
                for(GameBoard board: GameBoard.getBoards()){
                    SkyFortress.getInstance().getBoard().updateSpectators(board, watching);
                    SkyFortress.getInstance().getBoard().updateLiving(board, playing);
                }
            }
        }.runTaskLaterAsynchronously(fortress, TimeUnit.MILLISECONDS, 100L);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof ArmorStand)e.setCancelled(true);
        if(e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.VOID)e.setCancelled(true);
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
                fortress.getGuards().targetIfInRange(damager, 15.0);
            }
            else if(damager == fortress.getCapManager().getCapping()){
                fortress.getGuards().targetIfInRange(damaged, 8.0);
            }
        }
        else if(CitizensAPI.getNPCRegistry().isNPC(e.getDamager()) && fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == e.getEntity()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onKingTarget(EntityTargetEvent event){
        if(fortress.getCapManager().isCapped() && fortress.getCapManager().getCapping() == event.getEntity()){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCraft(InventoryClickEvent e){
        if(e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.CHEST){
            e.setCurrentItem(null);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e){
        if(e.getItem() != null && e.getItem().getItemStack() != null && e.getItem().getItemStack().getType() == Material.CHEST) {
            e.setCancelled(true);
        }
    }
}
