package com.lypaka.betterhardcore.Difficulties.Modules;

import java.util.Map;

public class LevelingModule {

    private final boolean enabled;
    private final boolean canBeDisobedient;
    private final Map<String, Integer> tierMap;

    public LevelingModule (boolean enabled, boolean canBeDisobedient, Map<String, Integer> tierMap) {

        this.enabled = enabled;
        this.canBeDisobedient = canBeDisobedient;
        this.tierMap = tierMap;

    }

    public boolean isEnabled() {

        return enabled;

    }

    public boolean canBeDisobedient() {

        return this.canBeDisobedient;

    }

    public Map<String, Integer> getTierMap() {

        return this.tierMap;

    }

}
