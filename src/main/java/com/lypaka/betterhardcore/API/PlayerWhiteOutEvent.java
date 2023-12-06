package com.lypaka.betterhardcore.API;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Called when a player loses a battle, mostly intended to be used for external leaderboards, win/loss counter, or some odd-ball events
 * If canceled, the player has no "punishment" for losing, basically death is ignored
 */
@Cancelable
public class PlayerWhiteOutEvent extends Event {

    private final ServerPlayerEntity player;

    public PlayerWhiteOutEvent (ServerPlayerEntity player) {

        this.player = player;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

}
