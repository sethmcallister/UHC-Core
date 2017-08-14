package center.hardcore.uhccore.command;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.Kit;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Only players are allowed to execute this command!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /kit <Name>");
            return true;
        }
        String kitName = args[0];
        Kit kit = Main.getInstance().getKitHandler().findByName(kitName);
        if (kit == null)
        {
            sender.sendMessage(ChatColor.RED + "No kit with the name '" + kitName + "' could be found.");
            return true;
        }

        if (!player.hasPermission("kit.use." + kit.getName().toLowerCase()) && !player.hasPermission("kit.use.*"))
        {
            sender.sendMessage(ChatColor.RED + "You don't have kit '" + kitName + "' available. Purchase it in our store or with ingame coins.");
            return true;
        }

        Timer timer = Main.getInstance().getTimerHandler().getTimer(player, TimerType.COMBAT_TAG);
        if(timer != null && timer.getTime() > 0)
        {
            sender.sendMessage(ChatColor.RED + "You cannot do this command while in combat tag.");
            return true;
        }

        Main.getInstance().getKitHandler().addPlayerToQueue(player.getUniqueId(), kit);
        return true;
    }
}
