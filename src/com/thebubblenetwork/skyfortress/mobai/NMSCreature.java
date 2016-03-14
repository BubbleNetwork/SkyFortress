package com.thebubblenetwork.skyfortress.mobai;

public interface NMSCreature<T extends BukkitCreature>{
    void setCraftCreature(T t);

    void setCreatureAI(CreatureAI creatureAI);

    T getBukkitEntity();

    CreatureAI getCreatureAI();
}
