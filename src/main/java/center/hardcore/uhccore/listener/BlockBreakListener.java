package center.hardcore.uhccore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener
{
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(event.isCancelled())
            return;

        switch (event.getBlock().getType())
        {
            case IRON_ORE:
            {
                break;
            }
        }
    }
}
