package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.pixelmonmod.pixelmon.api.events.spawning.SpawnEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.spawning.PlayerTrackingSpawner;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class PixelmonSpawnListener {

    @SubscribeEvent
    public void onSpawn (SpawnEvent event) throws ObjectMappingException {

        if (event.action.getOrCreateEntity() instanceof PixelmonEntity) {

            if (event.spawner instanceof PlayerTrackingSpawner) {

                ServerPlayerEntity player = ((PlayerTrackingSpawner) event.spawner).getTrackedPlayer();
                Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
                if (!account.getDifficulty().equalsIgnoreCase("none")) {

                    PixelmonEntity pokemon = (PixelmonEntity) event.action.getOrCreateEntity();
                    pokemon.getPokemon().getIVs().maximizeIVs();
                    pokemon.updateStats();

                }

            }

        }

    }

}
