package center.hardcore.uhccore.handler;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ServerHandler
{
    private final Integer spawnRadius;
    private final Set<UUID> spectators;
    private final Set<UUID> pvpProtected;

    public ServerHandler()
    {
        this.spawnRadius = 10;
        this.spectators = new HashSet<>();
        this.pvpProtected = new HashSet<>();
    }

    public Integer getSpawnRadius()
    {
        return spawnRadius;
    }

    public Set<UUID> getSpectators()
    {
        return spectators;
    }

    public Set<UUID> getPvpProtected()
    {
        return pvpProtected;
    }
}
