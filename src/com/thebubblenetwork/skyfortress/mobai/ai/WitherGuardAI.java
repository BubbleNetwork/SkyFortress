package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.skyfortress.mobai.CreatureAI;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {10:52}
 * Created February 2016
 */
public class WitherGuardAI extends CreatureAI<Skeleton>{
    private static ItemStackBuilder HELMET = new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
    private static ItemStackBuilder CHEST = new ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE).withUnbreaking(true);
    private static ItemStackBuilder PANTS = new ItemStackBuilder(Material.CHAINMAIL_LEGGINGS).withUnbreaking(true);
    private static ItemStackBuilder BOOTS = new ItemStackBuilder(Material.CHAINMAIL_BOOTS).withUnbreaking(true);
    private static ItemStackBuilder SWORD = new ItemStackBuilder(Material.IRON_SWORD).withUnbreaking(true);

    private static int HEALTH = 20;
    private static int TARGETRANGE = 5;

    public WitherGuardAI(Location l, String name) {
        super(Skeleton.class, l, name,20);

        getCreature().setSkeletonType(Skeleton.SkeletonType.WITHER);
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
        if(e.getEntity() instanceof Player){
            Player target = (Player)e.getEntity();
            Vector targetloc = target.getLocation().toVector();
            Vector myloc = getCreature().getLocation().toVector();
            if(targetloc.distanceSquared(myloc) > 10){
                e.setCancelled(true);
            }
            else{
                World w = target.getWorld();
                Vector difference = myloc.clone().subtract(targetloc);
                Vector current = myloc.clone();
                Block currentblock;
                while(current.getBlockX() != targetloc.getBlockX() || current.getBlockZ() != targetloc.getBlockZ() || current.getBlockY() != targetloc.getBlockY()){
                    currentblock = current.toLocation(w).getBlock();
                    if(!check(currentblock)){
                        e.setCancelled(true);
                        return;
                    }
                    current.add(difference);
                }
            }
        }
        else e.setCancelled(true);
    }

    public boolean check(Block b){
        return checkQuick(b) || checkQuick(b.getRelative(BlockFace.DOWN)) || checkQuick(b.getRelative(BlockFace.UP));
    }

    public boolean checkQuick(Block b){
        return b != null && b.getType().isSolid();
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
