package center.hardcore.uhccore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionSplashListener implements Listener
{
    private final List<PotionEffectType> blockPotionEffects;

    private PotionSplashListener()
    {
        this.blockPotionEffects = new ArrayList<>();
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event)
    {
        if(this.containsBlockPotionEffect(event.getPotion().getEffects()))
            event.setCancelled(true);
    }

    private boolean containsBlockPotionEffect(Collection<PotionEffect> potionEffects)
    {
        return potionEffects.stream().anyMatch(potionEffect -> this.blockPotionEffects.contains(potionEffect.getType()));
    }
}
