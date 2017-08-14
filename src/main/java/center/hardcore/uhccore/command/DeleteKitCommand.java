package center.hardcore.uhccore.command;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DeleteKitCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        if (!sender.hasPermission("kitpvp.deletekit"))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
            return true;
        }
        if (args.length != 1)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /deletekit <Name>");
            return true;
        }
        String kitName = args[0];
        Kit kit = Main.getInstance().getKitHandler().findByName(args[0]);
        if (kit == null)
        {
            sender.sendMessage(ChatColor.RED + "No kit with the name '" + kitName + "' could be found.");
            return true;
        }
        Main.getInstance().getKitHandler().getKits().remove(kit);
        sender.sendMessage(ChatColor.YELLOW + "You have successfully deleted the kit " + ChatColor.GREEN + kitName + ChatColor.YELLOW + ".");
        Bukkit.getLogger().info(sender.getName() + " has deleted the kit " + kitName + ".");
        return true;
    }
}
