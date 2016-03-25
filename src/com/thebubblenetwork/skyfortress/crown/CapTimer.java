package com.thebubblenetwork.skyfortress.crown;

import com.thebubblenetwork.api.framework.util.mc.timer.GameTimer;
import com.thebubblenetwork.api.game.scoreboard.GameBoard;
import com.thebubblenetwork.skyfortress.SkyFortress;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CapTimer extends GameTimer {
    private static final DateFormat format = new SimpleDateFormat("mm:ss");

    public CapTimer() {
        super(20, 60 * 4);
    }

    public void run(int i) {
        for (GameBoard api : GameBoard.getBoards()) {
            SkyFortress.getInstance().getBoard().updateAll(api, SkyFortress.getInstance().getCapManager(), this);
        }
    }

    public void end() {
        SkyFortress.getInstance().win(SkyFortress.getInstance().getCapManager().getCapping());
    }        //PLAYER WIN


    public String format() {
        return format.format(new Date((long) (getLeft() * 1000)));
    }
}
