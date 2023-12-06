package com.lypaka.betterhardcore;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static String bossBarColor;
    public static String bossBarTitle;
    public static List<String> difficulties;
    public static String forceDifficulty;
    public static Map<String, String> messages;

    public static void load() throws ObjectMappingException {

        bossBarColor = BetterHardcore.configManager.getConfigNode(0, "Boss-Bar-Settings", "Color").getString();
        bossBarTitle = BetterHardcore.configManager.getConfigNode(0, "Boss-Bar-Settings", "Title").getString();
        difficulties = BetterHardcore.configManager.getConfigNode(0, "Difficulties").getList(TypeToken.of(String.class));
        forceDifficulty = BetterHardcore.configManager.getConfigNode(0, "Settings", "Force-Difficulty").getString();
        messages = BetterHardcore.configManager.getConfigNode(0, "Messages").getValue(new TypeToken<Map<String, String>>() {});
        if (!messages.containsKey("Death-Message")) {

            BetterHardcore.configManager.getConfigNode(0, "Messages", "Death-Message").setValue("&e%player% whited out!");
            BetterHardcore.configManager.save();
            messages.put("Death-Message", "&e%player% whited out!");

        }

    }

}
