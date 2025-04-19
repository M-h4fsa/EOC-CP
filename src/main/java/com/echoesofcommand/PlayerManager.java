package com.echoesofcommand;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class PlayerManager {
    private static final String PLAYERS_FILE = "players.json";
    private Map<String, PlayerRecord> records;

    public PlayerManager() {
        this.records = loadRecords();
    }

    private Map<String, PlayerRecord> loadRecords() {
        try (Reader r = new FileReader(PLAYERS_FILE)) {
            Type type = new TypeToken<Map<String, PlayerRecord>>(){}.getType();
            Map<String, PlayerRecord> map = new Gson().fromJson(r, type);
            return map != null ? map : new HashMap<>();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public PlayerRecord login(String username) {
        return records.computeIfAbsent(username, PlayerRecord::new);
    }

    public PlayerRecord registerUser(String username) {
        if (records.containsKey(username)) {
            throw new IllegalArgumentException(
                    "Username '" + username + "' already exists. Please choose another."
            );
        }
        PlayerRecord rec = new PlayerRecord(username);
        records.put(username, rec);
        return rec;
    }

    public void save() {
        try (Writer w = new FileWriter(PLAYERS_FILE)) {
            new Gson().toJson(records, w);
        } catch (IOException e) {
            System.err.println("Failed to save players: " + e.getMessage());
        }
    }

    public List<PlayerRecord> leaderboard() {
        List<PlayerRecord> list = new ArrayList<>(records.values());
        list.sort(Comparator
                .comparingInt(PlayerRecord::getBestScore).reversed()
                .thenComparingLong(PlayerRecord::getBestTimeMillis)
        );
        return list;
    }
}
