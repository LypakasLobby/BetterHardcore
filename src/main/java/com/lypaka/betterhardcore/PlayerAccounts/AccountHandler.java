package com.lypaka.betterhardcore.PlayerAccounts;

import com.google.common.reflect.TypeToken;
import com.lypaka.betterhardcore.API.PlayerWhiteOutEvent;
import com.lypaka.betterhardcore.BetterHardcore;
import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.Listeners.RespawnListener;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.Listeners.JoinListener;
import com.lypaka.lypakautils.MiscHandlers.LogicalPixelmonMoneyHandler;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.common.MinecraftForge;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.text.DecimalFormat;
import java.util.*;

public class AccountHandler {

    public static Map<UUID, Account> accountMap = new HashMap<>();
    private static Map<Account, Integer> counterMap = new HashMap<>();
    private static Map<UUID, ServerBossInfo> timeBossBar = new HashMap<>();

    public static void whiteOut (ServerPlayerEntity player, String reason) throws ObjectMappingException {

        PlayerPartyStorage storage = StorageProxy.getParty(player);
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());

        PlayerWhiteOutEvent whiteOutEvent = new PlayerWhiteOutEvent(player);
        MinecraftForge.EVENT_BUS.post(whiteOutEvent);
        if (whiteOutEvent.isCanceled()) return;

        if (ConfigGetters.messages.containsKey("Death-Message")) {

            String deathMessage = ConfigGetters.messages.get("Death-Message");
            for (Map.Entry<UUID, ServerPlayerEntity> entry : JoinListener.playerMap.entrySet()) {

                entry.getValue().sendMessage(FancyText.getFormattedText(deathMessage.replace("%player%", player.getName().getString())), entry.getKey());

            }

        }
        if (difficulty.doesKillPlayerForLosing() && !reason.equalsIgnoreCase("player death")) {

            player.onKillCommand();

        } else {

            if (difficulty.getMoneyLost() > 0) {

                double balance = storage.getBalance().doubleValue();
                DecimalFormat df = new DecimalFormat("#.##");
                double value = (balance * difficulty.getMoneyLost());
                String formatted = df.format(value);
                double updatedMoney = Double.parseDouble(formatted);
                LogicalPixelmonMoneyHandler.remove(player.getUniqueID(), updatedMoney);

            }
            if (difficulty.doesClearInventory()) {

                player.inventory.clear();

            }
            for (int i = 0; i < 6; i++) {

                storage.set(i, null);

            }

            account.setWhiteouts(account.getWhiteouts() + 1);
            account.setDaysSurvived(0);
            account.setSecondsSurvived(0);
            // Remove all Pokemon and try to pull from PC
            // If nothing in PC, give them new starter
            PCStorage pc = StorageProxy.getPCForPlayer(player);
            Pokemon[] storedPokemon = pc.getAll();
            Pokemon firstPokemon = null;
            for (Pokemon p : storedPokemon) {

                if (p != null) {

                    firstPokemon = p;
                    break;

                }

            }
            if (firstPokemon != null) {

                int box = pc.getPosition(firstPokemon).box;
                int slot = pc.getPosition(firstPokemon).order;
                storage.add(firstPokemon);
                pc.set(box, slot, null);

            } else {

                RespawnListener.playersRespawningFromBattleDeath.add(player.getUniqueID());

            }

        }

    }

    public static Account getPlayerAccount (UUID uuid) throws ObjectMappingException {

        Account account = accountMap.getOrDefault(uuid, null);
        if (account == null) {

            int catchingLevel = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Catching-Level").getInt();
            boolean counterDisplayed = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Counter-Displayed").getBoolean();
            int daysSurvived = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Days-Survived").getInt();
            String difficulty = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Difficulty").getString();
            int levelingLevel = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Leveling-Level").getInt();
            int secondsSurvived = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Seconds-Survived").getInt();
            ArrayList<String> caughtSpecies = new ArrayList<>(BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Used-Species").getList(TypeToken.of(String.class)));
            int whiteouts = BetterHardcore.playerConfigManager.getPlayerConfigNode(uuid, "Whiteouts").getInt();
            account = new Account(uuid, catchingLevel, counterDisplayed, daysSurvived, difficulty, levelingLevel, secondsSurvived, caughtSpecies, whiteouts);
            account.create();

        }

        return account;

    }

    public static Account getPlayerAccount (ServerPlayerEntity player) throws ObjectMappingException {

        return getPlayerAccount(player.getUniqueID());

    }

    public static void toggleBossBar (ServerPlayerEntity player) {

        Account account = accountMap.get(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;
        if (!timeBossBar.containsKey(player.getUniqueID())) {

            // player currently does not see the bar and wants to turn it on
            ServerBossInfo bar = new ServerBossInfo(
                    FancyText.getFormattedText(ConfigGetters.bossBarTitle
                            .replace("%day%", String.valueOf(account.getDaysSurvived()))
                            .replace("%difficulty%", account.getDifficulty())
                    ),
                    getColorFromName(ConfigGetters.bossBarColor),
                    BossInfo.Overlay.PROGRESS
            );
            int max = 1200;
            int current = account.getSecondsSurvived();
            float percent = (float) current / max;
            bar.setPercent(percent);
            bar.addPlayer(player);
            bar.setVisible(true);
            timeBossBar.put(player.getUniqueID(), bar);

        } else {

            timeBossBar.entrySet().removeIf(e -> {

                if (e.getKey().toString().equalsIgnoreCase(player.getUniqueID().toString())) {

                    e.getValue().setVisible(false);
                    e.getValue().removePlayer(player);
                    return true;

                }

                return false;

            });

        }

    }

    public static void startTrackerTask() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                for (Map.Entry<UUID, Account> entry : AccountHandler.accountMap.entrySet()) {

                    entry.getValue().setSecondsSurvived(entry.getValue().getSecondsSurvived() + 1);
                    if (entry.getValue().getSecondsSurvived() == 1200) {

                        entry.getValue().setDaysSurvived(entry.getValue().getDaysSurvived() + 1);
                        entry.getValue().setSecondsSurvived(0);

                    }
                    if (timeBossBar.containsKey(entry.getKey())) {

                        float percent = (float) entry.getValue().getSecondsSurvived() / 1200;
                        timeBossBar.get(entry.getKey()).setPercent(percent);
                        timeBossBar.get(entry.getKey()).setName(FancyText.getFormattedText(ConfigGetters.bossBarTitle
                                .replace("%day%", String.valueOf(entry.getValue().getDaysSurvived()))
                                .replace("%difficulty%", entry.getValue().getDifficulty())
                        ));

                    }
                    int count = 0;
                    if (counterMap.containsKey(entry.getValue())) {

                        count = counterMap.get(entry.getValue());

                    }
                    // saves account every 5 seconds for safety
                    if (count == 5) {

                        entry.getValue().saveWholeAccount();
                        count = -1;

                    }
                    count++;
                    counterMap.put(entry.getValue(), count);

                }

            }

        }, 0, 1000);

    }

    private static BossInfo.Color getColorFromName (String name) {

        switch (name.toLowerCase()) {

            case "pink":
                return BossInfo.Color.PINK;

            case "blue":
                return BossInfo.Color.BLUE;

            case "red":
                return BossInfo.Color.RED;

            case "green":
                return BossInfo.Color.GREEN;

            case "yellow":
                return BossInfo.Color.YELLOW;

            case "purple":
                return BossInfo.Color.PURPLE;

            default:
                return BossInfo.Color.WHITE;

        }

    }

}
