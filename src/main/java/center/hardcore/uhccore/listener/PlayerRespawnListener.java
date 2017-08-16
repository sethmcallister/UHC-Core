package center.hardcore.uhccore.listener;

import center.hardcore.uhccore.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener
{
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        event.setRespawnLocation(Main.getInstance().getSpawnLocation());
    }
}
