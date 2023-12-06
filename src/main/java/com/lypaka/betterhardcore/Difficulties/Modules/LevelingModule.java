package com.lypaka.betterhardcore.Difficulties.Modules;

import java.util.Map;

public class LevelingModule {

    private final boolean enabled;
    private final Map<String, Integer> tierMap;

    public LevelingModule (boolean enabled, Map<String, Integer> tierMap) {

        this.enabled = enabled;
        this.tierMap = tierMap;

    }

    public boolean isEnabled() {

        return enabled;

    }

    public Map<String, Integer> getTierMap() {

        return this.tierMap;

    }

}
