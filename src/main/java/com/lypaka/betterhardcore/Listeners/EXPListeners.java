package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.pixelmonmod.pixelmon.api.events.ExperienceGainEvent;
import com.pixelmonmod.pixelmon.api.events.RareCandyEvent;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class EXPListeners {

    @SubscribeEvent
    public void onEXP (ExperienceGainEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = event.pokemon.getPlayerOwner();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        if (difficulty.getLevelingModule().isEnabled()) {

            int tierLevel = account.getLevelingLevel();
            int maxLevel = difficulty.getLevelingModule().getTierMap().get("Tier-" + tierLevel);
            int pokemonLevel = event.pokemon.getPokemonLevel();

            if (pokemonLevel >= maxLevel) {

                event.setExperience(0);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Leveling-Tier-Error")), player.getUniqueID());
                return;

            }

        }
        double mod = difficulty.getExpReducerModifier();
        double original = event.getExperience();
        int updated = (int) (original * mod);
        event.setExperience(updated);

    }

    @SubscribeEvent
    public void onRareCandy (RareCandyEvent event) throws ObjectMappingException {

        ServerPlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        if (difficulty.getLevelingModule().isEnabled()) {

            int tierLevel = account.getLevelingLevel();
            int maxLevel = difficulty.getLevelingModule().getTierMap().get("Tier-" + tierLevel);
            int pokemonLevel = event.getPixelmon().getPokemon().getPokemonLevel();

            if (pokemonLevel >= maxLevel) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Leveling-Tier-Error")), player.getUniqueID());

            }

        }

    }

}
