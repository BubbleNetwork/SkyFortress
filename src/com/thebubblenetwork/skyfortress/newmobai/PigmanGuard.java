package com.thebubblenetwork.skyfortress.newmobai;

import com.thebubblenetwork.skyfortress.SkyFortress;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.ai.PrioritisableGoal;
import net.citizensnpcs.api.ai.goals.TargetNearbyEntityGoal;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.Collections;

public class PigmanGuard extends Trait{
    private static String GUARDNAME = ChatColor.RED + "Guard";

    public static NPC spawnWithTrait(Location l){
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PIG_ZOMBIE, "Guard");
        npc.addTrait(PigmanGuard.class);
        npc.getTrait(PigmanGuard.class).setGuard(l.toVector());
        return npc;
    }

    private Vector guard = new Vector();

    public Vector getGuarding() {
        return guard;
    }

    public PigmanGuard() {
        super(PigmanGuard.class.getSimpleName());
    }

    public void setGuard(Vector v){
        guard.setX(v.getX());
        guard.setY(v.getY());
        guard.setZ(v.getZ());
    }

    public void save(DataKey key) {
        key.setDouble("x", guard.getX());
        key.setDouble("y", guard.getY());
        key.setDouble("z", guard.getZ());
    }

    public void load(DataKey key) throws NPCLoadException {
        guard.setX(key.getDouble("x"));
        guard.setY(key.getDouble("y"));
        guard.setZ(key.getDouble("z"));
    }

    public void onAttach() {
        getNPC().getDefaultGoalController().addPrioritisableGoal(new PigmanGuardGoal());
        getNPC().getDefaultGoalController().addGoal(new TargetNearbyEntityGoal.Builder(getNPC()).aggressive(true).radius(DISTFROMPOST).targets(Collections.singleton(EntityType.PLAYER)).build(),0);
    }

    public void onCopy() {
    }

    public void onDespawn() {
    }

    public void onRemove() {
        SkyFortress.getInstance().getGuards().respawn(getNPC());
    }

    public void onSpawn() {
        LivingEntity entity = ((LivingEntity)getNPC().getEntity());
        entity.setCustomName(GUARDNAME);
        entity.setCustomNameVisible(true);
        entity.setMaxHealth(20.0D);
        entity.setHealth(20.0D);
    }

    private static double DISTFROMPOST = 10.0D;

    private class PigmanGuardGoal implements PrioritisableGoal{

        public PigmanGuardGoal() {
        }

        public void reset() {
        }

        public int getPriority() {
            return 0;
        }

        public void run(GoalSelector goalSelector) {
            if(getNPC().getNavigator().isNavigating()){
                getNPC().getNavigator().cancelNavigation();
            }
            getNPC().getNavigator().setPaused(false);
            getNPC().getNavigator().setTarget(guard.toLocation(getNPC().getEntity().getWorld()));
        }

        public boolean shouldExecute(GoalSelector goalSelector) {
            return getNPC().hasTrait(PigmanGuard.class) && getNPC().getNavigator().getTargetAsLocation().toVector().distance(guard) > DISTFROMPOST;
        }
    }
}
