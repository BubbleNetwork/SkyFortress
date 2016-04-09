package com.thebubblenetwork.skyfortress.scoreboard;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.api.BoardPreset;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.api.BubbleBoardAPI;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.util.BoardModuleBuilder;
import com.thebubblenetwork.skyfortress.SkyFortress;
import com.thebubblenetwork.skyfortress.crown.CapManager;
import com.thebubblenetwork.skyfortress.crown.CapTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {17:40}
 * Created February 2016
 */
public class SkyFortressBoard extends BoardPreset {
    public SkyFortressBoard() {
        super("SkyFortress", new BoardModuleBuilder("Server", 11).withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Server").build(), new BoardModuleBuilder("ServerValue", 10).withRandomDisplay().build(), new BoardModuleBuilder("Spacer1", 9).withRandomDisplay().build(), new BoardModuleBuilder("Players", 8).withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Players").build(), new BoardModuleBuilder("AliveValue", 7).withDisplay(ChatColor.AQUA + "Alive: " + ChatColor.RESET).build(), new BoardModuleBuilder("WatchingValue", 6).withDisplay(ChatColor.AQUA + "Watching: " + ChatColor.RESET).build(), new BoardModuleBuilder("Spacer2", 5).withRandomDisplay().build(), new BoardModuleBuilder("CurrentCap", 4).withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Fortress").build(), new BoardModuleBuilder("CapValue1", 3).withDisplay(ChatColor.AQUA + "King: " + ChatColor.RESET).build(), new BoardModuleBuilder("CapValue2", 2).withDisplay(ChatColor.AQUA + "Time Left: " + ChatColor.RESET).build(), new BoardModuleBuilder("Spacer3", 1).withRandomDisplay().build(), new BoardModuleBuilder("address", 0).withDisplay("thebubblenetwork").build());
    }

    public void onEnable(BubbleBoardAPI api) {
        int spectators = SkyFortress.getInstance().getGame().getSpectatorList().size();
        int players = Bukkit.getOnlinePlayers().size() - spectators;
        Team address = api.getScore(this, getModule("address")).getTeam();
        address.setPrefix(ChatColor.GRAY + "play.");
        address.setSuffix(".com");
        api.getScore(this, getModule("ServerValue")).getTeam().setSuffix(BubbleNetwork.getInstance().getType().getName() + "-" + String.valueOf(BubbleNetwork.getInstance().getId()));
        api.getScore(this, getModule("AliveValue")).getTeam().setSuffix(String.valueOf(players));
        api.getScore(this, getModule("WatchingValue")).getTeam().setSuffix(String.valueOf(spectators));
        updateAll(api, SkyFortress.getInstance().getCapManager(), null);
    }

    public void updateAll(BubbleBoardAPI api, CapManager manager, CapTimer timer) {
        updateKing(api, manager);
        updateTime(api, timer);
    }

    public void updateKing(BubbleBoardAPI api, CapManager manager) {
        String cap = manager.isCapped() ? manager.getCappingName() : "No one";
        api.getScore(this, getModule("CapValue1")).getTeam().setSuffix(cap);
        Team t = api.getObject().getBoard().getTeam("KingTeam");
        if(t == null){
            t = api.getObject().getBoard().registerNewTeam("KingTeam");
            t.setPrefix(ChatColor.GOLD + ChatColor.BOLD.toString() + "King " + ChatColor.YELLOW);
        }
        if(manager.isCapped()){
            t.addPlayer(manager.getCapping());
            t.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }
        else{
            for(String s:t.getEntries()){
                t.removeEntry(s);
            }
        }
    }

    public void updateSpectators(BubbleBoardAPI api,int spectators){
        api.getScore(this, getModule("WatchingValue")).getTeam().setSuffix(String.valueOf(spectators));
    }

    public void updateLiving(BubbleBoardAPI api, int alive){
        api.getScore(this, getModule("AliveValue")).getTeam().setSuffix(String.valueOf(alive));
    }

    public void updateTime(BubbleBoardAPI api, CapTimer timer) {
        String time = timer != null ? timer.format() : "N/A";
        api.getScore(this, getModule("CapValue2")).getTeam().setSuffix(time);
    }
}
