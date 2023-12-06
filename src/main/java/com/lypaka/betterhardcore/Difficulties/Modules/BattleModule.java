package com.lypaka.betterhardcore.Difficulties.Modules;

public class BattleModule {

    private final boolean checkBattles;
    private final String dynamaxPermission;
    private final String megaPermission;

    public BattleModule (boolean checkBattles, String dynamaxPermission, String megaPermission) {

        this.checkBattles = checkBattles;
        this.dynamaxPermission = dynamaxPermission;
        this.megaPermission = megaPermission;

    }

    public boolean doCheckBattles() {

        return this.checkBattles;

    }

    public String getDynamaxPermission() {

        return this.dynamaxPermission;

    }

    public String getMegaPermission() {

        return this.megaPermission;

    }

}
