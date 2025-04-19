package com.echoesofcommand;

import java.util.List;

public interface GameUI {
    void displayWelcomeMessage();
    String promptUsername();
    void searchDisabledNotice();
    int promptPlayMode();
    Leader selectLeader(List<Leader> leaders);
    void displayLeaderSequence(String leaderName, int index, int total);
    void displayLevel(Level level);
    int getPlayerChoice();
    void displayTimeoutSkip();
    void displayResult(boolean correct, String summary);
    void showProgress(int score, int total);
    void displayEndOfRound(int score, int total, long timeMillis);
    boolean promptArchiveSearch();
    int promptPostRoundOption();
    void displayLeaderboard(List<PlayerRecord> list);
    void displayGoodbyeMessage();
}
