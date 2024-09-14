package com.lypaka.betterhardcore.Commands;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.PermissionHandler;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.Arrays;

public class SetCommand {

    public SetCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterHardcoreCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("set")
                                            .then(
                                                    Commands.argument("module", StringArgumentType.word())
                                                            .suggests(
                                                                    (context, builder) -> ISuggestionProvider.suggest(Arrays.asList("catch", "level"), builder)
                                                            )
                                                            .then(
                                                                    Commands.argument("player", EntityArgument.players())
                                                                            .then(
                                                                                    Commands.argument("level", IntegerArgumentType.integer())
                                                                                            .executes(c -> {

                                                                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                                                                    if (!PermissionHandler.hasPermission(player, "betterhardcore.command.admin")) {

                                                                                                        player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"), player.getUniqueID());
                                                                                                        return 1;

                                                                                                    }

                                                                                                }

                                                                                                ServerPlayerEntity target = EntityArgument.getPlayer(c, "player");
                                                                                                int value = IntegerArgumentType.getInteger(c, "level");
                                                                                                String module = StringArgumentType.getString(c, "module");

                                                                                                Account account;
                                                                                                if (!AccountHandler.accountMap.containsKey(target.getUniqueID())) {

                                                                                                    c.getSource().sendErrorMessage(FancyText.getFormattedText("&cCouldn't detect account for " + target.getName().getString()));
                                                                                                    return 0;

                                                                                                } else {

                                                                                                    account = AccountHandler.accountMap.get(target.getUniqueID());
                                                                                                    if (module.equalsIgnoreCase("catch")) {

                                                                                                        account.setCatchingLevel(value);

                                                                                                    } else {

                                                                                                        account.setLevelingLevel(value);

                                                                                                    }
                                                                                                    c.getSource().sendFeedback(FancyText.getFormattedText("&aSuccessfully set " + target.getName().getString() + "'s level in the " + module + " module to " + value), true);

                                                                                                }

                                                                                                return 0;

                                                                                            })
                                                                            )
                                                            )
                                            )
                            )
            );

        }

    }

}
