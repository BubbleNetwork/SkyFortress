package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.util.mc.world.LocationObject;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.map.SkyFortressMap;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import com.thebubblenetwork.skyfortress.mobai.NMSCreature;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class NMSGuard extends EntityPigZombie implements NMSCreature<BukkitGuard> {
    private static Field goalSelectorB,goalSelectorC, soundDelay;

    static{
        try {
            goalSelectorB = PathfinderGoalSelector.class.getDeclaredField("b");
            goalSelectorB.setAccessible(true);
            goalSelectorC = PathfinderGoalSelector.class.getDeclaredField("c");
            goalSelectorC.setAccessible(true);
            soundDelay = EntityPigZombie.class.getDeclaredField("soundDelay");
            soundDelay.setAccessible(true);
        } catch (NoSuchFieldException e1) {
            BubbleNetwork.getInstance().getLogger().log(Level.WARNING,"Could not setup NMSGuard",e1);
        }
    }

    private CreatureAI<NMSGuard,BukkitGuard> creatureAI;
    private boolean goingback = true;
    private Vector to = new Vector();


    public NMSGuard(World world) {
        super(world);
        //Reset goalselectors
        try {
            goalSelectorB.set(goalSelector,new UnsafeList<>());
            goalSelectorC.set(goalSelector,new UnsafeList<>());
        } catch (IllegalAccessException e1) {
            BubbleNetwork.getInstance().getLogger().log(Level.WARNING,"Could not reset goal selectors",e1);
        }
        //Auto jumping
        goalSelector.a(0, new PathfinderGoalFloat(this));
        //Attack players
        goalSelector.a(1, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 1.25D, false));
        //Go back to spawn point or to target
        goalSelector.a(2,new PathfinderGoalMoveTowardsRestriction(this,5.0D));//// FIXME: 14/03/2016 
        //Look at players
        goalSelector.a(4, new PathfinderGoalLookAtPlayer(this,EntityHuman.class,10.0F));
    }

    public Vector getTo(){
        return to;
    }

    public void setCraftCreature(BukkitGuard bukkitGuard) {
        bukkitEntity = bukkitGuard;
    }

    @SuppressWarnings("unchecked")
    public void setCreatureAI(CreatureAI creatureAI) {
        this.creatureAI = (CreatureAI<NMSGuard,BukkitGuard>)creatureAI;
    }

    public CreatureAI<NMSGuard,BukkitGuard> getCreatureAI() {
        return creatureAI;
    }

    //Tick
    @Override
    protected void E() {
        setBaby(false);
        setVillager(false);
        if(getGoalTarget() == null)a(new BlockPosition(getTo().getX(),getTo().getY(),getTo().getZ()));
        if(getSoundDelay() > 0 && getSoundDelayChange(-1) == 0) {
            this.makeSound("mob.zombiepig.zpigangry", this.bB() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }
    }

    public int getSoundDelayChange(int i){
        int sounddelay = getSoundDelay()+i;
        setSoundDelay(sounddelay);
        return sounddelay;
    }

    public void setSoundDelay(int i){
        try{
            soundDelay.set(this,i);
        }
        catch (IllegalAccessException e1){
            throw new IllegalArgumentException(e1);
        }
    }

    public int getSoundDelay(){
        try {
            return soundDelay.getInt(this);
        } catch (IllegalAccessException e1) {
            throw new IllegalArgumentException(e1);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public BukkitGuard getBukkitEntity(){
        return (BukkitGuard) super.getBukkitEntity();
    }

    //Setting up attack pathfinder
    @Override
    public void n(){
        targetSelector.a(0, new PathfinderDefence(this));
    }

    //Drops death loot
    @Override
    public void dropDeathLoot(boolean var1, int var2){
    }

    //Can the mob spawn
    @Override
    public boolean canSpawn(){
        return true;
    }

    //Initialize mob attributes
    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30D);//Faster
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(5.0D);//Follows less
    }

    //Mob death
    @Override
    public void die(){
        //// FIXME: 14/03/2016 
        super.die();
        SkyFortress.getInstance().getGuards().respawn(getCreatureAI());
    }

    public boolean isGoingback() {
        return goingback;
    }

    public void setGoingback(boolean goingback) {
        this.goingback = goingback;
    }

    static class PathfinderDefence extends PathfinderGoalTarget {
        private Player target = null;

        public PathfinderDefence(NMSGuard e) {
            super(e,true,false);
        }
        
        //Should we start
        @Override
        public boolean a() {
            List<Player> players;
            //Are we ready to go
            if (getGuard().getTo().distance(e.getBukkitEntity().getLocation().toVector()) < 4 && !(players = e.getWorld().getWorld().getPlayers()).isEmpty()) {
                double distance = Double.MAX_VALUE;
                Player selected = null;
                for (Player player : players) {
                    //Is the player actually playing?
                    if(isValid(player)) {
                        //Difference between guard position and player
                        double diff = player.getLocation().toVector().distance(getGuard().getTo());
                        if (diff < distance) {
                            selected = player;
                            distance = diff;
                        }
                    }
                }
                //If distance is in range, target
                if (distance <= 10.0D) {
                    target = selected;
                }
            }
            //Always switch targets
            return true;
        }

        public boolean isValid(Player p){
            //Feeble, but it works and is simple
            return !p.isDead() && p.isOnline() && p.getGameMode() == GameMode.SURVIVAL;
        }

        //Go
        @Override
        public void c() {
            EntityHuman human = target == null ? null : ((CraftPlayer) target).getHandle();
            this.e.setGoalTarget(human, EntityTargetEvent.TargetReason.CUSTOM, false);
            super.c();
            target = null;
        }

        public NMSGuard getGuard() {
            return (NMSGuard) e;
        }
    }
}
