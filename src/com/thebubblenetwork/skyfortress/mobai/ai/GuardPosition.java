package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GuardPosition implements GuardAI.DeathListener{
    private static final int RESPAWNTIME = 15;

    private Location guardposition;
    private GuardAI guardAI = null;

    public GuardPosition(Location guardposition) {
        this.guardposition = guardposition;
    }

    public Location getGuardposition() {
        return guardposition;
    }

    public boolean isGuarded(){
        double diff;
        return getGuardposition().toVector().distance(guardAI.getCreature().getLocation().toVector()) < 15 && (diff = getGuardposition().getY() - guardAI.getCreature().getLocation().getY()) < 2 && diff > -2;
    }

    public boolean isSafe(){
        Block b = getGuardposition().getBlock();
        return isSafeCheck(b.getRelative(BlockFace.DOWN)) && !isSafeCheck(b) && !isSafeCheck(b.getRelative(BlockFace.UP));
    }

    private boolean isSafeCheck(Block b){
        return b.getType() != null && b.getType().isSolid();
    }

    public void findPlayer(){
        double distancesquared = Double.MAX_VALUE;
        double temp;
        Player selected = null;
        Location creatureloc = guardAI.getCreature().getLocation();
        for(Player p: Bukkit.getOnlinePlayers()){
            if(!SkyFortress.getInstance().getGame().isSpectating(p)){
                if((temp = p.getLocation().distanceSquared(creatureloc)) < distancesquared ){
                    distancesquared = temp;
                    selected = p;
                }
            }
        }
        guardAI.getCreature().setTarget(selected);
        guardAI.getCreature().setAngry(true);
    }

    public void respawn(){
        if (guardAI != null){
            guardAI.deathListenerSet = null;
            guardAI.remove();
        }
        guardAI = new GuardAI(getGuardposition(), ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Guard");
        guardAI.deathListenerSet.add(this);
    }

    public void guard(){
        if(guardAI.isAlive()) {
            if(!isGuarded()){
                guardAI.getCreature().teleport(getGuardposition());
            }
            findPlayer();
        }
    }

    public void onDeath(){
        new BubbleRunnable(){
            public void run() {
                if(isSafe())respawn();
                else onDeath();
            }
        }.runTaskLater(SkyFortress.getInstance(), TimeUnit.SECONDS,RESPAWNTIME);
    }
}
