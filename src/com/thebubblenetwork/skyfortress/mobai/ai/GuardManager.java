package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.plugin.BubbleRunnable;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GuardManager {
    private static String GUARDNAME = ChatColor.RED + "Guard";
    private Set<CreatureAI<NMSGuard,BukkitGuard>> guards = new HashSet<>();

    public GuardManager(World w, Iterable<LocationObject> locationObjects){
        for(LocationObject object:locationObjects){
            CreatureAI<NMSGuard,BukkitGuard> ai = CreatureAI.create(NMSGuard.class,BukkitGuard.class,object.toLocation(w),GUARDNAME);
            ai.getCreature().setGuardLocation(object.toLocation(w));
        }
    }


    public void respawn(CreatureAI<NMSGuard,BukkitGuard> ai){
        if(guards.contains(ai)) {
            guards.remove(ai);
            Vector to = ai.getCreatureNMS().getTo();
            final Location l = new Location(ai.getCreature().getWorld(), to.getX(), to.getY(), to.getZ());
            new BubbleRunnable() {
                public void run() {
                    guards.add(CreatureAI.create(NMSGuard.class, BukkitGuard.class, l, GUARDNAME));
                }
            }.runTaskLater(SkyFortress.getInstance(), TimeUnit.SECONDS, 30);
        }
    }

    public void deleteAll(){
        for(CreatureAI ai:guards){
            ai.getCreature().remove();
        }
        guards.clear();
    }
}
