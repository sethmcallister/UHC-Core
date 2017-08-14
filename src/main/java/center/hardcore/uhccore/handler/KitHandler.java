package center.hardcore.uhccore.handler;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.Kit;
import center.hardcore.uhccore.dto.encoder.ItemStackEncoder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class KitHandler
{
    private final List<Kit> kits;
    private final Queue<UUID> kitApplicationQueue;
    private final Map<UUID, Kit> kitApplication;

    public KitHandler()
    {
        this.kits = Collections.synchronizedList(new ArrayList<>());
        this.kitApplicationQueue = new PriorityQueue<>();
        this.kitApplication = new HashMap<>();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (final UUID uuid : kitApplicationQueue)
                {
                    Kit kit = kitApplication.get(uuid);
                    if(kit == null)
                    {
                        kitApplicationQueue.remove(uuid);
                        kitApplication.remove(uuid);
                        continue;
                    }
                    kit.applyKit(Bukkit.getPlayer(uuid));
                    kitApplicationQueue.remove(uuid);
                    kitApplication.remove(uuid);
                }
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }

    public void loadKits()
    {
        final File folder = new File(Main.getInstance().getDataFolder(), "kits");
        if (!folder.exists())
        {
            folder.mkdir();
            return;
        }

        File[] directoryListing = folder.listFiles();
        if (directoryListing != null)
        {
            for (File child : directoryListing)
            {
                if (!child.isFile())
                    continue;

                if (!child.getName().endsWith(".yml"))
                    continue;

                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(child);
                String name = configuration.getString("name");

                int coins = configuration.getInt("coins");

                List<?> armorShit = configuration.getList("armor");
                List<ItemStack> armor = armorShit.stream().map(string -> ItemStackEncoder.decode((String) string)).collect(Collectors.toList());

                List<?> itemShit = configuration.getList("items");
                List<ItemStack> items = itemShit.stream().map(string -> ItemStackEncoder.decode((String) string)).collect(Collectors.toList());

                String iconShit = configuration.getString("icon");
                ItemStack itemStack = ItemStackEncoder.decode(iconShit);

                Boolean deleted = configuration.getBoolean("deleted");

                Kit kit = new Kit(name, coins, armor, items, itemStack);
                kit.setDeleted(deleted);
                getKits().add(kit);
            }
        }
    }

    public List<Kit> getKits()
    {
        return kits;
    }

    public Kit findByName(final String name)
    {
        return this.kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public void addPlayerToQueue(UUID uuid, Kit kit)
    {
        this.kitApplication.put(uuid, kit);
        this.kitApplicationQueue.add(uuid);
    }
}
