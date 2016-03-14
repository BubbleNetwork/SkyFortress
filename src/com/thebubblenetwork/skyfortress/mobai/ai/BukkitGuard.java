package com.thebubblenetwork.skyfortress.mobai.ai;

import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.skyfortress.mobai.BukkitCreature;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPigZombie;
import org.bukkit.enchantments.Enchantment;

public class BukkitGuard extends CraftPigZombie implements BukkitCreature<NMSGuard>{
    private static ItemStackBuilder HELMET = new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
    private static ItemStackBuilder CHEST = new ItemStackBuilder(Material.CHAINMAIL_CHESTPLATE).withUnbreaking(true);
    private static ItemStackBuilder PANTS = new ItemStackBuilder(Material.CHAINMAIL_LEGGINGS).withUnbreaking(true);
    private static ItemStackBuilder BOOTS = new ItemStackBuilder(Material.CHAINMAIL_BOOTS).withUnbreaking(true);
    private static ItemStackBuilder SWORD = new ItemStackBuilder(Material.IRON_SWORD).withUnbreaking(true);

    public BukkitGuard(CraftServer server, NMSGuard entity) {
        super(server, entity);
        getEquipment().setHelmet(HELMET.build());
        getEquipment().setChestplate(CHEST.build());
        getEquipment().setLeggings(PANTS.build());
        getEquipment().setBoots(BOOTS.build());

        getEquipment().setItemInHand(SWORD.build());

        getEquipment().setHelmetDropChance(0F);
        getEquipment().setLeggingsDropChance(0F);
        getEquipment().setChestplateDropChance(0F);
        getEquipment().setBootsDropChance(0F);

        getEquipment().setItemInHandDropChance(0F);

        setBaby(false);
        setRemoveWhenFarAway(false);
        setVillager(false);
    }

    @Override
    public NMSGuard getHandle() {
        return (NMSGuard) super.getHandle();
    }

    public void setGoingBackDefault(boolean b){
        getHandle().setGoingback(b);
    }

    public boolean isGoingBackDefault(){
        return getHandle().isGoingback();
    }

    public void setGuardLocation(Location l){
        if(getHandle().getWorld().getWorld() != l.getWorld()){
            teleport(l);
        }
        getHandle().getTo().setX(l.getX());
        getHandle().getTo().setY(l.getY());
        getHandle().getTo().setZ(l.getZ());
    }
}
