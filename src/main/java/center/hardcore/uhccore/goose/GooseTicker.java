package center.hardcore.uhccore.goose;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.Timer;
import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

public class GooseTicker extends BukkitRunnable
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

    private ConcurrentSkipListSet<String> splitEqually(final String text, final int size)
    {
        ConcurrentSkipListSet<String> ret = new ConcurrentSkipListSet<>();

        for (int start = 0; start < text.length(); start += size)
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        return ret;
    }

    public static String formatTime(long time)
    {
        if (time > 60000L)
            return setLongFormat(time);
        else
            return format(time);
    }

    private static String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private static String setLongFormat(long paramMilliseconds)
    {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
            return FORMAT.format(paramMilliseconds);
        return DurationFormatUtils.formatDuration(paramMilliseconds,
                                                  (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") +
                                                  "mm:ss");
    }

    private String translateString(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            GooseScoreboard scoreboard = Main.getInstance().getGooseHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            scoreboard.clear();

            if (Main.getInstance().getTimerHandler().getPlayerTimers(player) == null)
            {
                Main.getInstance().getTimerHandler().getTimers().put(player.getUniqueId(), new LinkedList<>());
                scoreboard.update();
                continue;
            }

            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            Profile profile = user.getProfile("factions");
            scoreboard.add(translateString("&7&m---------"), translateString("&7&m---------"));
            scoreboard.add(translateString("&c&lKills&7: "), translateString("&f" + profile.getDouble("kills").intValue()));
            scoreboard.add(translateString("&c&lDeaths&7: "), translateString("&f" + profile.getDouble("deaths").intValue()));
            if(profile.getDouble("killstreak") != null && profile.getDouble("killstreak") > 0D)
                scoreboard.add(translateString("&c&lKill Streak"), translateString("&7: &f") + profile.getDouble("killstreak").intValue());

            if (hasAnyTimers(player))
            {
                scoreboard.add("", "");
                List<Timer> defaultTimers = Main.getInstance().getTimerHandler().getPlayerTimers(player);
                for (Timer timer : defaultTimers)
                {
                    if (timer.getTime() > 0)
                    {
                        String left = translateString(timer.getTimerType().getScore());
                        String right = translateString("&7:&f ") + formatTime(timer.getTime());
                        scoreboard.add(left, right);
                    }
                }
            }
            scoreboard.add("", "");
            scoreboard.add(translateString("&6kitpvp.rip"), translateString("&6/store"));
            scoreboard.add(translateString("&7&m---------"), translateString("&7&m---------"));
            scoreboard.update();
        }
    }


    private boolean hasAnyTimers(Player player)
    {
        return Main.getInstance().getTimerHandler().getPlayerTimers(player).stream().filter(timer -> timer.getTime() > 0).count() > 0;
    }
}
