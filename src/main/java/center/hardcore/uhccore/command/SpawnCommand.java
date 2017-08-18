package center.hardcore.uhccore.command;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Wout on 10/08/2017.
 */
public class SpawnCommand implements CommandExecutor
{

    public static final List<UUID> TELEPORTING = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only players are allowed to execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (Main.getInstance().getTimerHandler().hasTimer(player, TimerType.COMBAT_TAG))
        {
            sender.sendMessage(ChatColor.RED + "You cannot teleport back to spawn while in combat tag!");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Teleporting in 5 seconds... don't get combat tagged!");

        TELEPORTING.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (player == null || !player.isOnline() || !TELEPORTING.contains(player.getUniqueId()))
                return;

            if (Main.getInstance().getTimerHandler().hasTimer(player, TimerType.COMBAT_TAG))
            {
                sender.sendMessage(ChatColor.RED + "You cannot teleport back to spawn while in combat tag!");
                return;
            }

            TELEPORTING.remove(player.getUniqueId());

            player.teleport(Main.getInstance().getSpawnLocation());
            Main.getInstance().getServerHandler().getPvpProtected().add(player.getUniqueId());
        }, 100L);

        return true;
    }
}
