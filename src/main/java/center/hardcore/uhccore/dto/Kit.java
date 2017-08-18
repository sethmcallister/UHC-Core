package center.hardcore.uhccore.dto;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.encoder.ItemStackEncoder;
import center.hardcore.uhccore.util.ItemStackUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Kit
{
    private final String name;
    private final long coins;
    private final List<ItemStack> armor;
    private final List<ItemStack> items;
    private final ItemStack icon;
    private final YamlConfiguration configuration;
    private boolean deleted;

    public Kit(final String name, final long coins, final List<ItemStack> armor, final List<ItemStack> items, final ItemStack icon)
    {
        this.name = name;
        this.coins = coins;
        this.armor = armor;
        this.items = items;
        this.deleted = false;
        this.icon = icon.clone();
        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c") + name);
        this.icon.setItemMeta(meta);
        this.configuration = YamlConfiguration.loadConfiguration(getSaveFile());
    }

    public String getName()
    {
        return name;
    }

    public long getCooldown() {
        return coins;
    }

    public List<ItemStack> getItems()
    {
        return items;
    }

    public ItemStack getIcon()
    {
        return icon;
    }

    public List<ItemStack> getArmor()
    {
        return armor;
    }

    public void applyKit(Player player)
    {
        if (!player.hasPermission("kit.use." + name.toLowerCase()) && !player.hasPermission("kit.use.*"))
        {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to use this kit."));
            return;
        }

        this.items.stream().filter(Objects::nonNull).forEachOrdered(itemStack -> player.getInventory().addItem(itemStack));

        this.armor.forEach(itemStack -> {
            if (ItemStackUtils.isHelmet(itemStack))
                player.getInventory().setHelmet(itemStack);
            else if (ItemStackUtils.isChest(itemStack))
                player.getInventory().setChestplate(itemStack);
            else if (ItemStackUtils.isLegs(itemStack))
                player.getInventory().setLeggings(itemStack);
            else if (ItemStackUtils.isBoots(itemStack))
                player.getInventory().setBoots(itemStack);
        });

        player.updateInventory();
    }

    public void save()
    {
        this.configuration.set("name", this.name);
        this.configuration.set("coins", this.coins);

        List<String> armorList = this.armor.stream().map(ItemStackEncoder::encode).collect(Collectors.toList());
        List<String> itemList = this.items.stream().map(ItemStackEncoder::encode).collect(Collectors.toList());

        this.configuration.set("armor", armorList);
        this.configuration.set("items", itemList);
        this.configuration.set("icon", ItemStackEncoder.encode(icon));
        this.configuration.set("deleted", this.deleted);
        try
        {
            this.configuration.save(getSaveFile());
        }
        catch (IOException e)
        {
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    private File getSaveFile()
    {
        File file = new File(Main.getInstance().getDataFolder() + File.separator + "kits", this.name + ".yml");
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        return file;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(final boolean deleted)
    {
        this.deleted = deleted;
    }
}
