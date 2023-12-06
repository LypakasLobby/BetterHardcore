package com.lypaka.betterhardcore.Commands;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class AssignCommand {

    public AssignCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterHardcoreCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("assign")
                                            .then(
                                                    Commands.argument("player", EntityArgument.players())
                                                            .then(
                                                                    Commands.argument("difficulty", StringArgumentType.word())
                                                                            .suggests(
                                                                                    (context, builder) -> ISuggestionProvider.suggest(ConfigGetters.difficulties, builder)
                                                                            )
                                                                            .executes(c -> {

                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                    if (!PermissionHandler.hasPermission(player, "betterhardcore.command.admin")) {

                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                        return 0;

                                                                                    }

                                                                                }

                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                                String diff = StringArgumentType.getString(c, "difficulty");
                                                                                Difficulty difficulty = DifficultyHandler.getFromName(diff);
                                                                                if (difficulty == null) {

                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cInvalid difficulty name!"));
                                                                                    return 0;

                                                                                } else {

                                                                                    try {

                                                                                        Account account = AccountHandler.getPlayerAccount(target.getUniqueID());
                                                                                        DifficultyHandler.assignDifficulty(account, difficulty);
                                                                                        c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully assigned the " + difficulty.getName() + " Difficulty to " + target.getName().getString()), true);
                                                                                        return 1;

                                                                                    } catch (ObjectMappingException e) {

                                                                                        throw new RuntimeException(e);

                                                                                    }

                                                                                }

                                                                            })
                                                            )
                                            )
                            )
            );

        }

    }

}
