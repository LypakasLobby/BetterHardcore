package com.lypaka.betterhardcore.API;

import com.lypaka.betterhardcore.Difficulties.Difficulty;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class DifficultyAssignedEvent extends Event {

    private final ServerPlayerEntity player;
    private Difficulty difficulty;

    public DifficultyAssignedEvent (ServerPlayerEntity player, Difficulty difficulty) {

        this.player = player;
        this.difficulty = difficulty;

    }

    public ServerPlayerEntity getPlayer() {

        return this.player;

    }

    public Difficulty getDifficulty() {

        return this.difficulty;

    }

    public void setDifficulty (Difficulty diff) {

        this.difficulty = diff;

    }

}
