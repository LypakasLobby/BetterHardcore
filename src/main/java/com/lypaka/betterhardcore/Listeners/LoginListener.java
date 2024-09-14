package com.lypaka.betterhardcore.Listeners;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterhardcore.API.DifficultyAssignedEvent;
import com.lypaka.betterhardcore.BetterHardcore;
import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.UUID;

public class LoginListener {

    @SubscribeEvent
    public void onLogin (PlayerEvent.PlayerLoggedInEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID uuid = player.getUniqueID();
        BetterHardcore.playerConfigManager.loadPlayer(uuid);
        int catchingLevel = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Catching-Level").getInt();
        boolean counterDisplayed = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Counter-Displayed").getBoolean();
        int daysSurvived = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Days-Survived").getInt();
        String difficulty = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Difficulty").getString();
        int levelingLevel = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Leveling-Level").getInt();
        int secondsSurvived = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Seconds-Survived").getInt();
        ArrayList<String> caughtSpecies = new ArrayList<>(BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Used-Species").getList(TypeToken.of(String.class)));
        int whiteouts = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Whiteouts").getInt();
        Account account = new Account(uuid, catchingLevel, counterDisplayed, daysSurvived, difficulty, levelingLevel, secondsSurvived, caughtSpecies, whiteouts);
        account.create();

        if (!ConfigGetters.forceDifficulty.equalsIgnoreCase("none")) {

            if (account.getDifficulty().equalsIgnoreCase("none")) {

                Difficulty diff = DifficultyHandler.getFromName(ConfigGetters.forceDifficulty);
                if (diff != null) {

                    DifficultyAssignedEvent assignedEvent = new DifficultyAssignedEvent(player, diff);
                    MinecraftForge.EVENT_BUS.post(assignedEvent);
                    if (!assignedEvent.isCanceled()) {

                        DifficultyHandler.assignDifficulty(account, assignedEvent.getDifficulty());
                        player.sendMessage(FancyText.getFormattedText("&eAssigned you the " + diff.getName() + " Difficulty!"), uuid);

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onLogout (PlayerEvent.PlayerLoggedOutEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player);
        account.saveWholeAccount();
        AccountHandler.accountMap.entrySet().removeIf(e -> e.getKey().toString().equalsIgnoreCase(player.getUniqueID().toString()));

    }

}
