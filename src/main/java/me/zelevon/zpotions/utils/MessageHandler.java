package me.zelevon.zpotions.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageHandler {

    public static void msg(CommandSender recipient, String message) {
        recipient.sendMessage(colorize(message));
    }

    public static void msg(Player recipient, String message) {
        recipient.sendMessage(colorize(message));
    }

    public static void msg(Player recipient, String message, Object... replacements) {
        msg(recipient, format(message, replacements));
    }

    public static void msg(CommandSender recipient, String message, Object... replacements) {
        msg(recipient, format(message, replacements));
    }

    private static String format(String message, Object... replacements) {
        return String.format(message, replacements);
    }

    public static String replace(String message, String key, String replacement) {
        return message.replaceAll(key, replacement);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(List<String> message) {
        for(int i = 0; i < message.size(); i++) {
            message.set(i, colorize(message.get(i)));
        }
        return message;
    }
}
