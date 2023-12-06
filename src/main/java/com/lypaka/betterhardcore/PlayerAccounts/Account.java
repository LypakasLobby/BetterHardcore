package com.lypaka.betterhardcore.PlayerAccounts;

import com.lypaka.betterhardcore.BetterHardcore;

import java.util.ArrayList;
import java.util.UUID;

public class Account {

    private final UUID uuid;
    private int catchingLevel;
    private boolean counterDisplayed;
    private int daysSurvived;
    private String difficulty;
    private int levelingLevel;
    private int secondsSurvived;
    private ArrayList<String> caughtSpecies;
    private int whiteouts;

    public Account (UUID uuid, int catchingLevel, boolean counterDisplayed, int daysSurvived, String difficulty, int levelingLevel, int secondsSurvived, ArrayList<String> caughtSpecies, int whiteouts) {

        this.uuid = uuid;
        this.catchingLevel = catchingLevel;
        this.counterDisplayed = counterDisplayed;
        this.daysSurvived = daysSurvived;
        this.difficulty = difficulty;
        this.levelingLevel = levelingLevel;
        this.secondsSurvived = secondsSurvived;
        this.caughtSpecies = caughtSpecies;
        this.whiteouts = whiteouts;

    }

    public void create() {

        AccountHandler.accountMap.put(this.uuid, this);

    }

    public void saveWholeAccount() {

        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Catching-Level").setValue(this.catchingLevel);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Counter-Displayed").setValue(this.counterDisplayed);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Days-Survived").setValue(this.daysSurvived);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Difficulty").setValue(this.difficulty);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Leveling-Level").setValue(this.levelingLevel);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Seconds-Survived").setValue(this.secondsSurvived);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Used-Species").setValue(this.caughtSpecies);
        BetterHardcore.playerConfigManager.getPlayerConfigNode(this.uuid, "Whiteouts").setValue(this.whiteouts);
        BetterHardcore.playerConfigManager.savePlayer(this.uuid);

    }

    public int getCatchingLevel() {

        return this.catchingLevel;

    }

    public void setCatchingLevel (int level) {

        this.catchingLevel = level;

    }

    public boolean isCounterDisplayed() {

        return this.counterDisplayed;

    }

    public void setCounterDisplayed (boolean displayed) {

        this.counterDisplayed = displayed;

    }

    public int getDaysSurvived() {

        return this.daysSurvived;

    }

    public void setDaysSurvived (int days) {

        this.daysSurvived = days;

    }

    public String getDifficulty() {

        return this.difficulty;

    }

    public void setDifficulty (String diff) {

        this.difficulty = diff;

    }

    public int getLevelingLevel() {

        return this.levelingLevel;

    }

    public void setLevelingLevel (int level) {

        this.levelingLevel = level;

    }

    public int getSecondsSurvived() {

        return this.secondsSurvived;

    }

    public void setSecondsSurvived (int seconds) {

        this.secondsSurvived = seconds;

    }

    public ArrayList<String> getCaughtSpecies() {

        return this.caughtSpecies;

    }

    public void setCaughtSpecies (ArrayList<String> species) {

        this.caughtSpecies = species;

    }

    public int getWhiteouts() {

        return this.whiteouts;

    }

    public void setWhiteouts (int whiteouts) {

        this.whiteouts = whiteouts;

    }

}
