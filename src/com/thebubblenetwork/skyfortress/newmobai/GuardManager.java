package com.thebubblenetwork.skyfortress.newmobai;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import com.thebubblenetwork.skyfortress.mobai.ai.BukkitGuard;
import com.thebubblenetwork.skyfortress.mobai.ai.NMSGuard;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitFactory;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuardManager {
    private Set<NPC> guards = new HashSet<>();
    private TraitInfo traitInfo;

    public GuardManager(World w, Iterable<LocationObject> locationObjects){
        traitInfo = TraitInfo.create(PigmanGuard.class);
        CitizensAPI.getTraitFactory().registerTrait(traitInfo);
        for(LocationObject object:locationObjects){
            guards.add(PigmanGuard.spawnWithTrait(object.toLocation(w)));
        }
    }


    public void respawn(final NPC npc){
        if(guards.contains(npc) && npc.hasTrait(PigmanGuard.class)) {
            Vector to = npc.getTrait(PigmanGuard.class).getGuarding();
            final Location l = new Location(npc.getEntity().getWorld(), to.getX(), to.getY(), to.getZ());
            new BubbleRunnable() {
                public void run() {
                    npc.spawn(l);
                }
            }.runTaskLater(SkyFortress.getInstance(), TimeUnit.SECONDS, 30);
        }
    }

    public void deleteAll(){
        CitizensAPI.getTraitFactory().deregisterTrait(traitInfo);
        for(NPC ai:guards){
            ai.despawn(DespawnReason.PLUGIN);
        }
        guards.clear();
    }
}
