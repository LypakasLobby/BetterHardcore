package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class PlayerDeathListener {

    @SubscribeEvent
    public void onDeath (LivingDeathEvent event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {

            ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();
            Account account = AccountHandler.getPlayerAccount(player);
            if (!account.getDifficulty().equalsIgnoreCase("none")) {

                AccountHandler.whiteOut(player, "player death");

            }

        }

    }

}
