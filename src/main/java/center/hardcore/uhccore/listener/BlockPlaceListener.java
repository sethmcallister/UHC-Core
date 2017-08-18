package center.hardcore.uhccore.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener
{
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getBlock().getLocation().getX() > 2970 && event.getBlock().getLocation().getZ() > 2970)
        {
            event.setBuild(false);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot build here.");
        }
    }
}
