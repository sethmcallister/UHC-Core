package center.hardcore.uhccore.listener;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.DefaultTimer;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class PlayerInteractListener implements Listener
{
    @EventHandler
    public void onEnderpearlClick(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL))
        {
            if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
            {
                Timer timer = Main.getInstance().getTimerHandler().getTimer(player, TimerType.ENDERPEARL);
                if (timer != null && timer.getTime() > 0)
                {
                    long millisLeft = timer.getTime();
                    double value = millisLeft / 1000.0D;
                    double sec = Math.round(10.0D * value) / 10.0D;
                    event.setCancelled(true);
                    event.getPlayer().updateInventory();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot use this for another &c&l" + sec + "&c seconds."));
                    return;
                }
                Main.getInstance().getTimerHandler().addTimer(player, new DefaultTimer(TimerType.ENDERPEARL, 16000 + System.currentTimeMillis(), player));
            }
        }
    }
}
