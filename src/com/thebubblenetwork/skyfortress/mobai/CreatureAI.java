package com.thebubblenetwork.skyfortress.mobai;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {09:20}
 * Created February 2016
 */
public abstract class CreatureAI<T extends Creature> {
    private T creature;
    private int maxhealth;

    protected CreatureAI (T creature,String name,int maxhealth){
        this.creature = creature;
        this.maxhealth = maxhealth;
        creature.setTarget(null);
        creature.setCustomName(name);
        creature.setCustomNameVisible(true);
        creature.setMaxHealth(maxhealth);
        SkyFortress.getInstance().getMobManager().getCreatureAIs().add(this);
    }

    public CreatureAI(Class<T> clazz,Location l,String name,int maxhealth){
        this(l.getWorld().spawn(l,clazz),name,maxhealth);
    }

    public T getCreature(){
        return creature;
    }

    public void remove(){
        SkyFortress.getInstance().getMobManager().getCreatureAIs().remove(this);
        getCreature().removeMetadata(CreatureMeta.META,BubbleNetwork.getInstance().getPlugin());
        getCreature().remove();
    }

    public void dealDamage(double amt){
        getCreature().damage(amt);
    }

    public int getMaxhealth(){
        return maxhealth;
    }

    public abstract void onDeath(EntityDeathEvent e);
    public abstract void onTarget(EntityTargetEvent e);
    public abstract void onDamage(EntityDamageEvent e);
}
