package center.hardcore.uhccore.dto;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import com.skygrind.core.framework.user.CoreUserManager;
import net.techcable.npclib.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.UUID;

public class CombatEntry extends BukkitRunnable implements Listener
{
    private UUID uuid;
    private String name;
    private NPC npc;
    private BukkitTask bukkitTask;
    private int i = 30;

    public CombatEntry(Player player)
    {
        this.uuid = player.getUniqueId();
        this.name = player.getName();

        this.npc = Main.getInstance().getCombatLogHandler().getNpcRegistry().createNPC(EntityType.PLAYER, this.uuid, this.name);

        this.npc.spawn(player.getLocation());
        this.npc.setProtected(false);

        Player npcPlayer = (Player) this.npc.getEntity();

        npcPlayer.getInventory().setContents(player.getInventory().getContents());
        npcPlayer.getInventory().setArmorContents(player.getInventory().getArmorContents());
        npcPlayer.setTotalExperience(player.getTotalExperience());

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public UUID getUuid()
    {
        return this.uuid;
    }

    public String getName()
    {
        return this.name;
    }

    public NPC getNpc()
    {
        return this.npc;
    }

    public BukkitTask getBukkitTask()
    {
        return this.bukkitTask;
    }

    public void removeNPC()
    {
        this.npc.despawn();
        Main.getInstance().getCombatLogHandler().getNpcRegistry().deregister(this.npc);
    }

    public void updateTimer()
    {
        this.i = 30;
    }

    @Override
    public void run()
    {
        if (this.i > 0)
            this.i -= 1;
        else
            cancel();
    }

    @Override
    public void cancel()
    {
        removeNPC();
        super.cancel();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        if (!Main.getInstance().getCombatLogHandler().getNpcRegistry().isNPC(event.getEntity()))
            return;

        NPC npc = Main.getInstance().getCombatLogHandler().getNpcRegistry().getAsNPC(event.getEntity());

        if (!Objects.equals(npc, getNpc()))
            return;

        Player killed = event.getEntity();

        CoreUserManager coreUserManager = (CoreUserManager) API.getUserManager();
        User killedUser = coreUserManager.getUserDataDriver().findById(uuid);
        Profile killedProfile = killedUser.getProfile("factions");
        killedUser.update();

        Double deaths = killedProfile.getDouble("deaths");
        deaths++;
        killedProfile.set("deaths", deaths);

        Timer timer = Main.getInstance().getTimerHandler().getTimer(killed, TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
        {
            timer.setTime(0L);
            Main.getInstance().getTimerHandler().getPlayerTimers(killed).remove(timer);
        }

        Player killer = killed.getKiller();
        if (killer == null)
        {
            Bukkit.getLogger().info(killed.getName() + " has died!");
            return;
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + killed.getName() + "&7(Combat-Logger) &4[" + killedProfile.getDouble("kills").intValue() + "]&e was killed by &c" + killer.getName() + "&4[" + killedProfile.getDouble("kills").intValue() + "]"));
        killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have killed &f" + killed.getName() + "&e."));
        Bukkit.getLogger().info(killed.getName() + " was killed by " + killer.getName());

        User killerUser = API.getUserManager().findByUniqueId(killer.getUniqueId());
        Profile killerProfile = killerUser.getProfile("factions");

        Double kills = killerProfile.getDouble("kills");
        kills++;
        killerProfile.set("kills", kills);

        Double killstreak = killerProfile.getDouble("killstreak");
        if (killstreak == null)
            killstreak = 0D;
        killstreak++;
        killerProfile.set("killstreak", killstreak);

        if (killstreak == 3)
        {
            killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 3));
            killer.sendMessage(ChatColor.YELLOW + "You have been given " + ChatColor.GREEN + "3x Golden Apple" + ChatColor.YELLOW + " for your killstreak.");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a" + killer.getName() + "&e has been given &a3x Golden Apples&e for their&a 3 killstreak&e."));
        }
        else if (killstreak == 5)
        {
            PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 2);
            killer.addPotionEffect(effect);
            killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been given &a5 seconds of Regeneration 3&e for your killstreak."));
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&a" + killer.getName() + "&e has been given &a5 seconds of Regeneration 3&e for their&a 5 killstreak&e."));
        }
        cancel();
    }
}
