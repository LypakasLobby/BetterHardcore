package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.pokemon.item.pokeball.PokeBallRegistry;
import com.pixelmonmod.pixelmon.api.storage.NbtKeys;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;

public class CaptureListeners {

    @SubscribeEvent
    public void onStartCapture (CaptureEvent.StartCapture event) throws ObjectMappingException {

        ServerPlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        PixelmonEntity pokemon = event.getPokemon();
        ItemStack ball = event.getPokeBall().getBallType().getBallItem();
        ball.setCount(1);
        String evoStage = getEvoStage(pokemon);

        if (difficulty.getCatchModule().isEnabled()) {

            if (pokemon.getPokemon().isShiny()) {

                if (!difficulty.getCatchModule().getShinyPermission().equalsIgnoreCase("")) {

                    if (!PermissionHandler.hasPermission(player, difficulty.getCatchModule().getShinyPermission())) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Missing-Permission")), player.getUniqueID());
                        player.addItemStackToInventory(ball);
                        return;

                    }

                }

            }

            if (pokemon.getPokemon().isLegendary() || pokemon.getPokemon().isMythical() || pokemon.getPokemon().isUltraBeast()) {

                if (!difficulty.getCatchModule().getLegendaryPermission().equals("")) {

                    if (!PermissionHandler.hasPermission(player, difficulty.getCatchModule().getLegendaryPermission())) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Missing-Permission")), player.getUniqueID());
                        player.addItemStackToInventory(ball);
                        return;

                    }

                }

            }

            String perm;
            switch (evoStage) {

                case "Final":
                    perm = difficulty.getCatchModule().getFinalStagePermission();
                    break;

                case "First":
                    perm = difficulty.getCatchModule().getFirstStagePermission();
                    break;

                case "Middle":
                    perm = difficulty.getCatchModule().getMiddleStagePermission();
                    break;

                default:
                    perm = difficulty.getCatchModule().getSingleStagePermission();
                    break;

            }

            if (!perm.equals("")) {

                if (!PermissionHandler.hasPermission(player, perm)) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Missing-Permission")), player.getUniqueID());
                    player.addItemStackToInventory(ball);
                    return;

                }

            }

            int tierLevel = account.getCatchingLevel();
            int maxLevel = difficulty.getCatchModule().getTierMap().get("Tier-" + tierLevel);
            if (pokemon.getLvl().getPokemonLevel() > maxLevel) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Catching-Tier-Error")), player.getUniqueID());
                player.addItemStackToInventory(ball);
                return;

            }

        }

        if (difficulty.enforcesSpeciesClause()) {

            List<String> caughtSpecies = account.getCaughtSpecies();
            if (caughtSpecies.contains(pokemon.getSpecies().getName())) {

                if (!pokemon.getPokemon().isShiny()) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Species-Clause")), player.getUniqueID());
                    player.addItemStackToInventory(ball);

                } else {

                    if (!difficulty.doShiniesIgnoreSpeciesClause()) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Species-Clause")), player.getUniqueID());
                        player.addItemStackToInventory(ball);

                    }

                }

            }

        }

        if (!difficulty.doesAllowMasterParkBalls()) {

            String actualBall = getActualPokeBallNameBecausePixelmonChangedThisForLiterallyNoReasonLOL(ball);
            if (actualBall.equalsIgnoreCase("pixelmon:master_ball") || actualBall.equalsIgnoreCase("pixelmon:park_ball")) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());
                player.addItemStackToInventory(ball);
                return;

            }

        }

        int originalCatchRate = event.getCaptureValues().getCatchRate();
        double evoStageMod = getEvoStageMod(difficulty, evoStage);
        int level = pokemon.getPokemon().getPokemonLevel();
        String equation = difficulty.getCatchRateEquation();
        Expression exp = new ExpressionBuilder(equation
                .replace("%originalCatchRate%", String.valueOf(originalCatchRate))
                .replace("%stageModifier%", String.valueOf(evoStageMod))
                .replace("%pokemonLevel%", String.valueOf(level))
        ).build();
        int modifiedCatchRate = (int) exp.evaluate();
        event.getCaptureValues().setCatchRate(Math.max(difficulty.getAbsoluteMinCatchRate(), modifiedCatchRate));

    }

    @SubscribeEvent
    public void onSuccessfulCapture (CaptureEvent.SuccessfulCapture event) throws ObjectMappingException {

        ServerPlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        PixelmonEntity pokemon = event.getPokemon();
        int maxIV = difficulty.getMaxIVForWildPokemon();
        int[] newIVs = new int[]{RandomHelper.getRandomNumberBetween(1, maxIV),
                RandomHelper.getRandomNumberBetween(1, maxIV),
                RandomHelper.getRandomNumberBetween(1, maxIV),
                RandomHelper.getRandomNumberBetween(1, maxIV),
                RandomHelper.getRandomNumberBetween(1, maxIV),
                RandomHelper.getRandomNumberBetween(1, maxIV)};

        pokemon.getPokemon().getIVs().fillFromArray(newIVs);
        pokemon.updateStats();

    }

    // public because its used in the BattleListeners class also
    public static String getActualPokeBallNameBecausePixelmonChangedThisForLiterallyNoReasonLOL (ItemStack item) {

        String ball = "poke_ball";
        if (item.hasTag()) {

            ball = PokeBallRegistry.getPokeBall(item.getTag().getString(NbtKeys.POKE_BALL_ID)).getValue().get().getName();

        }

        return "pixelmon:" + ball.replace(" ", "_").toLowerCase();

    }

    private static double getEvoStageMod (Difficulty difficulty, String stage) {

        switch (stage.toLowerCase()) {

            case "single":
                return difficulty.getSingleStageModifier();

            case "first":
                return difficulty.getFirstStageModifier();

            case "middle":
                return difficulty.getMiddleStageModifier();

            default:
                return difficulty.getFinalStageModifier();

        }

    }

    private static String getEvoStage (PixelmonEntity pokemon) {

        // Pokemon has no pre-evolutions and can evolve, Pokemon is baby-stage
        if (pokemon.getForm().getPreEvolutions().size() == 0 && pokemon.getForm().getEvolutions().size() != 0) {

            return "First";

        }

        // Pokemon has pre-evolutions and can evolve, Pokemon is middle-stage
        if (pokemon.getForm().getPreEvolutions().size() != 0 && pokemon.getForm().getEvolutions().size() != 0) {

            return "Middle";

        }

        // Pokemon has pre-evolutions and can not evolve, Pokemon is final-stage
        if (pokemon.getForm().getPreEvolutions().size() != 0 && pokemon.getForm().getEvolutions().size() == 0) {

            return "Final";

        }

        // Pokemon has no pre-evolutions and can not evolve, Pokemon is single-stage
        if (pokemon.getForm().getPreEvolutions().size() == 0 && pokemon.getForm().getEvolutions().size() == 0) {

            return "Single";

        }

        return "None";

    }

}
