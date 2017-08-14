package center.hardcore.uhccore.listener;

import center.hardcore.uhccore.Main;
import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &eWelcome to &fFactions&e."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &eStore:&f https://kitpvp.rip/store"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &eWebsite:&f https://kitpvp.rip"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', " &eSupport:&f ts.kitpvp.rip"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));

                Profile profile = user.getProfile("factions");
                if(profile == null)
                {
                    profile = new Profile("factions");
                    profile.set("kills", 0D);
                    profile.set("deaths", 0D);
                    profile.set("killstreak", 0D);
                    user.getAllProfiles().add(profile);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',  "&eWelcome &f" + player.getName() + "&e to &fFactions&e."));
                }
            }
        }.runTaskAsynchronously(Main.getInstance());

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                user.update();
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 20L);
    }
}
