package com.thebubblenetwork.skyfortress.mobai;

import com.thebubblenetwork.api.framework.BubbleNetwork;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.HashSet;
import java.util.Set;

/**
 * The Bubble Network 2016
 * SkyFortress
 * 21/02/2016 {09:20}
 * Created February 2016
 */
public class MobManager implements Listener{
    private Set<CreatureAI> creatureAIs = new HashSet<>();

    public Set<CreatureAI> getCreatureAIs() {
        return creatureAIs;
    }

    protected CreatureAI getAI(Creature c){
        for(CreatureAI creatureAI:getCreatureAIs()){
            if(creatureAI.getCreature() == c)return creatureAI;
        }
        return null;
    }

    public void setupMeta(Creature c){
        for(MetadataValue value:c.getMetadata(CreatureMeta.META)){
            if(value.getOwningPlugin() == BubbleNetwork.getInstance().getPlugin()){
                throw new IllegalArgumentException("Already has meta");
            }
        }
        c.setMetadata(CreatureMeta.META,new LazyMetadataValue(BubbleNetwork.getInstance().getPlugin(), LazyMetadataValue.CacheStrategy.CACHE_AFTER_FIRST_EVAL,new CreatureMeta(c)));
    }

    public CreatureAI getCreatureAI(Creature c){
        for(MetadataValue value:c.getMetadata(CreatureMeta.META)){
            if(value.getOwningPlugin() == BubbleNetwork.getInstance().getPlugin()){
                Object object = value.value();
                if(object == null)return null;
                if(object instanceof CreatureAI)return (CreatureAI)object;
                return null;
            }
        }
        return null;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){
        if(e.getEntity() instanceof Creature){
            setupMeta((Creature)e.getEntity());
        }
    }

    @EventHandler
    public void onCreatureTarget(EntityTargetEvent e){
        if(e.getEntity() instanceof Creature){
            CreatureAI ai = getCreatureAI((Creature)e.getEntity());
            if(ai != null){
                ai.onTarget(e);
            }
        }
    }

    @EventHandler
    public void onCreatureDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Creature){
            CreatureAI ai = getCreatureAI((Creature)e.getEntity());
            if(ai != null){
                ai.onDamage(e);
            }
        }
    }

    @EventHandler
    public void onCreatureDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Creature){
            if(e.getEntity() instanceof Creature){
                CreatureAI ai = getCreatureAI((Creature)e.getEntity());
                if(ai != null){
                    ai.onDeath(e);
                    ai.remove();
                    return;
                }
            }
            e.getEntity().removeMetadata(CreatureMeta.META,BubbleNetwork.getInstance().getPlugin());
        }
    }
}
