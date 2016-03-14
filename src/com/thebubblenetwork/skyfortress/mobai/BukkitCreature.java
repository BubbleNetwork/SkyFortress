package com.thebubblenetwork.skyfortress.mobai;

import org.bukkit.entity.Creature;

public interface BukkitCreature<T extends NMSCreature> extends Creature{
    T getHandle();
}
