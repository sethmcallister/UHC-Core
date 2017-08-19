package center.hardcore.uhccore.handler;

import center.hardcore.uhccore.timer.Timer;
import center.hardcore.uhccore.timer.TimerType;

import java.util.*;

public class ServerHandler
{
    private final Integer spawnRadius;
    private final Set<UUID> spectators;
    private final Set<UUID> pvpProtected;
    private final List<Timer> globalTimers;

    public ServerHandler()
    {
        this.spawnRadius = 10;
        this.spectators = new HashSet<>();
        this.pvpProtected = new HashSet<>();
        this.globalTimers = new ArrayList<>();
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

    public List<Timer> getGlobalTimers()
    {
        return globalTimers;
    }

    public Timer getProtectionTimer()
    {
        return this.globalTimers.stream().filter(timer -> timer.getTimerType().equals(TimerType.PROTECTION)).findFirst().orElse(null);
    }
}
