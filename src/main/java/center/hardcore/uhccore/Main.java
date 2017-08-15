package center.hardcore.uhccore;

import center.hardcore.uhccore.command.*;
import center.hardcore.uhccore.command.completors.KitCommandCompletor;
import center.hardcore.uhccore.dto.Kit;
import center.hardcore.uhccore.goose.GooseHandler;
import center.hardcore.uhccore.handler.CombatLogHandler;
import center.hardcore.uhccore.handler.KitHandler;
import center.hardcore.uhccore.handler.ServerHandler;
import center.hardcore.uhccore.listener.*;
import center.hardcore.uhccore.timer.TimerHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    private static Main instance;
    private TimerHandler timerHandler;
    private ServerHandler serverHandler;
    private CombatLogHandler combatLogHandler;
    private KitHandler kitHandler;
    private Location spawnLocation;
    private GooseHandler gooseHandler;

    @Override
    public void onLoad()
    {
        setInstance(this);
        this.timerHandler = new TimerHandler();
        this.serverHandler = new ServerHandler();
        this.combatLogHandler = new CombatLogHandler();
        this.kitHandler = new KitHandler();
        this.gooseHandler = new GooseHandler();
    }

    @Override
    public void onEnable()
    {
        getKitHandler().loadKits();

        spawnLocation = new Location(Bukkit.getWorld("world"), 0, 70, 0);

        getCommand("kit").setExecutor(new KitCommand());
        getCommand("kit").setTabCompleter(new KitCommandCompletor());
        getCommand("createkit").setExecutor(new CreateKitCommand());
        getCommand("deletekit").setExecutor(new DeleteKitCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("kdr").setExecutor(new KDRCommand());


        getServer().getPluginManager().registerEvents(new PlayerDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PotionSplashListener(), this);
    }

    @Override
    public void onDisable()
    {
        getKitHandler().getKits().forEach(Kit::save);
    }

    public static synchronized Main getInstance()
    {
        return instance;
    }

    private static synchronized void setInstance(Main newInstance)
    {
        instance = newInstance;
    }

    public synchronized TimerHandler getTimerHandler()
    {
        return timerHandler;
    }

    public synchronized ServerHandler getServerHandler()
    {
        return serverHandler;
    }

    public synchronized Location getSpawnLocation()
    {
        return spawnLocation;
    }

    public synchronized CombatLogHandler getCombatLogHandler()
    {
        return combatLogHandler;
    }

    public synchronized KitHandler getKitHandler()
    {
        return kitHandler;
    }

    public synchronized GooseHandler getGooseHandler()
    {
        return gooseHandler;
    }
}
