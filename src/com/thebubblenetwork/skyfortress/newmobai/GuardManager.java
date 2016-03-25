package com.thebubblenetwork.skyfortress.newmobai;

import com.thebubblenetwork.api.framework.plugin.util.BubbleRunnable;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuardManager {
    private Set<NPC> guards = new HashSet<>();
    private Set<BubbleRunnable> runnables = new HashSet<>();
    private TraitInfo traitInfo;
    private World w;

    public GuardManager(World w, Iterable<LocationObject> locationObjects){
        this.w = w;
        traitInfo = TraitInfo.create(PigmanGuard.class);
        CitizensAPI.getTraitFactory().registerTrait(traitInfo);
        for(LocationObject object:locationObjects){
            guards.add(PigmanGuard.spawnWithTrait(object.toLocation(w)));
        }
    }

    public void spawnAll(){
        SkyListener.BYPASS = true;
        for(NPC npc:guards){
            if(!npc.isSpawned()) {
                npc.spawn(npc.getTrait(PigmanGuard.class).getGuarding().toLocation(w));
            }
        }
        SkyListener.BYPASS = false;
    }

    public void respawn(final NPC npc){
        if(guards.contains(npc) && npc.hasTrait(PigmanGuard.class)) {
            Vector to = npc.getTrait(PigmanGuard.class).getGuarding();
            final Location l = new Location(w, to.getX(), to.getY(), to.getZ());
            BubbleRunnable runnable = new BubbleRunnable() {
                public void run() {
                    runnables.remove(this);
                    SkyListener.BYPASS = true;
                    npc.spawn(l);
                    SkyListener.BYPASS = false;
                }
            };
            runnables.add(runnable);
            runnable.runTaskLater(SkyFortress.getInstance(), TimeUnit.SECONDS, 30);
        }
    }

    public void deleteAll(){
        for(NPC ai:guards){
            ai.getOwningRegistry().deregister(ai);
        }
        for(BubbleRunnable runnable:runnables){
            runnable.cancel();
        }
        CitizensAPI.getTraitFactory().deregisterTrait(traitInfo);
        guards.clear();
    }
}
