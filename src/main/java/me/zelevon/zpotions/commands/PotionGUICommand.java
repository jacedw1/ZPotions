package me.zelevon.zpotions.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.zelevon.zpotions.ZPotions;
import me.zelevon.zpotions.storage.cache.PlayerCache;
import me.zelevon.zpotions.utils.MessageHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("potions|zpotions|zp|potion|zpotion")
public class PotionGUICommand extends BaseCommand {

    private ZPotions plugin;

    public PotionGUICommand() {
        this.plugin = ZPotions.getInstance();
    }

    @Default
    @CommandPermission("zp.slots.1")
    public void onPotionCommand(Player player) {
        plugin.getEffects().open(player);
    }

    @Subcommand("clear")
    @CommandPermission("zp.slots.1")
    public void onClearCommand(Player player) {
        PlayerCache.get(player).clear();
        MessageHandler.msg(player, "&bSuccessfully removed Z-Potion effects!");
    }

    @Subcommand("clearplayer")
    @CommandPermission("zp.clear.others")
    @CommandCompletion("@players")
    public void onClearOthers(CommandSender sender, Player target) {
        PlayerCache.get(target).clear();
        MessageHandler.msg(sender, "&bSuccessfully removed Z-Potion effects from &3" + target.getName() + "&b!");
        MessageHandler.msg(target, "&bYour Z-Potion effects were cleared by a member of staff.");
    }
}
