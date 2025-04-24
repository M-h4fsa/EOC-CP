package com.echoesofcommand;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Manages player records, including registration, login, and leaderboard functionality.
 */
public class PlayerManager {
    private static final String PLAYERS_FILE = "players.json";
    private Map<String, PlayerRecord> records;

    /**
     * Initializes the PlayerManager by loading existing player records from file.
     */
    public PlayerManager() {
        this.records = loadRecords();
    }

    /**
     * Loads player records from the players.json file.
     * @return A map of usernames to PlayerRecord objects, or an empty map if loading fails.
     */
    private Map<String, PlayerRecord> loadRecords() {
        try (Reader reader = new FileReader(PLAYERS_FILE)) {
            Type type = new TypeToken<Map<String, PlayerRecord>>(){}.getType();
            Map<String, PlayerRecord> loaded = new Gson().fromJson(reader, type);
            return loaded != null ? loaded : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Warning: Could not load player records: " + e.getMessage() + ". Using empty records.");
            return new HashMap<>();
        }
    }

    /**
     * Logs in a player, creating a new record if the username doesn't exist, and records the login time.
     * @param username The player's username.
     * @return The PlayerRecord for the user.
     */
    public PlayerRecord login(String username) {
        PlayerRecord record = records.computeIfAbsent(username, k -> new PlayerRecord(username));
        record.recordLogin(System.currentTimeMillis());
        save();
        return record;
    }

    /**
     * Registers a new user, throwing an exception if the username is taken.
     * @param username The desired username.
     * @return The new PlayerRecord.
     * @throws IllegalArgumentException If the username already exists.
     */
    public PlayerRecord registerUser(String username) {
        if (records.containsKey(username)) {
            throw new IllegalArgumentException(
                    "Username '" + username + "' already exists. Please choose another one."
            );
        }
        PlayerRecord record = new PlayerRecord(username);
        record.recordLogin(System.currentTimeMillis());
        records.put(username, record);
        save();
        return record;
    }

    /**
     * Saves player records to the players.json file.
     */
    public void save() {
        try (Writer writer = new FileWriter(PLAYERS_FILE)) {
            new Gson().toJson(records, writer);
        } catch (IOException e) {
            System.err.println("Error: Failed to save player records: " + e.getMessage());
        }
    }

    /**
     * Generates a sorted leaderboard based on best scores and times.
     * @return A sorted list of PlayerRecord objects.
     */
    public List<PlayerRecord> leaderboard() {
        List<PlayerRecord> list = new ArrayList<>(records.values());
        list.sort(Comparator.comparingInt(PlayerRecord::getBestScore)
                .reversed()
                .thenComparingLong(PlayerRecord::getBestTimeMillis));
        return list;
    }
}