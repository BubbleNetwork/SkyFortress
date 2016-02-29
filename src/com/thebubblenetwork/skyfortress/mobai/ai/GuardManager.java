package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.api.game.GameTimer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class GuardManager extends GameTimer{
    private Set<GuardPosition> positionSet = new HashSet<>();

    public GuardManager(World w, Iterable<LocationObject> locations) {
        super(20, Integer.MAX_VALUE);
        for(LocationObject l:locations){
            Location location = l.toLocation(w);
            location.setX(location.getBlockX() + 0.5D);
            location.setZ(location.getBlockZ() + 0.5D);
            location.setYaw(0F);
            GuardPosition position = new GuardPosition(location);
            positionSet.add(position);
            position.respawn();
            position.findPlayer();
        }
    }

    public Set<GuardPosition> getPositionSet() {
        return positionSet;
    }

    public void run(int i) {
        if((BubbleGameAPI.getInstance().getState() != BubbleGameAPI.State.INGAME && BubbleGameAPI.getInstance().getState() != BubbleGameAPI.State.PREGAME) || positionSet == null){
            cancel();
            return;
        }
        for(GuardPosition position:getPositionSet()){
            try {
                position.guard();
            } catch (GuardPosition.UnsafeException e) {
                BubbleNetwork.getInstance().getLogger().log(Level.SEVERE,"Could not position guard",e);
            }
        }
    }

    public void end(){

    }
}
