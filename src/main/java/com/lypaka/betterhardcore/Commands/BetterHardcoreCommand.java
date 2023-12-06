package com.lypaka.betterhardcore.Commands;

import com.lypaka.betterhardcore.BetterHardcore;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = BetterHardcore.MOD_ID)
public class BetterHardcoreCommand {

    public static List<String> ALIASES = Arrays.asList("betterhardcore", "hardcore", "bhardcore");

    @SubscribeEvent
    public static void onCommandRegistration (RegisterCommandsEvent event) {

        new AssignCommand(event.getDispatcher());
        new ReloadCommand(event.getDispatcher());
        new ToggleCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());

    }

}
