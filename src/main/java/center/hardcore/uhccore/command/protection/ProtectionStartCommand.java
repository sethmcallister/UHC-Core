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

public class ProtectionStartCommand extends SubCommand
{
    public ProtectionStartCommand()
    {
        super("start", Collections.emptyList(), true);
    }

    @Override
    public void execute(final Player player, final String[] args)
    {
        if(!player.hasPermission("protection.start") || !player.hasPermission("protection.*"))
        {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return;
        }
        if(args.length != 1)
        {
            player.sendMessage(ChatColor.RED + "Usage: /protection start <time>");
            return;
        }
        String timeSTR = args[0];
        long time = getTimeFromString(timeSTR);
        Timer timer = new DefaultTimer(TimerType.PROTECTION, time + System.currentTimeMillis(), null);
        Main.getInstance().getServerHandler().getGlobalTimers().add(timer);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&eProtection has started!"));
        Bukkit.broadcastMessage(" ");
    }

    private long getTimeFromString(String a)
    {
        if (a.endsWith("s"))
            return Long.valueOf(a.substring(0, a.length() - 1)) * 1000L;
        if (a.endsWith("m"))
            return Long.valueOf(a.substring(0, a.length() - 1)) * 60000L;
        if (a.endsWith("h"))
            return Long.valueOf(a.substring(0, a.length() - 1)) * 3600000L;
        if (a.endsWith("d"))
            return Long.valueOf(a.substring(0, a.length() - 1)) * 86400000L;
        if (a.endsWith("m"))
            return Long.valueOf(a.substring(0, a.length() - 1)) * 2592000000L;
        if (a.endsWith("y"))
            return Long.valueOf(a.substring(0, a.length() - 1)) * 31104000000L;
        return -1L;
    }
}
