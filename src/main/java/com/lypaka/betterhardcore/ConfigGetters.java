package com.lypaka.betterhardcore;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.List;
import java.util.Map;

public class ConfigGetters {

    public static String bossBarColor;
    public static String bossBarTitle;
    public static List<String> difficulties;
    public static boolean gcesMode;
    public static String forceDifficulty;
    public static Map<String, String> messages;

    public static void load() throws ObjectMappingException {

        boolean needsSaving = false;
        bossBarColor = BetterHardcore.configManager.getConfigNode(0, "Boss-Bar-Settings", "Color").getString();
        bossBarTitle = BetterHardcore.configManager.getConfigNode(0, "Boss-Bar-Settings", "Title").getString();
        difficulties = BetterHardcore.configManager.getConfigNode(0, "Difficulties").getList(TypeToken.of(String.class));
        gcesMode = false;
        if (BetterHardcore.configManager.getConfigNode(0, "GCES-Mode").isVirtual()) {

            needsSaving = true;
            BetterHardcore.configManager.getConfigNode(0, "GCES-Mode").setValue(false);
            BetterHardcore.configManager.getConfigNode(0, "GCES-Mode").setComment("If you set this to true, the mod will basically act like my old GCES mod where only the catching and leveling tiers are active");

        } else {

            gcesMode = BetterHardcore.configManager.getConfigNode(0, "GCES-Mode").getBoolean();

        }
        forceDifficulty = BetterHardcore.configManager.getConfigNode(0, "Settings", "Force-Difficulty").getString();
        messages = BetterHardcore.configManager.getConfigNode(0, "Messages").getValue(new TypeToken<Map<String, String>>() {});
        if (!messages.containsKey("Death-Message")) {

            if (!needsSaving) needsSaving = true;
            BetterHardcore.configManager.getConfigNode(0, "Messages", "Death-Message").setValue("&e%player% whited out!");
            messages.put("Death-Message", "&e%player% whited out!");

        }

        if (needsSaving) {

            BetterHardcore.configManager.save();

        }

    }

}
