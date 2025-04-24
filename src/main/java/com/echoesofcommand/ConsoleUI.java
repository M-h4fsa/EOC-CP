package com.echoesofcommand;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements the game UI using the console.
 */
public class ConsoleUI implements GameUI {
    private final Scanner sc = new Scanner(System.in);
    private static final String VALID_USERNAME_REGEX = "[a-zA-Z0-9_]+";

    /**
     * Displays the game welcome message.
     */
    @Override
    public void displayWelcomeMessage() {
        System.out.println("=== Echoes of Command ===");
    }

    /**
     * Prompts for and validates a username.
     * @return A valid username.
     */
    @Override
    public String promptUsername() {
        while (true) {
            System.out.print("Enter your username: ");
            String username = sc.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Error: Username cannot be empty.");
            } else if (!username.matches(VALID_USERNAME_REGEX)) {
                System.out.println("Error: Username can only contain letters, numbers, or underscores.");
            } else {
                return username;
            }
        }
    }

    /**
     * Displays a notice that archive search is disabled.
     */
    @Override
    public void searchDisabledNotice() {
        System.out.println("[Note] Archive-search disabled until after play.");
    }

    /**
     * Prompts the user to choose a play mode.
     * @return 1 for single-leader mode, 2 for sequential mode, 3 for randomized mode, 4 to quit.
     */
    @Override
    public int promptPlayMode() {
        System.out.println("\nHow do you want to play?");
        System.out.println("  1) Play ONE leader");
        System.out.println("  2) Play ALL leaders in sequence");
        System.out.println("  3) Play ALL leaders with randomized levels and choices");
        System.out.println("  4) Quit");
        System.out.print("Enter choice (1, 2, 3, or 4): ");
        while (true) {
            try {
                int mode = Integer.parseInt(sc.nextLine().trim());
                if (mode >= 1 && mode <= 4) {
                    return mode;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.print("Invalid. Please enter 1, 2, 3, or 4: ");
        }
    }

    /**
     * Allows the user to select a leader from a list.
     * @param leaders The list of available leaders.
     * @return The selected Leader object.
     */
    @Override
    public Leader selectLeader(List<Leader> leaders) {
        List<Leader> sorted = leaders.stream()
                .sorted(Comparator.comparing(Leader::getName))
                .toList();
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
                int choice = Integer.parseInt(sc.nextLine().trim()) - 1;
                if (choice >= 0 && choice < sorted.size()) {
                    Leader selected = sorted.get(choice);
                    System.out.println("You chose \"" + selected.getName() + "\"\n");
                    return selected;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.print("Invalid. Please enter a valid number: ");
        }
    }

    /**
     * Displays the current leader in sequential mode.
     * @param leaderName The name of the leader.
     * @param index The current leader index.
     * @param total The total number of leaders.
     */
    @Override
    public void displayLeaderSequence(String leaderName, int index, int total) {
        System.out.printf("%n=== Leader %d of %d: %s ===%n", index, total, leaderName);
    }

    /**
     * Displays a level's details.
     * @param level The level to display.
     */
    @Override
    public void displayLevel(Level level) {
        System.out.println("\n--- Level " + level.getNumber() + " (Leader: " + level.getLeaderName() + ") ---");
        System.out.println(level.getDescription());
        System.out.println("1) " + level.getChoices().get(0).getText());
        System.out.println("2) " + level.getChoices().get(1).getText());
        System.out.print("Your choice (1 or 2): ");
    }

    /**
     * Gets the player's choice for a level.
     * @return 1 or 2 for valid choices, 0 for invalid input.
     */
    @Override
    public int getPlayerChoice() {
        while (true) {
            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                if (choice == 1 || choice == 2) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.print("Invalid. Please enter 1 or 2: ");
        }
    }

    /**
     * Displays a message when a level is skipped due to timeout or invalid input.
     */
    @Override
    public void displayTimeoutSkip() {
        System.out.println("[No valid input — skipping level]");
    }

    /**
     * Displays the result of a player's choice.
     * @param correct True if the choice was correct.
     * @param summary The level's summary text.
     */
    @Override
    public void displayResult(boolean correct, String summary) {
        System.out.println(correct ? "✔️ Correct!" : "❌ Incorrect");
        System.out.println(summary);
    }

    /**
     * Shows the player's current progress.
     * @param score The current score.
     * @param total The total number of levels.
     */
    @Override
    public void showProgress(int score, int total) {
        System.out.printf("Progress: %d/%d%n", score, total);
    }

    /**
     * Displays the end-of-round summary.
     * @param score The final score.
     * @param total The total number of levels.
     * @param timeMillis The time taken in milliseconds.
     */
    @Override
    public void displayEndOfRound(int score, int total, long timeMillis) {
        System.out.println("\n=== Round Complete ===");
        System.out.printf("Score: %d out of %d%n", score, total);
        System.out.printf("Total Time: %.2f seconds%n", timeMillis / 1000.0);
    }

    /**
     * Prompts the user to search the archive.
     * @return True if the user wants to search, false otherwise.
     */
    @Override
    public boolean promptArchiveSearch() {
        System.out.print("Search your archive now? (yes/no): ");
        return sc.nextLine().trim().equalsIgnoreCase("yes");
    }

    /**
     * Prompts for a search keyword.
     * @return The keyword entered by the user.
     */
    @Override
    public String promptSearchKeyword() {
        System.out.print("Enter keyword to search: ");
        return sc.nextLine().trim();
    }

    /**
     * Prompts the user for a post-round option.
     * @return 1 to play again, 2 to switch user, 3 to view stats, 4 to quit.
     */
    @Override
    public int promptPostRoundOption() {
        System.out.println("\nWhat next?");
        System.out.println("  1) Play again");
        System.out.println("  2) Switch user");
        System.out.println("  3) View player statistics");
        System.out.println("  4) Quit");
        System.out.print("Enter choice (1–4): ");
        while (true) {
            try {
                int choice = Integer.parseInt(sc.nextLine().trim());
                if (choice >= 1 && choice <= 4) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.print("Invalid. Please enter 1, 2, 3, or 4: ");
        }
    }

    /**
     * Displays the leaderboard.
     * @param list The list of player records.
     */
    @Override
    public void displayLeaderboard(List<PlayerRecord> list) {
        System.out.println("\n=== Single–Leader Best Scores ===");
        System.out.printf("%-15s  %-5s  %-6s%n", "Player", "Score", "Time(s)");
        for (PlayerRecord record : list) {
            if (record.getBestSingleScore() > 0) {
                System.out.printf(
                        "%-15s  %-5d  %-6.2f%n",
                        record.getUsername(),
                        record.getBestSingleScore(),
                        record.getBestSingleTimeMillis() / 1000.0
                );
            }
        }

        System.out.println("\n=== Sequential (All Leaders) Best Scores ===");
        System.out.printf("%-15s  %-5s  %-6s%n", "Player", "Score", "Time(s)");
        for (PlayerRecord record : list) {
            if (record.getBestSequentialScore() > 0) {
                System.out.printf(
                        "%-15s  %-5d  %-6.2f%n",
                        record.getUsername(),
                        record.getBestSequentialScore(),
                        record.getBestSequentialTimeMillis() / 1000.0
                );
            }
        }
    }

    /**
     * Displays a goodbye message.
     */
    @Override
    public void displayGoodbyeMessage() {
        System.out.println("\nThanks for playing!");
    }

    /**
     * Displays a welcome message for the player, including last login and history option.
     * @param player The player's record.
     */
    public void displayWelcomeForPlayer(PlayerRecord player) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long lastLogin = player.getLastLogin();
        System.out.println();
        if (lastLogin == null) {
            System.out.println("Welcome, " + player.getUsername() + "! You're new to Echoes of Command!");
        } else {
            System.out.println("Welcome back, " + player.getUsername() + "! Last login: " + sdf.format(new Date(lastLogin)));
        }
        System.out.print("View login history? (yes/no): ");
        if (sc.nextLine().trim().equalsIgnoreCase("yes")) {
            displayLoginHistory(player);
        }
    }

    /**
     * Displays the player's login history.
     * @param player The player's record.
     */
    public void displayLoginHistory(PlayerRecord player) {
        System.out.println("\n=== Login History for " + player.getUsername() + " ===");
        List<Long> history = player.getLoginHistory();
        if (history.isEmpty()) {
            System.out.println("No login history available.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < history.size(); i++) {
                System.out.printf("%d) %s%n", i + 1, sdf.format(new Date(history.get(i))));
            }
        }
    }

    /**
     * Displays the player's statistics.
     * @param player The player's record.
     */
    @Override
    public void displayPlayerStats(PlayerRecord player) {
        System.out.println("\n=== Player Statistics for " + player.getUsername() + " ===");
        System.out.printf("Total Levels Played: %d%n", player.getTotalLevelsPlayed());
        System.out.printf("Accuracy: %.2f%%%n", player.getAccuracy());
        System.out.printf("Average Time per Level: %.2f seconds%n", player.getAverageTimePerLevel());
    }
}