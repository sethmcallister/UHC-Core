package center.hardcore.uhccore.command.protection;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.DefaultTimer;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.sethy.commands.SubCommand;

import java.util.Collections;

public class ProtectionStopCommand extends SubCommand
{
    public ProtectionStopCommand()
    {
        super("stop", Collections.emptyList(), true);
    }

    @Override
    public void execute(final Player player, final String[] args)
    {
        if(!player.hasPermission("protection.stop") || !player.hasPermission("protection.*"))
        {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return;
        }
        if(args.length != 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /protection start <time>");
            return;
        }
        Timer timer = findByType(TimerType.PROTECTION);
        if(timer == null)
        {
            player.sendMessage(ChatColor.RED + "Protection has not started.");
            return;
        }
        Main.getInstance().getServerHandler().getGlobalTimers().remove(timer);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&eProtection has stopped!"));
        Bukkit.broadcastMessage(" ");
    }

    private Timer findByType(TimerType timerType)
    {
        return Main.getInstance().getServerHandler().getGlobalTimers().stream().filter(timer -> timer.getTimerType().equals(timerType)).findFirst().orElse(null);
    }
}
