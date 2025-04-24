package com.echoesofcommand;
import java.util.List;

public class Game {
    private final List<Leader> leaders;
    private final GameUI ui;
    private final PlayerRecord player;
    private final ArchiveManager archive;
    private final boolean sequential;
    private int score;
    private long startTime;

    public Game(List<Leader> leaders, GameUI ui, PlayerRecord player, ArchiveManager archive) {
        this.leaders = leaders;
        this.ui = ui;
        this.player = player;
        this.archive = archive;
        this.sequential = leaders.size() > 1;
    }

    public void start() {
        score = 0;
        startTime = System.currentTimeMillis();

        int totalLeaders = leaders.size();
        int totalLevels  = leaders.stream().mapToInt(l -> l.getLevels().size()).sum(); //to count 40 levels
        int leaderCount  = 0;

        for (Leader leader : leaders) {
            leaderCount++;
            if (sequential) {
                ui.displayLeaderSequence(leader.getName(), leaderCount, totalLeaders);
            }
            for (Level level : leader.getLevels()) {
                ui.displayLevel(level);
                int choice = ui.getPlayerChoice();
                if (choice == 1 || choice == 2) {
                    boolean correct = level.getChoices().get(choice-1).isHistorical();
                    if (correct) {
                        score++;
                    }
                    ui.displayResult(correct, level.getSummary());
                } else {
                    ui.displayTimeoutSkip();
                }
                archive.addEntry(leader.getName(), level);
                archive.saveToJson();
                ui.showProgress(score, totalLevels);
            }
        }

        long elapsed = System.currentTimeMillis() - startTime;
        ui.displayEndOfRound(score, totalLevels, elapsed);
        player.recordSession(score, elapsed, sequential);
    }
}
