package com.echoesofcommand;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.Writer;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ArchiveManager {
    private List<ArchiveEntry> archive = new ArrayList<>();
    private static final String ARCHIVE_FILE = "archive.json";

    public void addEntry(String leader, Level level) {
        String histChoice = level.getChoices().stream()
                .filter(Choice::isHistorical)
                .findFirst()
                .map(Choice::getText)
                .orElse("");
        archive.add(new ArchiveEntry(
                leader,
                level.getNumber(),
                level.getDescription(),
                histChoice,
                level.getSummary()
        ));
    }

    public void saveToJson() {
        try (Writer writer = new FileWriter(ARCHIVE_FILE)) {
            new Gson().toJson(archive, writer);
        } catch (Exception e) {
            System.err.println("Failed to save archive: " + e.getMessage());
        }
    }

    public void promptSearch() {
        if (archive.isEmpty()) {
            System.out.println("[Your archive is empty. Complete levels to build your archive!]");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("Search your archive now? (yes/no): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("yes")) return;
        System.out.print("Enter keyword to search: ");
        String keyword = sc.nextLine().toLowerCase();
        List<ArchiveEntry> results = archive.stream()
                .filter(e -> e.description.toLowerCase().contains(keyword)
                          || e.leader.toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        if (results.isEmpty()) {
            System.out.println("[No results found. Try a different keyword or play more levels.]");
            return;
        }
        System.out.println("\n=== Archive Search Results ===");
        for (ArchiveEntry e : results) {
            System.out.println("Leader: " + e.leader);
            System.out.println("Level " + e.levelNumber + ": " + e.description);
            System.out.println("Historical Decision: " + e.historicalChoice);
            System.out.println("Summary: " + e.summary + "\n");
        }
    }

    static class ArchiveEntry {
        String leader;
        int levelNumber;
        String description;
        String historicalChoice;
        String summary;
        public ArchiveEntry(String leader, int levelNumber, String description, String historicalChoice, String summary) {
            this.leader = leader;
            this.levelNumber = levelNumber;
            this.description = description;
            this.historicalChoice = historicalChoice;
            this.summary = summary;
        }
    }
}
