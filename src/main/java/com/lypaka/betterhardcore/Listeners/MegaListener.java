package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.pixelmonmod.pixelmon.api.events.MegaEvolutionEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class MegaListener {

    @SubscribeEvent
    public void onMega (MegaEvolutionEvent.Battle event) throws ObjectMappingException {

        if (ConfigGetters.gcesMode) return;
        if (event.getPlayer() == null) return;
        ServerPlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player);
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        if (!difficulty.getBattleModule().getMegaPermission().equalsIgnoreCase("")) {

            if (!PermissionHandler.hasPermission(player, difficulty.getBattleModule().getMegaPermission())) {

                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Mega-Error")), player.getUniqueID());
                event.setCanceled(true);

            }

        }

    }

}
