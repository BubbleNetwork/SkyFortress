package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.api.game.BubbleGameAPI;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuardManager extends BubbleRunnable {
    private Set<GuardPosition> positionSet = new HashSet<>();

    public GuardManager(World w, Iterable<LocationObject> locations) {
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
        runTaskTimer(SkyFortress.getInstance(), TimeUnit.MILLISECONDS,500L);
    }

    public Set<GuardPosition> getPositionSet() {
        return positionSet;
    }

    public void run() {
        if(BubbleGameAPI.getInstance().getState() != BubbleGameAPI.State.INGAME && BubbleGameAPI.getInstance().getState() != BubbleGameAPI.State.PREGAME){
            cancel();
            return;
        }
        for(GuardPosition position:getPositionSet()){
            position.guard();
        }
    }
}
