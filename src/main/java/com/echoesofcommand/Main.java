package com.echoesofcommand;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui      = new ConsoleUI();
        PlayerManager pm  = new PlayerManager();
        ArchiveManager am = new ArchiveManager();
        JsonLoader loader = new JsonLoader();

        boolean running = true;
        while (running) {
            ui.displayWelcomeMessage();

            // Unique username registration
            PlayerRecord player;
            while (true) {
                String username = ui.promptUsername();
                try {
                    player = pm.registerUser(username);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }

            boolean userActive = true;
            while (userActive) {
                ui.searchDisabledNotice();
                int mode = ui.promptPlayMode();
                List<Leader> allLeaders = loader.loadLeaders("history.json");
                List<Leader> toPlay =
                        (mode == 1)
                                ? List.of(ui.selectLeader(allLeaders))
                                : allLeaders;

                Game game = new Game(toPlay, ui, player, am);
                game.start();

                pm.save();

                if (ui.promptArchiveSearch()) {
                    am.promptSearch();
                }

                int next = ui.promptPostRoundOption();
                switch (next) {
                    case 1 -> {/*replay with same username*/}
                    case 2 -> userActive = false;
                    case 3 -> { userActive = false; running = false; }
                }
            }
            ui.displayLeaderboard(pm.leaderboard());
        }
        ui.displayGoodbyeMessage();
    }
}
