package fr.raff.smpkill.data;

import java.util.UUID;

public class PlayerData {
    public UUID uuid;
    public String name;
    public int kills = 0;
    public boolean hasBounty = false;

    public PlayerData(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Rank getRank() {
        return Rank.getRankForKills(kills);
    }
}
