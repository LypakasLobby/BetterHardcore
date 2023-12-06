package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.API.PlayerWhiteOutEvent;
import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.Difficulties.Modules.LevelingModule;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.battles.BattleAIMode;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.battles.UseBattleItemEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.Effectiveness;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.items.PokeBallItem;
import com.pixelmonmod.pixelmon.items.medicine.MedicineItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleListeners {

    @SubscribeEvent
    public void onBattleStart (BattleStartedEvent event) throws ObjectMappingException {

        TrainerParticipant tp;
        PlayerParticipant pp;
        BattleController bcb = event.getBattleController();

        if (bcb.participants.get(0) instanceof TrainerParticipant && bcb.participants.get(1) instanceof PlayerParticipant) {

            tp = (TrainerParticipant) bcb.participants.get(0);
            pp = (PlayerParticipant) bcb.participants.get(1);

        } else if (bcb.participants.get(0) instanceof PlayerParticipant && bcb.participants.get(1) instanceof TrainerParticipant) {

            tp = (TrainerParticipant) bcb.participants.get(1);
            pp = (PlayerParticipant) bcb.participants.get(0);

        } else {

            List<PlayerParticipant> players = bcb.getPlayers();
            if (players.size() == 0) return;

            for (PlayerParticipant pps : players) {

                ServerPlayerEntity player = pps.player;
                Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
                if (!account.getDifficulty().equalsIgnoreCase("none")) {

                    Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
                    if (!difficulty.getBattleModule().doCheckBattles()) return;

                    PlayerPartyStorage storage = StorageProxy.getParty(player);
                    int maxLevel = storage.getHighestLevel();
                    LevelingModule levelingModule = difficulty.getLevelingModule();
                    int levelingLevel = account.getLevelingLevel();
                    int maxLevelingLevel = levelingModule.getTierMap().get("Tier-" + levelingLevel);
                    if (maxLevel > maxLevelingLevel) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Battle-Error")), player.getUniqueID());

                    }

                }

            }

            return;

        }

        if (tp != null && pp != null) {

            ServerPlayerEntity player = pp.player;
            Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            tp.setBattleAI(BattleAIMode.valueOf(difficulty.getNPCAI().toUpperCase()).createAI(tp));

        }

    }

    @SubscribeEvent
    public void onTypeEffectiveness (AttackEvent.TypeEffectiveness event) throws ObjectMappingException {

        if (event.user.getPlayerOwner() != null) {

            ServerPlayerEntity player = event.user.getPlayerOwner();
            Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            if (difficulty.areOutgoingAttacksNotVeryEffective()) {

                event.setEffectiveness(Effectiveness.Not);

            }

        }
        if (event.target.getPlayerOwner() != null) {

            ServerPlayerEntity player = event.target.getPlayerOwner();
            Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            if (difficulty.areIncomingAttacksSuperEffective()) {

                event.setEffectiveness(Effectiveness.Super);

            }

        }

    }

    @SubscribeEvent
    public void onSTAB (AttackEvent.Stab event) throws ObjectMappingException {

        if (event.user.getPlayerOwner() != null) {

            ServerPlayerEntity player = event.user.getPlayerOwner();
            Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            if (difficulty.doesDisableSTAB()) {

                event.setStabbing(false);

            }

        }

    }

    @SubscribeEvent
    public void onCrit (AttackEvent.CriticalHit event) throws ObjectMappingException {

        if (event.target.getPlayerOwner() != null) {

            ServerPlayerEntity player = event.target.getPlayerOwner();
            Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            double critChance = difficulty.getCriticalHitChance();
            if (RandomHelper.getRandomChance(critChance)) {

                event.setCrit(true);

            }

        }

    }

    @SubscribeEvent
    public void onBattleItemUse (UseBattleItemEvent event) throws ObjectMappingException {

        if (event.getParticipant() instanceof PlayerParticipant) {

            PlayerParticipant pp = (PlayerParticipant) event.getParticipant();
            ServerPlayerEntity player = pp.player;
            Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            ItemStack usedItem = event.getStack();
            if (usedItem.getItem() instanceof MedicineItem) {

                if (!difficulty.doesAllowHealingInBattles()) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                } else {

                    String id = usedItem.getItem().getRegistryName().toString();
                    if (id.contains("revive")) {

                        if (!difficulty.doesAllowRevives()) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                        }

                    }

                }

            } else if (usedItem.getItem() instanceof PokeBallItem) {

                if (!difficulty.doesAllowPokeBallsInBattle()) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());
                    return;

                }
                String actualBall = CaptureListeners.getActualPokeBallNameBecausePixelmonChangedThisForLiterallyNoReasonLOL(usedItem);
                if (actualBall.equalsIgnoreCase("pixelmon:master_ball") || actualBall.equalsIgnoreCase("pixelmon:park_ball")) {

                    if (!difficulty.doesAllowMasterParkBalls()) {

                        event.setCanceled(true);
                        player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onBattleEnd (BattleEndEvent event) throws ObjectMappingException {

        BattleController bcb = event.getBattleController();
        PlayerParticipant pp1 = null;
        PlayerParticipant pp2 = null;

        if (bcb.participants.get(0) instanceof PlayerParticipant) {

            pp1 = (PlayerParticipant) bcb.participants.get(0);

        }
        if (bcb.participants.get(1) instanceof PlayerParticipant) {

            pp2 = (PlayerParticipant) bcb.participants.get(1);

        }

        if (pp1 != null) {

            ServerPlayerEntity player1 = pp1.player;
            Account account = AccountHandler.getPlayerAccount(player1.getUniqueID());
            if (!account.getDifficulty().equalsIgnoreCase("none")) {

                Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
                if (difficulty.isNuzlockeMode()) {

                    PlayerPartyStorage party1 = StorageProxy.getParty(player1);
                    int partySize = 0;
                    int deadPokemonCount = 0;
                    Map<Integer, Pokemon> deadPokemon = new HashMap<>();
                    for (int i = 0; i < 6; i++) {

                        Pokemon pokemon = party1.get(i);
                        if (pokemon != null) {

                            partySize++;
                            if (pokemon.isFainted()) {

                                deadPokemonCount++;
                                deadPokemon.put(i, pokemon);

                            }

                        }

                    }

                    for (Map.Entry<Integer, Pokemon> entry : deadPokemon.entrySet()) {

                        party1.set(entry.getKey(), null);

                    }

                    if (partySize == deadPokemonCount) {

                        AccountHandler.whiteOut(player1, "reason 1");

                        PlayerWhiteOutEvent whiteOutEvent = new PlayerWhiteOutEvent(player1);
                        MinecraftForge.EVENT_BUS.post(whiteOutEvent);

                    }

                }

            }

        }
        if (pp2 != null) {

            ServerPlayerEntity player2 = pp2.player;
            Account account = AccountHandler.getPlayerAccount(player2.getUniqueID());
            if (!account.getDifficulty().equalsIgnoreCase("none")) {

                Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
                if (difficulty.isNuzlockeMode()) {

                    PlayerPartyStorage party2 = StorageProxy.getParty(player2);
                    int partySize = 0;
                    int deadPokemonCount = 0;
                    Map<Integer, Pokemon> deadPokemon = new HashMap<>();
                    for (int i = 0; i < 6; i++) {

                        Pokemon pokemon = party2.get(i);
                        if (pokemon != null) {

                            partySize++;
                            if (pokemon.isFainted()) {

                                deadPokemonCount++;
                                deadPokemon.put(i, pokemon);

                            }

                        }

                    }

                    for (Map.Entry<Integer, Pokemon> entry : deadPokemon.entrySet()) {

                        party2.set(entry.getKey(), null);

                    }

                    if (partySize == deadPokemonCount) {

                        AccountHandler.whiteOut(player2, "reason 2");

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onLoseToTrainer (LostToTrainerEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = event.player;
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (!account.getDifficulty().equalsIgnoreCase("none")) {

            AccountHandler.whiteOut(player, "reason 3");

        }

    }

    @SubscribeEvent
    public void onLoseToPokemon (LostToWildPixelmonEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = event.player;
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (!account.getDifficulty().equalsIgnoreCase("none")) {

            AccountHandler.whiteOut(player, "reason 4");

        }

    }

}
