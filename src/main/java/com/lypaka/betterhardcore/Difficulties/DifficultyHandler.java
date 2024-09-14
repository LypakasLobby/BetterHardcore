package com.lypaka.betterhardcore.Difficulties;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterhardcore.BetterHardcore;
import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Modules.BattleModule;
import com.lypaka.betterhardcore.Difficulties.Modules.CatchingModule;
import com.lypaka.betterhardcore.Difficulties.Modules.LevelingModule;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DifficultyHandler {

    public static Map<String, Difficulty> difficultyMap = new HashMap<>();

    public static void registerDifficulties() throws IOException, ObjectMappingException {

        BetterHardcore.difficultyConfigManager = new HashMap<>();
        String[] files = new String[]{"settings.conf", "battling.conf", "catching.conf", "leveling.conf"};
        for (String s : ConfigGetters.difficulties) {

            Path dir = ConfigUtils.checkDir(Paths.get("./config/betterhardcore/difficulties/" + s));
            BasicConfigManager bcm = new BasicConfigManager(files, dir, BetterHardcore.class, BetterHardcore.MOD_NAME, BetterHardcore.MOD_ID, BetterHardcore.logger);
            bcm.init();

            boolean allowBreeding = bcm.getConfigNode(0, "Allow-Breeding").getBoolean();
            boolean allowHealingInBattle = bcm.getConfigNode(0, "Allow-Healing-In-Battle").getBoolean();
            boolean allowHealingOutsideOfBattle = bcm.getConfigNode(0, "Allow-Healing-Outside-Of-Battle").getBoolean();
            boolean allowHealers = bcm.getConfigNode(0, "Allow-Healers").getBoolean();
            boolean allowHeldItems = bcm.getConfigNode(0, "Allow-Held-Items").getBoolean();
            boolean allowPokeBallsInBattle = bcm.getConfigNode(0, "Allow-Poke-Balls").getBoolean();
            boolean allowMasterParkBalls = bcm.getConfigNode(0, "Allow-Master-Park-Balls").getBoolean();
            boolean allowRareCandies = bcm.getConfigNode(0, "Allow-Rare-Candies").getBoolean();
            boolean allowRevives = bcm.getConfigNode(0, "Allow-Revives").getBoolean();
            boolean allowShopkeepers = bcm.getConfigNode(0, "Allow-Shopkeepers").getBoolean();
            boolean allowTera = bcm.getConfigNode(0, "Allow-Tera").getBoolean();
            boolean allowTeraBattleMods = bcm.getConfigNode(0 , "Allow-Tera-Battle-Modifiers").getBoolean();
            boolean allowTMsTRs = bcm.getConfigNode(0, "Allow-TMs-TRs").getBoolean();
            boolean allowTrading = bcm.getConfigNode(0, "Allow-Trading").getBoolean();
            boolean allowXPCandies = bcm.getConfigNode(0, "Allow-XP-Candies").getBoolean();
            int absoluteMinCatchRate = bcm.getConfigNode(0, "Catch-Rate-Modifiers", "Absolute-Min-Value").getInt();
            String catchRateEquation = bcm.getConfigNode(0, "Catch-Rate-Modifiers", "Equation").getString();
            boolean disableSTAB = bcm.getConfigNode(0, "Disable-STAB").getBoolean();
            double firstStageModifier = bcm.getConfigNode(0, "Catch-Rate-Modifiers", "Evolution-Stage", "First").getDouble();
            double middleStageModifier = bcm.getConfigNode(0, "Catch-Rate-Modifiers", "Evolution-Stage", "Middle").getDouble();
            double finalStageModifier = bcm.getConfigNode(0, "Catch-Rate-Modifiers", "Evolution-Stage", "Final").getDouble();
            double singleStageModifier = bcm.getConfigNode(0, "Catch-Rate-Modifiers", "Evolution-Stage", "Single").getDouble();
            boolean clearInventoryOnDeath = bcm.getConfigNode(0, "Clear-Inventory-On-Death").getBoolean();
            int costToHeal = bcm.getConfigNode(0, "Cost-To-Heal").getInt();
            double criticalHitChance = bcm.getConfigNode(0, "Critical-Hit-Chance").getDouble();
            double expReducerModifier = bcm.getConfigNode(0, "EXP-Reducer-Modifier").getDouble();
            boolean incomingAttacksSuperEffective = bcm.getConfigNode(0, "Incoming-Attacks-Super-Effective").getBoolean();
            boolean killPlayerForLosing = bcm.getConfigNode(0, "Kill-Players-For-Losing").getBoolean();
            int maxIVForWildPokemon = bcm.getConfigNode(0, "Max-IV-For-Wild-Pokemon").getInt();
            double moneyLost = bcm.getConfigNode(0, "Money-Lost").getDouble();
            String npcAI = bcm.getConfigNode(0, "NPC-AI").getString();
            boolean nuzlockeMode = bcm.getConfigNode(0, "Nuzlocke-Mode").getBoolean();
            boolean outgoingAttacksNotVeryEffective = bcm.getConfigNode(0, "Outgoing-Attacks-Not-Very-Effective").getBoolean();
            boolean shiniesIgnoreSpeciesClause = bcm.getConfigNode(0, "Shinies-Ignore-Species-Clause").getBoolean();
            boolean speciesClause = bcm.getConfigNode(0, "Species-Clause").getBoolean();

            boolean checkBattles = bcm.getConfigNode(1, "Check-Battles").getBoolean();
            String dynamaxPermission = bcm.getConfigNode(1, "Permissions", "Dynamax-Permission").getString();
            String megaPermission = bcm.getConfigNode(1, "Permissions", "Mega-Permission").getString();
            BattleModule battleModule = new BattleModule(checkBattles, dynamaxPermission, megaPermission);

            boolean enableCatching = bcm.getConfigNode(2, "Enabled").getBoolean();
            String finalStagePermission = bcm.getConfigNode(2, "Settings", "Evolution-Stage", "Final").getString();
            String firstStagePermission = bcm.getConfigNode(2, "Settings", "Evolution-Stage", "First").getString();
            String middleStagePermission = bcm.getConfigNode(2, "Settings", "Evolution-Stage", "Middle").getString();
            String singleStagePermission = bcm.getConfigNode(2, "Settings", "Evolution-Stage", "Single").getString();
            String legendaryPermission = bcm.getConfigNode(2, "Settings", "Legendary-Permission").getString();
            String shinyPermission = bcm.getConfigNode(2, "Settings", "Shiny-Permission").getString();
            Map<String, Integer> tierMap = bcm.getConfigNode(2, "Tiers").getValue(new TypeToken<Map<String, Integer>>() {});
            CatchingModule catchingModule = new CatchingModule(enableCatching, finalStagePermission, firstStagePermission, middleStagePermission, singleStagePermission, legendaryPermission, shinyPermission, tierMap);

            boolean enableLeveling = bcm.getConfigNode(3, "Enabled").getBoolean();
            boolean obedient = true;
            if (bcm.getConfigNode(3, "Obedience").isVirtual()) {

                bcm.getConfigNode(3, "Obedience").setValue(true);
                bcm.getConfigNode(3, "Obedience").setComment("If the player somehow tries to use a Pokemon that is on a higher level than their currently allowed max level, this set to true means the Pokemon will have a chance to be disobedient in battle");
                bcm.save();

            } else {

                obedient = bcm.getConfigNode(3, "Obedience").getBoolean();

            }
            Map<String, Integer> levelTierMap = bcm.getConfigNode(3, "Tiers").getValue(new TypeToken<Map<String, Integer>>() {});
            LevelingModule levelingModule = new LevelingModule(enableLeveling, obedient, levelTierMap);

            Difficulty difficulty = new Difficulty(s, allowBreeding, allowHealingInBattle, allowHealingOutsideOfBattle, allowHealers, allowHeldItems,
                    allowPokeBallsInBattle, allowMasterParkBalls, allowRareCandies, allowRevives, allowShopkeepers, allowTera, allowTeraBattleMods,
                    allowTMsTRs, allowTrading, allowXPCandies, absoluteMinCatchRate, catchRateEquation, disableSTAB, firstStageModifier,
                    middleStageModifier, finalStageModifier, singleStageModifier, clearInventoryOnDeath, costToHeal, criticalHitChance,
                    expReducerModifier, incomingAttacksSuperEffective, killPlayerForLosing, maxIVForWildPokemon, moneyLost, npcAI, nuzlockeMode,
                    outgoingAttacksNotVeryEffective, shiniesIgnoreSpeciesClause, speciesClause, battleModule, catchingModule, levelingModule);
            difficulty.create();
            BetterHardcore.logger.info("Successfully loaded difficulty: " + s);

        }

    }

    public static void assignDifficulty (Account account, Difficulty difficulty) {

        account.setDifficulty(difficulty.getName());
        account.setCatchingLevel(1);
        account.setLevelingLevel(1);
        account.setSecondsSurvived(0);
        account.setDaysSurvived(1);
        account.setCaughtSpecies(new ArrayList<>());
        account.saveWholeAccount();

    }

    public static Difficulty getFromName (String name) {

        Difficulty diff = null;
        for (Map.Entry<String, Difficulty> entry : difficultyMap.entrySet()) {

            if (entry.getKey().equalsIgnoreCase(name)) {

                diff = entry.getValue();
                break;

            }

        }

        return diff;

    }

}
