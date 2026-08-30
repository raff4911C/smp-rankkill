package fr.raff.smpkill.data;

import net.minecraft.scoreboard.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RankManager {
    public static void init() { DataManager.load(); }

    public static void onPlayerJoin(ServerPlayerEntity player) {
        PlayerData data = DataManager.getOrCreate(player.getUuid(), player.getName().getString());
        data.rank = Rank.getRankForKills(data.kills);
        updateDisplay(player, data);
        BossBarManager.addPlayer(player);
    }

    public static void addKill(ServerPlayerEntity killer) {
        PlayerData data = DataManager.getOrCreate(killer.getUuid(), killer.getName().getString());
        data.kills++;
        data.rank = Rank.getRankForKills(data.kills);
        DataManager.save();
        updateDisplay(killer, data);
        BossBarManager.updatePlayer(killer);
    }

    public static void updateDisplay(ServerPlayerEntity player, PlayerData data) {
        Scoreboard sc = player.getServer().getScoreboard();
        String teamName = "RANK_" + data.rank.name();
        if (teamName.length() > 16) teamName = teamName.substring(0,16);
        Team team = sc.getTeam(teamName);
        if (team == null) {
            team = sc.addTeam(teamName);
            team.setPrefix(Text.literal("[" + data.rank.displayName + "] ").formatted(Formatting.GRAY));
        }
        sc.addScoreHolderToTeam(player.getName().getString(), team);
        player.setPlayerListName(Text.literal("[" + data.rank.displayName + "] " + player.getName().getString()));

        ScoreboardObjective obj = sc.getObjective("rank_below");
        if (obj == null) {
            obj = sc.addObjective("rank_below", ScoreboardCriterion.DUMMY, Text.literal("Rank"), ScoreboardCriterion.RenderType.INTEGER);
            sc.setObjectiveSlot(ScoreboardDisplaySlot.BELOW_NAME, obj);
        }
        obj.setDisplayName(Text.literal(data.rank.displayName));
        sc.getOrCreateScore(player, obj).setScore(data.kills);
    }
}
