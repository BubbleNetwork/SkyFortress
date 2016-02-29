package com.thebubblenetwork.skyfortress.crown;

import com.thebubblenetwork.api.framework.BukkitBubblePlayer;
import com.thebubblenetwork.api.framework.messages.Messages;
import com.thebubblenetwork.api.framework.util.mc.items.ItemStackBuilder;
import com.thebubblenetwork.api.game.scoreboard.GameBoard;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class CapManager {

    private static PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,0);
    private static PotionEffect STRENGTH = new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE,0);

    private static ItemStackBuilder HELMET = new ItemStackBuilder(Material.GOLD_HELMET).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2);
    private static ItemStackBuilder CHESTPLATE = new ItemStackBuilder(Material.GOLD_CHESTPLATE).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2);
    private static ItemStackBuilder LEGGINGS = new ItemStackBuilder(Material.GOLD_LEGGINGS).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2);
    private static ItemStackBuilder BOOTS = new ItemStackBuilder(Material.GOLD_BOOTS).withUnbreaking(true).withEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,2);

    private static ItemStackBuilder SWORD = new ItemStackBuilder(Material.GOLD_SWORD).withUnbreaking(true).withEnchantment(Enchantment.DAMAGE_ALL,1);

    private Player capping = null;
    private CapTimer timer;
    private ItemStack[] inventorycontent = null;
    private ItemStack[] armorcontent = null;

    public boolean isCapped(){
        return capping != null;
    }

    public void endCap(boolean items){
        getCapping();
        capping.removePotionEffect(SLOWNESS.getType());
        capping.removePotionEffect(STRENGTH.getType());
        timer.cancel();
        if(items) {
            PlayerInventory inventory = capping.getInventory();
            inventory.setContents(inventorycontent);
            inventory.setArmorContents(armorcontent);
        }
        Messages.broadcastMessageTitle("", ChatColor.GOLD + getNick(capping) + ChatColor.YELLOW + " lost the crown!",new Messages.TitleTiming(5,20,15));
        SkyFortress.getInstance().resetCrown();
        nullThis();
        updateScoreboards();
    }

    public void startCap(Player p){
        if(isCapped())throw new IllegalArgumentException("Already capped");
        capping = p;
        timer = new CapTimer();
        PlayerInventory inventory = p.getInventory();
        inventorycontent = inventory.getContents().clone();
        armorcontent = inventory.getArmorContents().clone();
        int currentslot = inventory.getHeldItemSlot();
        inventory.clear();
        inventory.setArmorContents(new ItemStack[]{BOOTS.build(),LEGGINGS.build(),CHESTPLATE.build(),HELMET.build()});
        inventory.setItem(currentslot,SWORD.build());
        p.addPotionEffects(Arrays.asList(SLOWNESS,STRENGTH));
        Messages.broadcastMessageTitle("", ChatColor.GOLD + getNick(p) + ChatColor.YELLOW + " is now king!",new Messages.TitleTiming(5,20,15));
        SkyFortress.getInstance().getItem().cancel();
    }

    public CapTimer getTimer() {
        return timer;
    }

    public void updateScoreboards(){
        for(GameBoard board: GameBoard.getBoards()){
            SkyFortress.getInstance().getBoard().updateAll(board,this,getTimer());
        }
    }

    private static String getNick(Player p){
        BukkitBubblePlayer player = BukkitBubblePlayer.getObject(p.getUniqueId());
        if(player != null){
            return player.getNickName();
        }
        return p.getName();
    }

    public Player getCapping(){
        if(!isCapped())throw new IllegalArgumentException("Not capped");
        return capping;
    }

    public String getCappingName(){
        return getNick(getCapping());
    }

    protected void nullThis(){
        capping = null;
        inventorycontent = null;
        armorcontent = null;
        timer = null;
    }
}
