package center.hardcore.uhccore.listener;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.DefaultTimer;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.TimeUnit;

public class PlayerDamageListener implements Listener
{
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if(event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();

        Timer timer = Main.getInstance().getTimerHandler().getTimer(damaged, TimerType.COMBAT_TAG);
        if(timer != null && timer.getTime() > 0)
            timer.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            Main.getInstance().getTimerHandler().addTimer(damaged, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damaged));

        if(!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        if(Main.getInstance().getServerHandler().getPvpProtected().contains(damager.getUniqueId()))
        {
            event.setCancelled(true);
            return;
        }

        Timer timer1 = Main.getInstance().getTimerHandler().getTimer(damager, TimerType.COMBAT_TAG);
        if(timer1 != null && timer1.getTime() > 0)
            timer1.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            Main.getInstance().getTimerHandler().addTimer(damager, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damager));
    }
}
