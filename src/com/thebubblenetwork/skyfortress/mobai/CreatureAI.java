package com.thebubblenetwork.skyfortress.mobai;

import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {09:20}
 * Created February 2016
 */
public class CreatureAI<C extends NMSCreature<B>,B extends BukkitCreature<C>>{
    private static Set<Class<?>> registered = new HashSet<>();
    private static Field c,d,e,f,g;

    static {
        try {
            c = EntityTypes.class.getDeclaredField("c");
            d = EntityTypes.class.getDeclaredField("d");
            e = EntityTypes.class.getDeclaredField("e");
            f = EntityTypes.class.getDeclaredField("f");
            g = EntityTypes.class.getDeclaredField("g");
            c.setAccessible(true);
            d.setAccessible(true);
            e.setAccessible(true);
            f.setAccessible(true);
            g.setAccessible(true);
        } catch (NoSuchFieldException e) {
        }
    }

    private static void a(Class<? extends Entity> var0, String var1, int var2) throws IllegalAccessException{
        Map<String, Class<? extends Entity>> c = (Map<String, Class<? extends Entity>>) CreatureAI.c.get(null);
        Map<Class<? extends Entity>, String> d = (Map<Class<? extends Entity>, String>) CreatureAI.d.get(null);
        Map<Integer, Class<? extends Entity>> e = (Map<Integer, Class<? extends Entity>>) CreatureAI.e.get(null);
        Map<Class<? extends Entity>, Integer> f =  (Map<Class<? extends Entity>, Integer>) CreatureAI.f.get(null);
        Map<String, Integer> g = (Map<String, Integer>) CreatureAI.g.get(null);
        c.put(var1, var0);
        d.put(var0, var1);
        e.put(var2, var0);
        f.put(var0, var2);
        g.put(var1, var2);
    }

    private C creature;
    private CraftArmorStand stand;

    public static <C extends NMSCreature<B>,B extends BukkitCreature<C>> CreatureAI<C,B>  create(Class<C> creatureclass,Class<B> bukkitclass,Location l, String name){
        Constructor<C> creatureconstructor;
        Constructor<B> bukkitconstructor;
        try {
            creatureconstructor = creatureclass.getDeclaredConstructor(World.class);
            bukkitconstructor = bukkitclass.getDeclaredConstructor(CraftServer.class, creatureclass);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        WorldServer world = ((CraftWorld)l.getWorld()).getHandle();
        C creature;
        B bukkit;
        try {
            creature = creatureconstructor.newInstance(world);
            //NMSCreature may not extend EntityLiving
            Validate.isTrue(EntityLiving.class.isInstance(creature));
            bukkit = bukkitconstructor.newInstance(Bukkit.getServer(),creature);
            //BukkitCreature may not extend CraftCreature
            Validate.isTrue(CraftCreature.class.isInstance(bukkit));
            creature.setCraftCreature(bukkit);

            //Registering type
            if(!registered.contains(creatureclass)){
                //Bukkit is handy, use their entitytypes
                EntityType type = bukkit.getType();
                //Adding manually
                a(creatureclass.asSubclass(Entity.class),type.getName(),(int)type.getTypeId());
                registered.add(creatureclass);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        //Creating object
        CreatureAI<C,B> ai = new CreatureAI<>(creature,bukkit,name);
        creature.setCreatureAI(ai);
        //Getting LivingEntity instance
        EntityLiving living = EntityLiving.class.cast(creature);
        //Setting the world
        living.spawnIn(world);
        //Set location before so entity is there
        living.setPosition(l.getX(),l.getY(),l.getZ());
        //Add it to the world
        SkyListener.BYPASS = true;
        ((CraftWorld) l.getWorld()).addEntity(living, CreatureSpawnEvent.SpawnReason.CUSTOM);
        SkyListener.BYPASS = false;
        return ai;
    }

    protected CreatureAI(C creature,B bukkitcopy, String name) {
        if(creature.getBukkitEntity() != bukkitcopy)throw new IllegalArgumentException("Invalid bukkitcopy");
        this.creature = creature;
        if(name != null) {
            //Armor stand tag
            stand = SkyFortress.getInstance().spawnHologram(getCreature().getLocation(),name);
            getCreature().setPassenger(stand);
            getCreature().setCustomName(name);
        }
        else getCreature().setCustomName(getCreature().getType().getName());
        getCreature().setCustomNameVisible(false);
    }

    public boolean hasStand(){
        return stand != null;
    }

    public ArmorStand getStand(){
        return stand;
    }

    public B getCreature() {
        return getCreatureNMS().getBukkitEntity();
    }

    public C getCreatureNMS(){
        return creature;
    }
}
