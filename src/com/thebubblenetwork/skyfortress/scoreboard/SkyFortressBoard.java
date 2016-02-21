package com.thebubblenetwork.skyfortress.scoreboard;

import com.thebubblenetwork.api.framework.util.mc.scoreboard.BoardPreset;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.BubbleBoardAPI;
import com.thebubblenetwork.api.framework.util.mc.scoreboard.util.BoardModuleBuilder;
import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 20/02/2016 {17:40}
 * Created February 2016
 */
public class SkyFortressBoard extends BoardPreset{
    public SkyFortressBoard() {
        super("SkyFortress",
                new BoardModuleBuilder("Alive",16).withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Alive").build(),
                new BoardModuleBuilder("AliveValue",15).withRandomDisplay().build(),
                new BoardModuleBuilder("Spacer1",14).withRandomDisplay().build(),
                new BoardModuleBuilder("Watching",13).withDisplay(ChatColor.BLUE + ChatColor.BOLD.toString() + "Watching").build(),
                new BoardModuleBuilder("WatchingValue",12).withRandomDisplay().build(),
                new BoardModuleBuilder("Spacer2",11).withRandomDisplay().build()
        );
    }

    public void onEnable(BubbleBoardAPI api){
        int spectators = SkyFortress.getInstance().getGame().getSpectatorList().size();
        int players = Bukkit.getOfflinePlayers().length- spectators;
        api.getScore(this,getModule("AliveValue")).getTeam().setSuffix(String.valueOf(players));
        api.getScore(this,getModule("WatchingValue")).getTeam().setSuffix(String.valueOf(spectators));
    }
}
