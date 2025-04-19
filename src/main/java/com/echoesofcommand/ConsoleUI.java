package com.echoesofcommand;

import java.util.*;
import java.util.stream.Collectors;

public class ConsoleUI implements GameUI {
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void displayWelcomeMessage() {
        System.out.println("=== Echoes of Command ===");
    }

    @Override
    public String promptUsername() {
        System.out.print("Enter your username: ");
        return sc.nextLine().trim();
    }

    @Override
    public void searchDisabledNotice() {
        System.out.println("[Note] Level-search disabled until after play.]");
    }

    @Override
    public int promptPlayMode() {
        System.out.println("\nHow do you want to play?");
        System.out.println("  1) Play ONE leader");
        System.out.println("  2) Play ALL leaders in sequence");
        System.out.print("Enter choice (1 or 2): ");
        while (true) {
            try {
                int m = Integer.parseInt(sc.nextLine().trim());
                if (m == 1 || m == 2) {
                    return m;
                }
            } catch (Exception ignored) {}
            System.out.print("Invalid. Please enter 1 or 2: ");
        }
    }

    @Override
    public Leader selectLeader(List<Leader> leaders) {
        List<Leader> sorted = leaders.stream()
                .sorted(Comparator.comparing(Leader::getName))
                .collect(Collectors.toList());
        System.out.println("\n=== Select a Leader ===");
        for (int i = 0; i < sorted.size(); i++) {
            System.out.printf("  %d) %s  —  %s%n",
                    i + 1,
                    sorted.get(i).getName(),
                    sorted.get(i).getBackstory()
            );
        }
        System.out.print("Enter your choice (1–" + sorted.size() + "): ");
        while (true) {
            try {
                int c = Integer.parseInt(sc.nextLine().trim()) - 1;
                if (c >= 0 && c < sorted.size()) {
                    Leader sel = sorted.get(c);
                    System.out.println("You chose \"" + sel.getName() + "\"\n");
                    return sel;
                }
            } catch (Exception ignored) {}
            System.out.print("Invalid. Please enter a valid number: ");
        }
    }

    @Override
    public void displayLeaderSequence(String leaderName, int index, int total) {
        System.out.printf("%n=== Leader %d of %d: %s ===%n", index, total, leaderName);
    }

    @Override
    public void displayLevel(Level lvl) {
        System.out.println("\n--- Level " + lvl.getNumber() + " ---");
        System.out.println(lvl.getDescription());
        System.out.println("1) " + lvl.getChoices().get(0).getText());
        System.out.println("2) " + lvl.getChoices().get(1).getText());
        System.out.print("Your choice (1 or 2): ");
    }

    @Override
    public int getPlayerChoice() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void displayTimeoutSkip() {
        System.out.println("[No valid input — skipping level]");
    }

    @Override
    public void displayResult(boolean correct, String summary) {
        System.out.println(correct ? "✔️  Correct!" : "❌  Incorrect");
        System.out.println(summary);
    }

    @Override
    public void showProgress(int score, int total) {
        System.out.printf("Progress: %d/%d%n", score, total);
    }

    @Override
    public void displayEndOfRound(int score, int total, long timeMillis) {
        System.out.println("\n=== Round Complete ===");
        System.out.printf("Score: %d out of %d%n", score, total);
        System.out.printf("Total Time: %.2f seconds%n", timeMillis / 1000.0);
    }

    @Override
    public boolean promptArchiveSearch() {
        System.out.print("Search your archive now? (yes/no): ");
        return sc.nextLine().trim().equalsIgnoreCase("yes");
    }

    @Override
    public int promptPostRoundOption() {
        System.out.println("\nWhat next?");
        System.out.println("  1) Play again");
        System.out.println("  2) Switch user");
        System.out.println("  3) Quit");
        System.out.print("Enter choice (1–3): ");
        while (true) {
            try {
                int c = Integer.parseInt(sc.nextLine().trim());
                if (c >= 1 && c <= 3) {
                    return c;
                }
            } catch (Exception ignored) {}
            System.out.print("Invalid. Please enter 1, 2, or 3: ");
        }
    }

    @Override
    public void displayLeaderboard(List<PlayerRecord> list) {
        System.out.println("\n=== Single–Leader Best Scores ===");
        System.out.printf("%-15s  %-5s  %-6s%n", "Player", "Score", "Time(s)");
        for (PlayerRecord r : list) {
            if (r.getBestSingleScore() > 0) {
                System.out.printf(
                        "%-15s  %-5d  %-6.2f%n",
                        r.getUsername(),
                        r.getBestSingleScore(),
                        r.getBestSingleTimeMillis() / 1000.0
                );
            }
        }

        System.out.println("\n=== Sequential (All Leaders) Best Scores ===");
        System.out.printf("%-15s  %-5s  %-6s%n", "Player", "Score", "Time(s)");
        for (PlayerRecord r : list) {
            if (r.getBestSequentialScore() > 0) {
                System.out.printf(
                        "%-15s  %-5d  %-6.2f%n",
                        r.getUsername(),
                        r.getBestSequentialScore(),
                        r.getBestSequentialTimeMillis() / 1000.0
                );
            }
        }
    }

    @Override
    public void displayGoodbyeMessage() {
        System.out.println("\nThanks for playing!");
    }
}
