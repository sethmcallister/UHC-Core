package center.hardcore.uhccore.command;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Wout on 10/08/2017.
 */
public class KDRCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
        {
            return false;
        }

        Player player = (Player) sender;

        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("factions");

        Double kills = profile.getDouble("kills");
        Double deaths = profile.getDouble("deaths");

        if (deaths == 0)
        {
            sender.sendMessage(ChatColor.YELLOW + "Your current KDR is " + ChatColor.GOLD + kills.intValue());
        }
        else
        {
            sender.sendMessage(ChatColor.YELLOW + "Your current KDR is " + ChatColor.GOLD + round(kills / deaths));
        }

        return true;
    }

    private double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
