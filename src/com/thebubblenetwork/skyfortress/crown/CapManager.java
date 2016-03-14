package com.thebubblenetwork.skyfortress.crown;

import com.thebubblenetwork.api.framework.BukkitBubblePlayer;
import com.thebubblenetwork.api.framework.messages.Messages;
import com.thebubblenetwork.api.framework.messages.titlemanager.types.TimingTicks;
import com.thebubblenetwork.api.framework.util.mc.chat.ChatColorAppend;
import com.thebubblenetwork.api.game.scoreboard.GameBoard;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.listener.SkyListener;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CapManager {

    private static String getNick(Player p) {
        BukkitBubblePlayer player = BukkitBubblePlayer.getObject(p.getUniqueId());
        if (player != null) {
            return player.getNickName();
        }
        return p.getName();
    }

    private static PotionEffect SLOWNESS = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0);
    private static PotionEffect STRENGTH = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0);
    private Player capping = null;
    private CapTimer timer;

    public boolean isCapped() {
        return capping != null;
    }

    public void endCap() {
        getCapping();
        capping.removePotionEffect(SLOWNESS.getType());
        capping.removePotionEffect(STRENGTH.getType());
        timer.cancel();
        Messages.broadcastMessageTitle("", ChatColor.GOLD + getNick(capping) + ChatColor.YELLOW + " lost the crown!", new TimingTicks(TimeUnit.MILLISECONDS,250, 1000, 750));
        SkyFortress.getInstance().resetCrown();
        capping = null;
        updateScoreboards();
    }

    public void startCap(Player p) {
        if (isCapped()) {
            throw new IllegalArgumentException("Already capped");
        }
        capping = p;
        timer = new CapTimer();
        p.addPotionEffects(Arrays.asList(SLOWNESS, STRENGTH));
        Messages.broadcastMessageTitle("", ChatColor.GOLD + getNick(p) + ChatColor.YELLOW + " is now king!", new TimingTicks(TimeUnit.MILLISECONDS,250, 1000, 750));
        SkyFortress.getInstance().getItem().cancel();
    }

    public CapTimer getTimer() {
        return timer;
    }

    public void updateScoreboards() {
        for (GameBoard board : GameBoard.getBoards()) {
            SkyFortress.getInstance().getBoard().updateAll(board, this, getTimer());
        }
    }

    public Player getCapping() {
        if (!isCapped()) {
            throw new IllegalArgumentException("Not capped");
        }
        return capping;
    }

    public String getCappingName() {
        return getNick(getCapping());
    }
}
