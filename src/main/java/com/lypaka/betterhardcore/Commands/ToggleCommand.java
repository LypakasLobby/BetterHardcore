package com.lypaka.betterhardcore.Commands;

import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ToggleCommand {

    public ToggleCommand (CommandDispatcher<CommandSource> dispatcher) {

        for (String a : BetterHardcoreCommand.ALIASES) {

            dispatcher.register(
                    Commands.literal(a)
                            .then(
                                    Commands.literal("toggle")
                                            .executes(c -> {

                                                if (c.getSource().getEntity() instanceof ServerPlayerEntity) {

                                                    ServerPlayerEntity player = (ServerPlayerEntity) c.getSource().getEntity();
                                                    AccountHandler.toggleBossBar(player);

                                                }

                                                return 1;

                                            })
                            )
            );

        }

    }

}
