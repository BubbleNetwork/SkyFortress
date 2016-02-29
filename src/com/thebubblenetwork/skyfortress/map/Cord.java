package com.thebubblenetwork.skyfortress.map;

import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class Cord {
    public static Cord fromLocation(Location l){
        return new Cord(l.getBlockX(),l.getBlockY(),l.getBlockZ());
    }

    public static Cord fromBlock(Block b){
        return fromLocation(b.getLocation());
    }

    public static Cord fromState(BlockState state){
        return fromLocation(state.getLocation());
    }

    public static Cord fromLocation(LocationObject l){
        return new Cord((int)l.getX(),(int)l.getY(),(int)l.getZ());
    }

    private int x,y,z;

    public Cord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString(){
        return "[x=" + getX() + " y=" + getY() + " z=" + getZ() + "]";
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Cord){
            Cord cord = (Cord)o;
            return getX() == cord.getX() && getY() == cord.getY() && getZ() == cord.getZ();
        }
        return false;
    }
}
