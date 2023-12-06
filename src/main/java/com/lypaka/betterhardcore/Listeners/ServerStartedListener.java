package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.BetterHardcore;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

@Mod.EventBusSubscriber(modid = BetterHardcore.MOD_ID)
public class ServerStartedListener {

    @SubscribeEvent
    public static void onServerStart (FMLServerStartedEvent event) {

        MinecraftForge.EVENT_BUS.register(new InteractionListeners());
        MinecraftForge.EVENT_BUS.register(new LoginListener());
        MinecraftForge.EVENT_BUS.register(new PlayerDeathListener());
        MinecraftForge.EVENT_BUS.register(new RespawnListener());
        MinecraftForge.EVENT_BUS.register(new PixelmonFaintListener());

        Pixelmon.EVENT_BUS.register(new BattleListeners());
        Pixelmon.EVENT_BUS.register(new CaptureListeners());
        Pixelmon.EVENT_BUS.register(new DynamaxListener());
        Pixelmon.EVENT_BUS.register(new EXPListeners());
        Pixelmon.EVENT_BUS.register(new HeldItemListener());
        Pixelmon.EVENT_BUS.register(new MegaListener());
        Pixelmon.EVENT_BUS.register(new NPCInteractListener());
        Pixelmon.EVENT_BUS.register(new PixelmonSpawnListener());

        if (ModList.get().isLoaded("catalystterapokemon")) {

            MinecraftForge.EVENT_BUS.register(new TeraListeners());

        }

    }

}
