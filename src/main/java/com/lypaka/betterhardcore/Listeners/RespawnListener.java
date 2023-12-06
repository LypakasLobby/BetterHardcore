package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.pixelmonmod.pixelmon.TickHandler;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RespawnListener {

    public static List<UUID> playersRespawningFromBattleDeath = new ArrayList<>();

    @SubscribeEvent
    public void onRespawn (PlayerEvent.PlayerRespawnEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (!account.getDifficulty().equalsIgnoreCase("none")) {

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            if (difficulty.doesKillPlayerForLosing()) {

                playersRespawningFromBattleDeath.removeIf(u -> {

                    if (u.toString().equalsIgnoreCase(player.getUniqueID().toString())) {

                        PlayerPartyStorage storage = StorageProxy.getParty(player);
                        storage.playerPokedex.wipe();
                        storage.starterPicked = false;
                        TickHandler.registerStarterList(player);
                        return true;

                    }

                    return false;

                });

            }

        }

    }

}
