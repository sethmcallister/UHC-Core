package center.hardcore.uhccore.listener;

import center.hardcore.uhccore.command.SpawnCommand;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(event.getFrom().getX() == event.getTo().getX() || event.getFrom().getZ() == event.getTo().getZ())
            return;

        if(SpawnCommand.TELEPORTING.contains(event.getPlayer().getUniqueId()))
        {
            SpawnCommand.TELEPORTING.remove(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You have moved, and cancelled your current teleportation.");
        }
    }
}
