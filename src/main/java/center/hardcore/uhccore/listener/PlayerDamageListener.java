package center.hardcore.uhccore.listener;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.DefaultTimer;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.TimeUnit;

public class PlayerDamageListener implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if(event.isCancelled())
            return;

        if(!(event.getEntity() instanceof Player))
            return;

        Timer protection = Main.getInstance().getServerHandler().getProtectionTimer();
        if(protection == null || protection.getTime() < 0)
            return;

        event.setCancelled(true);
        event.setDamage(0.0D);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if (event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;

        Timer protection = Main.getInstance().getServerHandler().getProtectionTimer();
        if(protection != null && protection.getTime() > 0)
        {
            event.getDamager().sendMessage(ChatColor.RED + "You cannot attack players while protection is active.");
            event.setCancelled(true);
            event.setDamage(0.0D);
            return;
        }

        Player damaged = (Player) event.getEntity();

        Timer timer = Main.getInstance().getTimerHandler().getTimer(damaged, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
            timer.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            Main.getInstance().getTimerHandler().addTimer(damaged, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damaged));

        if (!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        if (Main.getInstance().getServerHandler().getPvpProtected().contains(damager.getUniqueId()))
        {
            event.setCancelled(true);
            return;
        }

        Timer timer1 = Main.getInstance().getTimerHandler().getTimer(damager, TimerType.COMBAT_TAG);
        if (timer1 != null && timer1.getTime() > 0)
            timer1.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            Main.getInstance().getTimerHandler().addTimer(damager, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damager));
    }
}
