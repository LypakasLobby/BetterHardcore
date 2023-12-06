package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.events.HeldItemChangedEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class HeldItemListener {

    @SubscribeEvent
    public void onHeldItemChange (HeldItemChangedEvent event) throws ObjectMappingException {

        if (event.pokemon.getOwnerPlayer() == null) return;

        // I can't believe I have to put this here
        String currentItemID = event.pokemon.getHeldItem().getItem().getRegistryName().toString();
        String newItemID = event.newHeldItem.getItem().getRegistryName().toString();
        if (newItemID.equals("minecraft:air") && currentItemID.equals("minecraft:air")) return;

        ServerPlayerEntity player = event.player;
        if (!AccountHandler.accountMap.containsKey(player.getUniqueID())) return;
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        if (!difficulty.doesAllowHeldItems()) {

            event.setCanceled(true);
            player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

        }

    }

}
