package center.hardcore.uhccore.handler;

import center.hardcore.uhccore.Main;
import center.hardcore.uhccore.dto.CombatEntry;
import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;
import net.techcable.npclib.NPC;
import net.techcable.npclib.NPCLib;
import net.techcable.npclib.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatLogHandler implements Listener
{
    private Map<UUID, CombatEntry> combatEntries;
    private NPCRegistry npcRegistry;
    private LinkedList<UUID> killedList;

    public CombatLogHandler()
    {
        this.combatEntries = new ConcurrentHashMap<>();
        this.npcRegistry = NPCLib.getNPCRegistry(Main.getInstance());
        this.killedList = new LinkedList<>();
        for (NPC npc : this.npcRegistry.listNpcs())
            npc.despawn();

        npcRegistry.deregisterAll();

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public NPCRegistry getNpcRegistry()
    {
        return this.npcRegistry;
    }

    public void addKilled(UUID uuid)
    {
        if (!this.killedList.contains(uuid))
            this.killedList.add(uuid);
    }

    public void removeKilled(UUID uuid)
    {
        if (this.killedList.contains(uuid))
            this.killedList.remove(uuid);
    }

    public UUID getByNPC(NPC npc)
    {
        return npc.getUUID();
    }

    private void createNPC(Player p)
    {
        if ((p.isDead()) || (!p.isValid()) || (p.getHealth() <= 0.0D))
            return;

        CombatEntry npcWrapper = new CombatEntry(p);
        npcWrapper.runTaskTimer(Main.getInstance(), 0L, 20L);

        this.combatEntries.put(p.getUniqueId(), npcWrapper);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        if (this.killedList.contains(event.getPlayer().getUniqueId()))
        {
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            Bukkit.getPluginManager().callEvent(new PlayerRespawnEvent(player, (Bukkit.getWorlds().get(0)).getSpawnLocation(), false));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYour Combat Logger was killed while you where offline."));
            this.killedList.remove(player.getUniqueId());
            return;
        }
        CombatEntry npcWrapper = this.combatEntries.get(event.getPlayer().getUniqueId());

        if (npcWrapper != null)
        {
            Main.getInstance().getServerHandler().getPvpProtected().remove(player.getUniqueId());
            Player npc = (Player) npcWrapper.getNpc().getEntity();
            if (npc == null)
            {
                player.teleport(Main.getInstance().getSpawnLocation());
                return;
            }

            player.getInventory().setContents(npc.getInventory().getContents());
            player.getInventory().setArmorContents(npc.getInventory().getArmorContents());
            player.setHealth(npc.getHealth());
            player.teleport(npcWrapper.getNpc().getEntity().getLocation());
            npcWrapper.cancel();
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event)
    {
        Timer timer = Main.getInstance().getTimerHandler().getTimer(event.getPlayer(), TimerType.COMBAT_TAG);
        if (timer != null && timer.getTime() > 0)
            createNPC(event.getPlayer());
    }

    public Map<UUID, CombatEntry> getCombatEntries()
    {
        return combatEntries;
    }
}
