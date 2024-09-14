package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.catalystterapokemon.API.Battles.TeraAttackEffectivenessEvent;
import com.lypaka.catalystterapokemon.API.Battles.TeraAttackSTABEvent;
import com.lypaka.catalystterapokemon.API.TerastallizeEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class TeraListeners {

    @SubscribeEvent
    public void onTeraStab (TeraAttackSTABEvent event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        if (event.getUserOwner() instanceof PlayerParticipant) {

            ServerPlayerEntity player = ((PlayerParticipant) event.getUserOwner()).player;
            if (player != null) {

                Account account = AccountHandler.getPlayerAccount(player);
                if (account.getDifficulty().equalsIgnoreCase("none")) return;

                Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
                if (!difficulty.doesAllowTeraBattleMods()) event.setCanceled(true);

            }

        }

    }

    @SubscribeEvent
    public void onTeraTypeEffectiveness (TeraAttackEffectivenessEvent event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        if (event.getUserOwner() instanceof PlayerParticipant) {

            ServerPlayerEntity player = ((PlayerParticipant) event.getUserOwner()).player;
            if (player != null) {

                Account account = AccountHandler.getPlayerAccount(player);
                if (account.getDifficulty().equalsIgnoreCase("none")) return;

                Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
                if (!difficulty.doesAllowTeraBattleMods()) event.setCanceled(true);

            }

        }

    }

    @SubscribeEvent
    public void onTera (TerastallizeEvent event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        ServerPlayerEntity player = event.getPlayer();
        if (player != null) {

            Account account = AccountHandler.getPlayerAccount(player);
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            if (!difficulty.doesAllowTera()) event.setCanceled(true);

        }

    }

}
