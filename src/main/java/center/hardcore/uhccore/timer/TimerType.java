package center.hardcore.uhccore.timer;

public enum TimerType
{
    COMBAT_TAG("&c&lSpawn Tag"),
    ENDERPEARL("&e&lEnderpearl");

    private String score;

    TimerType(String score)
    {
        this.score = score;
    }

    public String getScore()
    {
        return score;
    }
}