package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.events.DynamaxEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class DynamaxListener {

    @SubscribeEvent
    public void onDynamax (DynamaxEvent.BattleEvolve event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        ServerPlayerEntity player = event.pw.getPlayerOwner();
        if (player != null) {

            Account account = AccountHandler.getPlayerAccount(player);
            if (account.getDifficulty().equalsIgnoreCase("none")) return;

            Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
            if (!difficulty.getBattleModule().getDynamaxPermission().equalsIgnoreCase("")) {

                if (!PermissionHandler.hasPermission(player, difficulty.getBattleModule().getDynamaxPermission())) {

                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Dynamax-Error")), player.getUniqueID());
                    event.setCanceled(true);

                }

            }

        }

    }

}
