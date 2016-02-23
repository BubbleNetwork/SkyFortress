package com.thebubblenetwork.skyfortress.mobai.ai;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class GuardPosition {
    private Location guardposition;
    private WitherGuardAI guardAI = null;

    public GuardPosition(Location guardposition) {
        this.guardposition = guardposition;
    }

    public Location getGuardposition() {
        return guardposition;
    }

    public boolean isGuarded(){
        return guardAI != null && guardAI.getCreature() != null && !guardAI.getCreature().isDead();
    }

    public boolean isSafe(){
        Block b = getGuardposition().getBlock();
        return isSafeCheck(b.getRelative(BlockFace.DOWN)) && !isSafeCheck(b) && !isSafeCheck(b.getRelative(BlockFace.UP));
    }

    private boolean isSafeCheck(Block b){
        return b.getType() != null && b.getType().isSolid();
    }

    public void guard() throws UnsafeException{
        if(isGuarded())return;
        if(!isSafe())throw new UnsafeException();
        if(guardAI != null){
            guardAI.remove();
        }
        guardAI = new WitherGuardAI(getGuardposition(), ChatColor.RED + "Wither Guard");
    }

    class UnsafeException extends Exception{
        public UnsafeException(){
            super();
        }
    }
}
