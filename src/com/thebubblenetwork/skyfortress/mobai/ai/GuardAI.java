package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {10:52}
 * Created February 2016
 */
public class GuardAI extends CreatureAI<PigZombie>{
    private static ItemStackBuilder HELMET = new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
    private static ItemStackBuilder CHEST = new ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE).withUnbreaking(true);
    private static ItemStackBuilder PANTS = new ItemStackBuilder(Material.CHAINMAIL_LEGGINGS).withUnbreaking(true);
    private static ItemStackBuilder BOOTS = new ItemStackBuilder(Material.CHAINMAIL_BOOTS).withUnbreaking(true);
    private static ItemStackBuilder SWORD = new ItemStackBuilder(Material.IRON_SWORD).withUnbreaking(true);

    private boolean alive = true;
    protected Set<DeathListener> deathListenerSet = new HashSet<>();

    public GuardAI(Location l, String name) {
        super(PigZombie.class, l, name,20);

        getCreature().setBaby(false);
        getCreature().setVillager(false);
        getCreature().setAngry(true);
        getCreature().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1),false);

        EntityEquipment entityEquipment = getCreature().getEquipment();

        entityEquipment.setHelmet(HELMET.build());
        entityEquipment.setChestplate(CHEST.build());
        entityEquipment.setLeggings(PANTS.build());
        entityEquipment.setBoots(BOOTS.build());

        entityEquipment.setItemInHand(SWORD.build());

        entityEquipment.setHelmetDropChance(0F);
        entityEquipment.setLeggingsDropChance(0F);
        entityEquipment.setChestplateDropChance(0F);
        entityEquipment.setBootsDropChance(0F);

        entityEquipment.setItemInHandDropChance(0F);
    }

    public void onTarget(EntityTargetEvent e) {
        if(!(e.getTarget() instanceof Player) || SkyFortress.getInstance().getGame().isSpectating((Player)e.getTarget())){
            e.setCancelled(true);
        }
    }

    public boolean check(Block b){
        return checkQuick(b) || checkQuick(b.getRelative(BlockFace.DOWN)) || checkQuick(b.getRelative(BlockFace.UP));
    }

    public boolean checkQuick(Block b){
        return b != null && b.getType().isSolid();
    }

    public void onDamage(EntityDamageEvent e) {
        if(e instanceof EntityDamageByEntityEvent){
            if(e instanceof Creature){
                e.setCancelled(true);
                return;
            }
        }
        switch (e.getCause()){
            case FIRE:
            case FIRE_TICK:
            case SUFFOCATION:
            case STARVATION:
            case WITHER:
            case SUICIDE:
            case FALL:
            case FALLING_BLOCK:
                e.setCancelled(true);
                return;
            case VOID:
                e.setCancelled(true);
                remove();
                return;
        }
        Location l = getCreature().getLocation();
        double yplus = l.getY() + 2D;
        for(;l.getY() < yplus;l.add(0,0.3,0)){
            getCreature().getWorld().spigot().playEffect(l,Effect.SPELL);
        }
    }

    public boolean isAlive(){
        return alive;
    }

    public void onDeath(EntityDeathEvent e) {
        alive = false;
        e.setDroppedExp(0);
        e.getDrops().clear();
        Player killer = getCreature().getKiller();
        if(killer != null){
            Location l = getCreature().getLocation();
            double yplus = l.getY() + 2D;
            for(;l.getY() < yplus;l.add(0,0.1,0)){
                getCreature().getWorld().spigot().playEffect(l,Effect.MOBSPAWNER_FLAMES);
            }
        }
        for(DeathListener listener:deathListenerSet)listener.onDeath();
    }

    interface DeathListener{
        void onDeath();
    }
}
