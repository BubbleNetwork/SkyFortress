package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.Date;

public class GuardPosition implements GuardAI.DeathListener{
    private Location guardposition;
    private GuardAI guardAI = null;

    private int RESPAWNTIME = 15;

    private long died = 0L;

    public boolean canRespawn(){
        return new Date(died + (long)RESPAWNTIME*1000L).after(new Date(System.currentTimeMillis()));
    }

    public GuardPosition(Location guardposition) {
        this.guardposition = guardposition;
    }

    public Location getGuardposition() {
        return guardposition;
    }

    public boolean isGuarded(){
        double diff;
        return guardAI != null
                && guardAI.getCreature() != null
                && guardAI.isAlive()
                && !guardAI.getCreature().isDead()
                &&  guardposition.toVector().distanceSquared(guardAI.getCreature().getLocation().toVector()) < 10
                && (diff = guardposition.getY() - guardAI.getCreature().getLocation().getY()) < 2 && diff > -2;
    }

    public boolean isSafe(){
        Block b = getGuardposition().getBlock();
        return isSafeCheck(b.getRelative(BlockFace.DOWN)) && !isSafeCheck(b) && !isSafeCheck(b.getRelative(BlockFace.UP));
    }

    private boolean isSafeCheck(Block b){
        return b.getType() != null && b.getType().isSolid();
    }

    public void findPlayer(){
        guardAI.getCreature().setAngry(true);
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
        if (guardAI != null) guardAI.remove();
        guardAI = new GuardAI(getGuardposition(), ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Guard");
    }

    public void guard() throws UnsafeException{
        if(!isGuarded()) {
            if (isSafe()) {
                if (guardAI != null && guardAI.isAlive() && !guardAI.getCreature().isDead()) {
                    guardAI.getCreature().teleport(getGuardposition());
                }
                else if (canRespawn()){
                    respawn();
                    return;
                }
            }
            else throw new UnsafeException();
        }
        findPlayer();
    }

    public void onDeath(){
        died = System.currentTimeMillis();
    }

    class UnsafeException extends Exception{
        public UnsafeException(){
            super();
        }
    }
}
