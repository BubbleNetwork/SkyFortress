package com.thebubblenetwork.skyfortress.newmobai;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.skyfortress.SkyFortress;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.event.CancelReason;
import net.citizensnpcs.api.ai.event.NavigatorCallback;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PigmanGuard extends Trait {
    private static String GUARDNAME = ChatColor.RED + "Guard";

    private static ItemStackBuilder HELMET = new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
    private static ItemStackBuilder CHEST = new ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE).withUnbreaking(true);
    private static ItemStackBuilder PANTS = new ItemStackBuilder(Material.CHAINMAIL_LEGGINGS).withUnbreaking(true);
    private static ItemStackBuilder BOOTS = new ItemStackBuilder(Material.CHAINMAIL_BOOTS).withUnbreaking(true);
    private static ItemStackBuilder SWORD = new ItemStackBuilder(Material.IRON_SWORD).withUnbreaking(true);

    public static NPC spawnWithTrait(Location l) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PIG_ZOMBIE, GUARDNAME);
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

    public void setGuard(Vector v) {
        guard.setX(v.getX());
        guard.setY(v.getY());
        guard.setZ(v.getZ());
    }

    public void save(DataKey key) {
    }

    public void load(DataKey key) throws NPCLoadException {
    }

    public void onAttach() {
    }

    public void onCopy() {
    }

    public void onDespawn() {
    }

    public void onRemove() {

    }

    public void onSpawn() {
        List<Class<? extends Trait>> toRemove = new ArrayList<>();
        for(Trait trait: getNPC().getTraits()){
            if(trait != this)toRemove.add(trait.getClass());
        }
        for(Class<? extends Trait> traitclass: toRemove){
            getNPC().removeTrait(traitclass);
        }

        getNPC().setProtected(false);
        getNPC().setFlyable(false);

        getNPC().getDefaultGoalController().clear();

        getNPC().getDefaultGoalController().addBehavior(new GuardLookGoal(),1);
        getNPC().getDefaultGoalController().addBehavior(new GuardTargetGoal(), 5);

        getNPC().getNavigator().getDefaultParameters().attackRange(2.5D).avoidWater(true).baseSpeed(1.3F);
        getNPC().getNavigator().getDefaultParameters().distanceMargin(7.0D);
        getNPC().getNavigator().getDefaultParameters().avoidWater(true);


        LivingEntity entity = ((LivingEntity) getNPC().getEntity());
        entity.setCustomName(GUARDNAME);
        entity.setCustomNameVisible(true);
        entity.setMaxHealth(25.0D);
        entity.setHealth(25.0D);

        entity.getEquipment().setHelmet(HELMET.build());
        entity.getEquipment().setChestplate(CHEST.build());
        entity.getEquipment().setLeggings(PANTS.build());
        entity.getEquipment().setBoots(BOOTS.build());

        entity.getEquipment().setItemInHand(SWORD.build());

        entity.getEquipment().setHelmetDropChance(0F);
        entity.getEquipment().setLeggingsDropChance(0F);
        entity.getEquipment().setChestplateDropChance(0F);
        entity.getEquipment().setBootsDropChance(0F);

        entity.getEquipment().setItemInHandDropChance(0F);

        if (entity instanceof Ageable) {
            Ageable ageable = (Ageable) entity;
            ageable.setAdult();
            ageable.setAgeLock(true);
        }
    }

    private static double DISTFROMPOST = 7.0D;

    private class GuardLookGoal extends BehaviorGoalAdapter{
        private boolean finished = false;

        public void reset() {
            finished = false;
        }

        public BehaviorStatus run() {
            return finished ? BehaviorStatus.SUCCESS : BehaviorStatus.RUNNING;
        }

        public boolean shouldExecute() {
            if(getNPC() != null && getNPC().hasTrait(PigmanGuard.class) && getNPC().isSpawned() && getNPC().getNavigator() != null && getNPC().getNavigator().getTargetAsLocation() != null){
                getNPC().faceLocation(getNPC().getNavigator().getTargetAsLocation());
                finished = true;
                return true;
            }
            return false;
        }
    }

    public class GuardTargetGoal extends BehaviorGoalAdapter {
        private boolean finished;
        private CancelReason reason;
        private Player target;

        private GuardTargetGoal() {
        }

        public void reset() {
            getNPC().getNavigator().cancelNavigation();
            this.target = null;
            this.finished = false;
            this.reason = null;
        }

        public BehaviorStatus run() {
            return this.finished ? (this.reason == null ? BehaviorStatus.SUCCESS : BehaviorStatus.FAILURE) : BehaviorStatus.RUNNING;
        }

        public boolean shouldExecute() {
            if (getNPC() != null && getNPC().hasTrait(PigmanGuard.class) && getNPC().isSpawned() && getNPC().getNavigator() != null && getNPC().getEntity() != null) {
                this.target = null;
                for (Player entity : getNPC().getEntity().getWorld().getPlayers()) {
                    if (entity.getLocation().toVector().distance(guard) <= DISTFROMPOST) {
                        this.target = entity;
                        break;
                    }
                }

                if (this.target != null) {
                    getNPC().getNavigator().setTarget(this.target, true);
                }
                else{
                    getNPC().getNavigator().setTarget(guard.toLocation(getNPC().getEntity().getWorld()));
                }

                getNPC().getNavigator().getLocalParameters().addSingleUseCallback(new NavigatorCallback() {
                    public void onCompletion(CancelReason cancelReason) {
                        GuardTargetGoal.this.reason = cancelReason;
                        GuardTargetGoal.this.finished = true;
                    }
                });
                return true;
            }
            return false;
        }
    }
}
