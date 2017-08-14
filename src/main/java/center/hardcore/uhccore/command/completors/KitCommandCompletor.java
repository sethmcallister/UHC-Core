package center.hardcore.uhccore.command.completors;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitCommandCompletor implements TabCompleter
{
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        List<String> kitNames = Collections.synchronizedList(new ArrayList<>());
        Main.getInstance().getKitHandler().getKits().stream().filter(kit -> sender.hasPermission("kit.use." + kit.getName().toLowerCase()) || sender.hasPermission("kit.use.*")).map(Kit::getName).forEach(kitNames::add);
        return kitNames;
    }
}
