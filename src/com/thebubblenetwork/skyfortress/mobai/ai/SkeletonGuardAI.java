package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.util.Vector;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {10:52}
 * Created February 2016
 */
public class SkeletonGuardAI extends CreatureAI<Skeleton>{
    private static int HEALTH = 20;

    public SkeletonGuardAI(Location l, String name) {
        super(Skeleton.class, l, name,20);
        getCreature().setSkeletonType(Skeleton.SkeletonType.WITHER);
        EntityEquipment entityEquipment = getCreature().getEquipment();
        entityEquipment.setBootsDropChance(0F);
    }

    public void onTarget(EntityTargetEvent e) {
        if(e instanceof Player){

        }
        else e.setCancelled(true);
    }

    public void onDamage(EntityDamageEvent e) {

    }

    public void onDeath(EntityDeathEvent e) {
        e.setDroppedExp(0);
        e.getDrops().clear();
        Player killer = getCreature().getKiller();
        if(killer != null){
            Vector direction = killer.getLocation().getDirection();
            killer.spigot().playEffect(getCreature().getLocation(), Effect.FLAME,Effect.FLAME.getId(),0,(float)direction.getX(),(float)direction.getY(),(float)direction.getZ(),0.5F,50,2);
        }
    }
}
