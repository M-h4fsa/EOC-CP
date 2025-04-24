package com.echoesofcommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's record, including scores, times, and login history.
 */
public class PlayerRecord {
    private String username;
    private int bestSingleScore = 0;
    private long bestSingleTimeMillis = Long.MAX_VALUE;
    private int bestSequentialScore = 0;
    private long bestSequentialTimeMillis = Long.MAX_VALUE;
    private List<Long> loginHistory;
    private int totalLevelsPlayed = 0;
    private int totalCorrectChoices = 0;
    private long totalTimeMillis = 0;

    /**
     * Default constructor for JSON deserialization.
     */
    public PlayerRecord() {
        this.loginHistory = new ArrayList<>();
    }

    /**
     * Creates a new player record with the given username.
     * @param username The player's username.
     */
    public PlayerRecord(String username) {
        this.username = username;
        this.loginHistory = new ArrayList<>();
    }

    /**
     * Records a game session's score and time.
     * @param score The score achieved.
     * @param timeMillis The time taken in milliseconds.
     * @param sequential True if sequential mode, false if single-leader mode.
     */
    public void recordSession(int score, long timeMillis, boolean sequential) {
        if (sequential) {
            if (score > bestSequentialScore
                    || (score == bestSequentialScore && timeMillis < bestSequentialTimeMillis)) {
                bestSequentialScore = score;
                bestSequentialTimeMillis = timeMillis;
            }
        } else {
            if (score > bestSingleScore
                    || (score == bestSingleScore && timeMillis < bestSingleTimeMillis)) {
                bestSingleScore = score;
                bestSingleTimeMillis = timeMillis;
            }
        }
    }

    /**
     * Records a login timestamp.
     * @param timestamp The login time in milliseconds since epoch.
     */
    public void recordLogin(long timestamp) {
        loginHistory.add(timestamp);
    }

    /**
     * Updates statistics after a game session.
     * @param levelsPlayed The number of levels played in the session.
     * @param correctChoices The number of correct choices made.
     * @param timeMillis The total time taken in milliseconds.
     */
    public void updateStatistics(int levelsPlayed, int correctChoices, long timeMillis) {
        this.totalLevelsPlayed += levelsPlayed;
        this.totalCorrectChoices += correctChoices;
        this.totalTimeMillis += timeMillis;
    }

    /**
     * Gets the list of login timestamps.
     * @return A list of login times in milliseconds.
     */
    public List<Long> getLoginHistory() {
        return new ArrayList<>(loginHistory);
    }

    /**
     * Gets the most recent login timestamp, or null if none exist.
     * @return The last login time in milliseconds, or null.
     */
    public Long getLastLogin() {
        return loginHistory.isEmpty() ? null : loginHistory.get(loginHistory.size() - 1);
    }

    /**
     * Gets the player's username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the best single-leader score.
     * @return The best single-leader score.
     */
    public int getBestSingleScore() {
        return bestSingleScore;
    }

    /**
     * Gets the best single-leader time.
     * @return The best single-leader time in milliseconds.
     */
    public long getBestSingleTimeMillis() {
        return bestSingleTimeMillis;
    }

    /**
     * Gets the best sequential score.
     * @return The best sequential score.
     */
    public int getBestSequentialScore() {
        return bestSequentialScore;
    }

    /**
     * Gets the best sequential time.
     * @return The best sequential time in milliseconds.
     */
    public long getBestSequentialTimeMillis() {
        return bestSequentialTimeMillis;
    }

    /**
     * Gets the overall best score (single or sequential).
     * @return The highest score.
     */
    public int getBestScore() {
        return Math.max(bestSingleScore, bestSequentialScore);
    }

    /**
     * Gets the best time for the highest score.
     * @return The best time in milliseconds.
     */
    public long getBestTimeMillis() {
        if (bestSequentialScore > bestSingleScore) {
            return bestSequentialTimeMillis;
        } else if (bestSequentialScore < bestSingleScore) {
            return bestSingleTimeMillis;
        } else {
            return Math.min(bestSequentialTimeMillis, bestSingleTimeMillis);
        }
    }

    /**
     * Gets the total number of levels played.
     * @return The total levels played.
     */
    public int getTotalLevelsPlayed() {
        return totalLevelsPlayed;
    }

    /**
     * Gets the total number of correct choices.
     * @return The total correct choices.
     */
    public int getTotalCorrectChoices() {
        return totalCorrectChoices;
    }

    /**
     * Gets the total time spent playing in milliseconds.
     * @return The total time in milliseconds.
     */
    public long getTotalTimeMillis() {
        return totalTimeMillis;
    }

    /**
     * Calculates the accuracy as a percentage.
     * @return The accuracy percentage, or 0 if no levels played.
     */
    public double getAccuracy() {
        if (totalLevelsPlayed == 0) return 0.0;
        return (double) totalCorrectChoices / totalLevelsPlayed * 100;
    }

    /**
     * Calculates the average time per level in seconds.
     * @return The average time per level in seconds, or 0 if no levels played.
     */
    public double getAverageTimePerLevel() {
        if (totalLevelsPlayed == 0) return 0.0;
        return (double) totalTimeMillis / totalLevelsPlayed / 1000.0;
    }
}