package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.LogicalPixelmonMoneyHandler;
import com.pixelmonmod.pixelmon.api.events.npc.NPCEvent;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class NPCInteractListener {

    @SubscribeEvent
    public void onNPCInteract (NPCEvent.Interact event) throws ObjectMappingException {

        PlayerEntity player = event.player;
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        if (event.npc instanceof NPCNurseJoy) {

            if (!difficulty.doesAllowHealers()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

            } else {

                int cost = difficulty.getCostToHeal();
                int balance = (int) LogicalPixelmonMoneyHandler.getBalance(player.getUniqueID());
                if (cost > balance) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Money-Error")), player.getUniqueID());

                } else {

                    LogicalPixelmonMoneyHandler.remove(player.getUniqueID(), cost);

                }

            }

        } else if (event.npc instanceof NPCTrader) {

            if (!difficulty.doesAllowTrading()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

            }

        } else if (event.npc instanceof NPCShopkeeper) {

            if (!difficulty.doesAllowShopkeepers()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

            }

        }

    }

}
