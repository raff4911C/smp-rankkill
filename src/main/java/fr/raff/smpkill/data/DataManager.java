package fr.raff.smpkill.data;

import net.minecraft.server.MinecraftServer;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DataManager {
    private static final Map<UUID, PlayerData> players = new HashMap<>();
    private static final Gson gson = new Gson();
    
    public static PlayerData get(UUID uuid, String name) {
        return players.computeIfAbsent(uuid, k -> new PlayerData(uuid, name));
    }
    
    public static Collection<PlayerData> getAll() {
        return players.values();
    }
    
    public static void addKill(UUID uuid, String name) {
        PlayerData d = get(uuid, name);
        d.kills++;
    }

    public static void load(MinecraftServer server) {
        try {
            Path file = server.getSavePath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("smpkill_data.json");
            if (Files.exists(file)) {
                String json = Files.readString(file);
                Map<String, PlayerData> map = gson.fromJson(json, new TypeToken<Map<String, PlayerData>>(){}.getType());
                players.clear();
                for (PlayerData d : map.values()) players.put(d.uuid, d);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void save(MinecraftServer server) {
        try {
            Path file = server.getSavePath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("smpkill_data.json");
            Files.writeString(file, gson.toJson(players));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
