package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.pixelmonmod.pixelmon.api.config.BetterSpawnerConfig;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class PixelmonFaintListener {

    @SubscribeEvent
    public void onPokemonDeath (LivingDeathEvent event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        if (event.getEntity() instanceof PixelmonEntity) {

            PixelmonEntity pixelmon = (PixelmonEntity) event.getEntity();
            Pokemon pokemon = pixelmon.getPokemon();
            if (pixelmon.hasOwner()) {

                if (pixelmon.getOwner() instanceof ServerPlayerEntity) {

                    ServerPlayerEntity player = pixelmon.getPokemon().getOwnerPlayer();
                    Account account = AccountHandler.getPlayerAccount(player);
                    if (!account.getDifficulty().equalsIgnoreCase("none")) {

                        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
                        if (difficulty.isNuzlockeMode()) {

                            if (BattleRegistry.getBattle(player) == null) { // don't want to be calling this from battles, there's already a listener for that

                                PlayerPartyStorage storage = StorageProxy.getParty(player);
                                for (int i = 0; i < 6; i++) {

                                    Pokemon p = storage.get(i);
                                    if (p != null) {

                                        if (p == pokemon) {

                                            storage.set(i, null);

                                        }

                                    }

                                }

                                if (storage.getFirstAblePokemon() == null) {

                                    AccountHandler.whiteOut(player, "pixelmon fainted");

                                }

                            }

                        }

                    }

                }

            }

        }

    }

}
