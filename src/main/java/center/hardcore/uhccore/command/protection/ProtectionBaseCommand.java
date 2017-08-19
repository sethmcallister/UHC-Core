package center.hardcore.uhccore.command.protection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.commands.SubCommand;

import java.util.Collections;

public class ProtectionBaseCommand extends SubCommand
{
    public ProtectionBaseCommand()
    {
        super("help", Collections.emptyList(), true);
    }

    @Override
    public void execute(final Player player, final String[] args)
    {
        if(!player.hasPermission("protection.help") || !player.hasPermission("protection.*"))
        {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
        player.sendMessage(ChatColor.YELLOW + "Protection Help " + ChatColor.WHITE + "Page");
        player.sendMessage(ChatColor.YELLOW + " /protection start " + ChatColor.WHITE + "Start the protection timer, for a given time.");
        player.sendMessage(ChatColor.YELLOW + " /protection stop " + ChatColor.WHITE + "Stop the current the protection timer.");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
    }
}
