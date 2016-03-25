package com.thebubblenetwork.skyfortress.listener;

import com.thebubblenetwork.api.framework.plugin.util.BubbleRunnable;
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
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

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
        final Player died = e.getEntity();
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
            if(fortress.getGame().getSpectatorList().size() == Bukkit.getOnlinePlayers().size()){
                fortress.endGame();
            }
        }
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
                SkyFortress.getInstance().getGuards().respawn(npc);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        if(SkyFortress.getInstance().getCapManager().isCapped() && SkyFortress.getInstance().getCapManager().getCapping() == e.getPlayer()){
            SkyFortress.getInstance().getCapManager().endCap();
        }
        if(!fortress.getGame().isSpectating(e.getPlayer()) && fortress.getGame().getSpectatorList().size() <= Bukkit.getOnlinePlayers().size()){
            fortress.endGame();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof ArmorStand)e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof ArmorStand)e.setCancelled(true);
    }
}
