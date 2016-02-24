package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.GameTimer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class WitherGuardManager extends GameTimer{
    private Set<GuardPosition> positionSet = new HashSet<>();

    public WitherGuardManager(Iterable<Location> locations) {
        super(20, Integer.MAX_VALUE);
        for(Location l:locations){
            positionSet.add(new GuardPosition(l));
        }
    }

    public WitherGuardManager(World w, Iterable<LocationObject> locations) {
        super(20, Integer.MAX_VALUE);
        for(LocationObject l:locations){
            positionSet.add(new GuardPosition(l.toLocation(w)));
        }
    }

    public Set<GuardPosition> getPositionSet() {
        return positionSet;
    }

    public void run(int i) {
        if(BubbleGameAPI.getInstance().getState() != BubbleGameAPI.State.INGAME){
            cancel();
            end();
        }
        for(GuardPosition position:getPositionSet()){
            try {
                position.guard();
            } catch (GuardPosition.UnsafeException e) {

            }
        }
    }

    public void end(){
        positionSet = null;
    }
}
