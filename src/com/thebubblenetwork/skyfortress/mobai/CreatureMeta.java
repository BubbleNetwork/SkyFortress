package com.thebubblenetwork.skyfortress.mobai;

import com.thebubblenetwork.skyfortress.SkyFortress;
import org.bukkit.entity.Creature;

import java.util.concurrent.Callable;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {09:32}
 * Created February 2016
 */
public class CreatureMeta implements Callable<Object> {
    public static String META = "CustomAIMeta";

    private Creature creature;

    public CreatureMeta(Creature creature) {
        if (creature == null) {
            throw new IllegalArgumentException("Cannot be null");
        }
        this.creature = creature;
    }

    public Creature getCreature() {
        return creature;
    }

    public CreatureAI call() {
        try {
            return SkyFortress.getInstance().getMobManager().getAI(getCreature());
        } catch (Throwable ex) {
            return null;
        }
    }
}
