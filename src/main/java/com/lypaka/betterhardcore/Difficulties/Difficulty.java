package com.lypaka.betterhardcore.Difficulties;

import com.lypaka.betterhardcore.Difficulties.Modules.BattleModule;
import com.lypaka.betterhardcore.Difficulties.Modules.CatchingModule;
import com.lypaka.betterhardcore.Difficulties.Modules.LevelingModule;

public class Difficulty {

    private final String name;
    private final boolean allowBreeding;
    private final boolean allowHealingInBattles;
    private final boolean allowHealingOutsideOfBattles;
    private final boolean allowHealers;
    private final boolean allowHeldItems;
    private final boolean allowPokeBallsInBattle;
    private final boolean allowMasterParkBalls;
    private final boolean allowRareCandies;
    private final boolean allowRevives;
    private final boolean allowShopkeepers;
    private final boolean allowTera;
    private final boolean allowTeraBattleMods;
    private final boolean allowTMsTRs;
    private final boolean allowTrading;
    private final boolean allowXPCandies;
    private final int absoluteMinCatchRate;
    private final String catchRateEquation;
    private final boolean disableSTAB;
    private final double firstStageModifier;
    private final double middleStageModifier;
    private final double finalStageModifier;
    private final double singleStageModifier;
    private final boolean clearInventoryOnDeath;
    private final int costToHeal;
    private final double criticalHitChance;
    private final double expReducerModifier;
    private final boolean incomingAttacksSuperEffective;
    private final boolean killPlayerForLosing;
    private final int maxIVForWildPokemon;
    private final double moneyLost;
    private final String npcAI;
    private final boolean nuzlockeMode;
    private final boolean outgoingAttacksNotVeryEffective;
    private final boolean shiniesIgnoreSpeciesClause;
    private final boolean speciesClause;

    private final BattleModule battleModule;
    private final CatchingModule catchModule;
    private final LevelingModule levelingModule;

    public Difficulty (String name, boolean allowBreeding, boolean allowHealingInBattles, boolean allowHealingOutsideOfBattles,
                       boolean allowHealers, boolean allowHeldItems, boolean allowPokeBallsInBattle, boolean allowMasterParkBalls,
                       boolean allowRareCandies, boolean allowRevives, boolean allowShopkeepers, boolean allowTera, boolean allowTeraBattleMods,
                       boolean allowTMsTRs, boolean allowTrading, boolean allowXPCandies, int absoluteMinCatchRate, String catchRateEquation,
                       boolean disableSTAB, double firstStageModifier, double middleStageModifier, double finalStageModifier,
                       double singleStageModifier, boolean clearInventoryOnDeath, int costToHeal, double criticalHitChance,
                       double expReducerModifier, boolean incomingAttacksSuperEffective, boolean killPlayerForLosing, int maxIVForWildPokemon,
                       double moneyLost, String npcAI, boolean nuzlockeMode, boolean outgoingAttacksNotVeryEffective,
                       boolean shiniesIgnoreSpeciesClause, boolean speciesClause, BattleModule battleModule, CatchingModule catchModule,
                       LevelingModule levelingModule) {

        this.name = name;
        this.allowBreeding = allowBreeding;
        this.allowHealingInBattles = allowHealingInBattles;
        this.allowHealingOutsideOfBattles = allowHealingOutsideOfBattles;
        this.allowHealers = allowHealers;
        this.allowHeldItems = allowHeldItems;
        this.allowPokeBallsInBattle = allowPokeBallsInBattle;
        this.allowMasterParkBalls = allowMasterParkBalls;
        this.allowShopkeepers = allowShopkeepers;
        this.allowRareCandies = allowRareCandies;
        this.allowRevives = allowRevives;
        this.allowTera = allowTera;
        this.allowTeraBattleMods = allowTeraBattleMods;
        this.allowTMsTRs = allowTMsTRs;
        this.allowTrading = allowTrading;
        this.allowXPCandies = allowXPCandies;
        this.absoluteMinCatchRate = absoluteMinCatchRate;
        this.catchRateEquation = catchRateEquation;
        this.disableSTAB = disableSTAB;
        this.firstStageModifier = firstStageModifier;
        this.middleStageModifier = middleStageModifier;
        this.finalStageModifier = finalStageModifier;
        this.singleStageModifier = singleStageModifier;
        this.clearInventoryOnDeath = clearInventoryOnDeath;
        this.costToHeal = costToHeal;
        this.criticalHitChance = criticalHitChance;
        this.expReducerModifier = expReducerModifier;
        this.incomingAttacksSuperEffective = incomingAttacksSuperEffective;
        this.killPlayerForLosing = killPlayerForLosing;
        this.maxIVForWildPokemon = maxIVForWildPokemon;
        this.moneyLost = moneyLost;
        this.npcAI = npcAI;
        this.nuzlockeMode = nuzlockeMode;
        this.outgoingAttacksNotVeryEffective = outgoingAttacksNotVeryEffective;
        this.shiniesIgnoreSpeciesClause = shiniesIgnoreSpeciesClause;
        this.speciesClause = speciesClause;
        this.battleModule = battleModule;
        this.catchModule = catchModule;
        this.levelingModule = levelingModule;

    }

    public void create() {

        DifficultyHandler.difficultyMap.put(this.name, this);

    }

    public String getName() {

        return this.name;

    }

    public boolean doesAllowBreeding() {

        return this.allowBreeding;

    }

    public boolean doesAllowHealingInBattles() {

        return this.allowHealingInBattles;

    }

    public boolean doesAllowHealingOutsideOfBattles() {

        return this.allowHealingOutsideOfBattles;

    }

    public boolean doesAllowHealers() {

        return this.allowHealers;

    }

    public boolean doesAllowHeldItems() {

        return this.allowHeldItems;

    }

    public boolean doesAllowPokeBallsInBattle() {

        return this.allowPokeBallsInBattle;

    }

    public boolean doesAllowMasterParkBalls() {

        return this.allowMasterParkBalls;

    }

    public boolean doesAllowShopkeepers() {

        return this.allowShopkeepers;

    }

    public boolean doesAllowRareCandies() {

        return this.allowRareCandies;

    }

    public boolean doesAllowRevives() {

        return this.allowRevives;

    }

    public boolean doesAllowTera() {

        return this.allowTera;

    }

    public boolean doesAllowTeraBattleMods() {

        return this.allowTeraBattleMods;

    }

    public boolean doesAllowTMsTRs() {

        return this.allowTMsTRs;

    }

    public boolean doesAllowTrading() {

        return this.allowTrading;

    }

    public boolean doesAllowXPCandies() {

        return this.allowXPCandies;

    }

    public int getAbsoluteMinCatchRate() {

        return this.absoluteMinCatchRate;

    }

    public String getCatchRateEquation() {

        return this.catchRateEquation;

    }

    public double getFirstStageModifier() {

        return this.firstStageModifier;

    }

    public double getMiddleStageModifier() {

        return this.middleStageModifier;

    }

    public double getFinalStageModifier() {

        return this.finalStageModifier;

    }

    public double getSingleStageModifier() {

        return this.singleStageModifier;

    }

    public boolean doesClearInventory() {

        return this.clearInventoryOnDeath;

    }

    public int getCostToHeal() {

        return this.costToHeal;

    }

    public double getCriticalHitChance() {

        return this.criticalHitChance;

    }

    public boolean doesDisableSTAB() {

        return this.disableSTAB;

    }

    public double getExpReducerModifier() {

        return this.expReducerModifier;

    }

    public boolean areIncomingAttacksSuperEffective() {

        return this.incomingAttacksSuperEffective;

    }

    public boolean doesKillPlayerForLosing() {

        return this.killPlayerForLosing;

    }

    public int getMaxIVForWildPokemon() {

        return this.maxIVForWildPokemon;

    }

    public double getMoneyLost() {

        return this.moneyLost;

    }

    public String getNPCAI() {

        return this.npcAI;

    }

    public boolean isNuzlockeMode() {

        return this.nuzlockeMode;

    }

    public boolean areOutgoingAttacksNotVeryEffective() {

        return this.outgoingAttacksNotVeryEffective;

    }

    public boolean doShiniesIgnoreSpeciesClause() {

        return this.shiniesIgnoreSpeciesClause;

    }

    public boolean enforcesSpeciesClause() {

        return this.speciesClause;

    }

    public BattleModule getBattleModule() {

        return this.battleModule;

    }

    public CatchingModule getCatchModule() {

        return this.catchModule;

    }

    public LevelingModule getLevelingModule() {

        return this.levelingModule;

    }

}
