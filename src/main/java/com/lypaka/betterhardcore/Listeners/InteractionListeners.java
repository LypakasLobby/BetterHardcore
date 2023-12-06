package com.lypaka.betterhardcore.Listeners;

import com.lypaka.betterhardcore.ConfigGetters;
import com.lypaka.betterhardcore.Difficulties.Difficulty;
import com.lypaka.betterhardcore.Difficulties.DifficultyHandler;
import com.lypaka.betterhardcore.PlayerAccounts.Account;
import com.lypaka.betterhardcore.PlayerAccounts.AccountHandler;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.MiscHandlers.LogicalPixelmonMoneyHandler;
import com.pixelmonmod.pixelmon.entities.npcs.NPCNurseJoy;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrader;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.items.ExpCandyItem;
import com.pixelmonmod.pixelmon.items.PokeBallItem;
import com.pixelmonmod.pixelmon.items.ReviveItem;
import com.pixelmonmod.pixelmon.items.TechnicalMoveItem;
import com.pixelmonmod.pixelmon.items.medicine.MedicineItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.UUID;

public class InteractionListeners {

    @SubscribeEvent
    public void onBlockInteract (PlayerInteractEvent.RightClickBlock event) throws ObjectMappingException {

        if (event.getHand() != Hand.MAIN_HAND) return;
        if (event.getSide() == LogicalSide.CLIENT) return;

        PlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        String blockID = player.world.getBlockState(event.getPos()).getBlock().getRegistryName().toString();
        if (blockID.contains("pixelmon") && blockID.contains("healer")) {

            if (!difficulty.doesAllowHealers()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

            } else {

                int cost = difficulty.getCostToHeal();
                int balance = (int) LogicalPixelmonMoneyHandler.getBalance(player.getUniqueID());
                if (cost > balance) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Money-Error")), player.getUniqueID());

                } else {

                    LogicalPixelmonMoneyHandler.remove(player.getUniqueID(), cost);

                }

            }

        } else if (blockID.contains("pixelmon") && blockID.contains("trade_machine")) {

            if (!difficulty.doesAllowTrading()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

            }

        } else if (blockID.contains("pixelmon") && blockID.contains("day_care")) {

            if (!difficulty.doesAllowBreeding()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("General-Error")), player.getUniqueID());

            }

        }

    }

    @SubscribeEvent
    public void onEntityInteract (PlayerInteractEvent.EntityInteract event) throws ObjectMappingException {

        if (event.getHand() != Hand.MAIN_HAND) return;
        if (event.getSide() == LogicalSide.CLIENT) return;

        PlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        if (event.getTarget() instanceof PixelmonEntity) {

            PixelmonEntity pixelmon = (PixelmonEntity) event.getTarget();
            if (pixelmon.hasOwner()) {

                if (pixelmon.getOwner() instanceof ServerPlayerEntity) {

                    UUID owner = pixelmon.getOwnerUniqueId();
                    UUID playerUUID = player.getUniqueID();
                    if (!owner.toString().equalsIgnoreCase(playerUUID.toString())) return;

                    ItemStack item = player.getHeldItemMainhand();
                    if (item.getItem() instanceof TechnicalMoveItem) {

                        if (!difficulty.doesAllowTMsTRs()) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                        }

                    } else if (item.getItem() instanceof ExpCandyItem) {

                        String id = item.getItem().getRegistryName().toString();
                        if (id.equalsIgnoreCase("pixelmon:rare_candy")) {

                            if (!difficulty.doesAllowRareCandies()) {

                                event.setCanceled(true);
                                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                            }

                        } else if (!difficulty.doesAllowXPCandies()) {

                            event.setCanceled(true);
                            player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                        }

                    }

                }

            }

        }

    }

    @SubscribeEvent
    public void onItemInteract (PlayerInteractEvent.RightClickItem event) throws ObjectMappingException {

        if (event.getHand() != Hand.MAIN_HAND) return;
        if (event.getSide() == LogicalSide.CLIENT) return;

        PlayerEntity player = event.getPlayer();
        Account account = AccountHandler.getPlayerAccount(player.getUniqueID());
        if (account.getDifficulty().equalsIgnoreCase("none")) return;

        Difficulty difficulty = DifficultyHandler.getFromName(account.getDifficulty());
        ItemStack item = event.getItemStack();
        if (item.getItem() instanceof MedicineItem) {

            if (!difficulty.doesAllowHealingOutsideOfBattles()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

            }

        } else if (item.getItem() instanceof PokeBallItem) {

            String ball = CaptureListeners.getActualPokeBallNameBecausePixelmonChangedThisForLiterallyNoReasonLOL(item);
            if (ball.equalsIgnoreCase("pixelmon:master_ball") || ball.equalsIgnoreCase("pixelmon:park_ball")) {

                if (!difficulty.doesAllowMasterParkBalls()) {

                    event.setCanceled(true);
                    player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

                }

            }

        } else if (item.getItem() instanceof ReviveItem) {

            if (!difficulty.doesAllowRevives()) {

                event.setCanceled(true);
                player.sendMessage(FancyText.getFormattedText(ConfigGetters.messages.get("Item-Clause")), player.getUniqueID());

            }

        }

    }

}
