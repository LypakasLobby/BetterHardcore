package com.lypaka.betterhardcore;

import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.ConfigurationLoaders.PlayerConfigManager;
import net.minecraftforge.fml.common.Mod;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("betterhardcore")
public class BetterHardcore {

    public static final String MOD_ID = "betterhardcore";
    public static final String MOD_NAME = "BetterHardcore";
    public static Logger logger = LogManager.getLogger("BetterHardcore");
    public static BasicConfigManager configManager;
    public static PlayerConfigManager playerConfigManager;
    public static Map<String, BasicConfigManager> difficultyConfigManager;
    public static Path dir;

    /**
     * TODO
     * Pokemon obedience based on prestige (not currently possible with Pixelmon's API as of version 9.1.0)
     *      prestige increases via winning battles and for each day survived
     *
     *
     *
     */

    /**
     * In progress
     *
     */

    /**
     * Done
     * All Pokemon spawn with low (configurable) % IVs
     * Kill player on losing battles
     * Remove money on losing battles
     * Remove Pokemon as they faint (have to be after battles)
     *      Have to pull first found Pokemon from PC to prevent players having 0 Pokemon
     *      Make sure that if no PC Pokemon to pull, automatically open starter menu
     *
     * GCES integration for catching/leveling
     *
     * Difficulties
     *   Pay fees to use healers/doctor NPCs
     *   Wild Pokemon and NPC Pokemon critical hit chance increased (if possible - its possible, I checked - AttackEvent.CriticalHit.setCrit(true))
     *   NPC AI modified by difficulty
     *   Player Pokemon EXP values reduced by configurable amount
     *   Catch rate modifiers based on Pokemon evolution stage and level
     *   No healing items in battle
     *
     * Server toggleable option to allow player to pick difficulty or force apply a difficulty to them
     *
     * Remove all money and Pokemon on player death (pvp/Minecraft death) (configurable)
     *
     * Event listeners
     *
     * Day counter
     *      Command to hide/show day counter boss bar
     *      Task to count player playtime in seconds
     *      Update day counter every 20 minutes (1200 seconds, 1 Minecraft day)
     *
     * Commands
     */

    public BetterHardcore() throws IOException, ObjectMappingException {

        dir = ConfigUtils.checkDir(Paths.get("./config/betterhardcore"));
        String[] files = new String[]{"betterhardcore.conf"};
        configManager = new BasicConfigManager(files, dir, BetterHardcore.class, MOD_NAME, MOD_ID, logger);
        configManager.init();
        playerConfigManager = new PlayerConfigManager("account.conf", "player-accounts", dir, BetterHardcore.class, MOD_NAME, MOD_ID, logger);
        playerConfigManager.init();
        ConfigGetters.load();
        DifficultyHandler.registerDifficulties();
        //AccountHandler.startTrackerTask();

    }

}
