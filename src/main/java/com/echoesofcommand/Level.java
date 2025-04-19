package com.echoesofcommand;

import java.util.List;

public class Level {
    private int number;
    private String description;
    private List<Choice> choices;
    private String summary;

    public Level(int number, String description, List<Choice> choices, String summary) {
        this.number = number;
        this.description = description;
        this.choices = choices;
        this.summary = summary;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public String getSummary() {
        return summary;
    }
}