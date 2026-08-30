package fr.raff.smpkill.data;

public enum Rank {
    NONE(0, "§7", ""),
    LT5(2, "§8", "LT5"),
    LT4(7, "§a", "LT4"),
    HT4(15, "§2", "HT4"),
    LT3(25, "§e", "LT3"),
    HT3(40, "§6", "HT3"),
    LT2(60, "§c", "LT2"),
    HT2(85, "§4", "HT2"),
    LT1(100, "§5", "LT1"),
    HT1(150, "§d§l", "HT1");

    public final int killsRequired;
    public final String color;
    public final String displayName;

    Rank(int kills, String color, String display) {
        this.killsRequired = kills;
        this.color = color;
        this.displayName = display;
    }

    public static Rank getRankForKills(int kills) {
        Rank current = NONE;
        for (Rank r : values()) {
            if (kills >= r.killsRequired && r.killsRequired >= current.killsRequired) {
                current = r;
            }
        }
        return current;
    }
}
