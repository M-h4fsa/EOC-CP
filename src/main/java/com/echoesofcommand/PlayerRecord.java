package com.echoesofcommand;

public class PlayerRecord {
    private String username;
    private int bestSingleScore = 0;
    private long bestSingleTimeMillis = Long.MAX_VALUE;
    private int bestSequentialScore = 0;
    private long bestSequentialTimeMillis = Long.MAX_VALUE;

    public PlayerRecord() {}

    public PlayerRecord(String username) {
        this.username = username;
    }

    /**
     * Record a finished session.
     * @param score      number of correct answers
     * @param timeMillis elapsed time in milliseconds
     * @param sequential true if “play all leaders” mode
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

    public String getUsername() {
        return username;
    }
    public int getBestSingleScore() {
        return bestSingleScore;
    }
    public long getBestSingleTimeMillis() {
        return bestSingleTimeMillis;
    }
    public int getBestSequentialScore() {
        return bestSequentialScore;
    }
    public long getBestSequentialTimeMillis() {
        return bestSequentialTimeMillis;
    }

    /**
     * Overall best: the higher of single vs sequential.
     */
    public int getBestScore() {
        return Math.max(bestSingleScore, bestSequentialScore);
    }

    /**
     * Time corresponding to the overall best score.
     * If both modes tie, returns the quicker time.
     */
    public long getBestTimeMillis() {
        if (bestSequentialScore > bestSingleScore) {
            return bestSequentialTimeMillis;
        } else if (bestSequentialScore < bestSingleScore) {
            return bestSingleTimeMillis;
        } else {
            // scores are equal: pick the smaller time
            return Math.min(bestSequentialTimeMillis, bestSingleTimeMillis);
        }
    }
}
