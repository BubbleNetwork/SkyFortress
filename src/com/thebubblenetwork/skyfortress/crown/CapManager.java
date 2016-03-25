package com.thebubblenetwork.skyfortress.crown;

import com.sun.istack.internal.Nullable;
import com.thebubblenetwork.api.framework.messages.Messages;
import com.thebubblenetwork.api.framework.messages.titlemanager.types.TimingTicks;
import com.thebubblenetwork.api.framework.player.BukkitBubblePlayer;
import com.thebubblenetwork.api.game.scoreboard.GameBoard;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

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
        Player previouscap = capping;
        capping = null;
        updateScoreboards(previouscap, null);
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
        updateScoreboards(null, capping);
    }

    public CapTimer getTimer() {
        return timer;
    }

    public void updateScoreboards(@Nullable final Player previouscap, @Nullable final Player currentcap) {
        final BukkitBubblePlayer prevcap = previouscap == null ? null : BukkitBubblePlayer.getObject(previouscap.getUniqueId());
        new Thread(){
            @Override
            public void run() {
                for (GameBoard board : GameBoard.getBoards()) {
                    if(currentcap != null) {
                        for (Team t : board.getObject().getBoard().getTeams()) {
                            t.removePlayer(currentcap);
                        }
                    }
                    SkyFortress.getInstance().getBoard().updateAll(board, CapManager.this, getTimer());
                    if(previouscap != null && prevcap != null){
                        board.applyRank(prevcap.getRank(), previouscap);
                    }
                }
            }
        }.start();
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
