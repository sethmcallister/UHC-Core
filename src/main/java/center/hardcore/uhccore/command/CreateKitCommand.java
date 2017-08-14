package center.hardcore.uhccore.command;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateKitCommand implements CommandExecutor
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
        if (!sender.hasPermission("kitpvp.createkit"))
        {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
            return true;
        }
        if (args.length != 2)
        {
            sender.sendMessage(ChatColor.RED + "Usage: /createkit <name> <coins>");
            return true;
        }
        String kitName = args[0];
        String strCoins = args[1];

        if (Main.getInstance().getKitHandler().findByName(kitName) != null)
        {
            sender.sendMessage(ChatColor.RED + "A kit with the name '" + kitName + "' already exists.");
            return true;
        }

        int coins = -1;
        try {
            coins = Integer.parseInt(strCoins);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid number for coins!");
            return true;
        }

        if (coins < 0) {
            sender.sendMessage(ChatColor.RED + "Invalid number for coins!");
            return true;
        }

        List<ItemStack> armor = Collections.synchronizedList(new ArrayList<>());
        armor.addAll(Arrays.asList(player.getInventory().getArmorContents()));

        List<ItemStack> items = Collections.synchronizedList(new ArrayList<>());
        Collections.addAll(items, player.getInventory().getContents());

        ItemStack icon = player.getItemInHand();

        Kit kit = new Kit(kitName, coins, armor, items, icon);
        Main.getInstance().getKitHandler().getKits().add(kit);
        sender.sendMessage(ChatColor.YELLOW + "You have successfully created the kit " + ChatColor.GREEN + kitName + ChatColor.YELLOW + ".");
        Bukkit.getLogger().info(sender.getName() + " has created a kit called " + kitName + ": " + Arrays.toString(armor.toArray()) + ", " + Arrays.toString(items.toArray()));
        return true;
    }
}
