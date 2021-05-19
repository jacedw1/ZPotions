package me.zelevon.zpotions.storage.cache;

import com.google.common.collect.Maps;
import me.zelevon.zpotions.ZPotions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerCache {

    private static Map<UUID, PlayerWrapper> players = Maps.newHashMap();

    public static void add(Player player) {
        UUID uuid = player.getUniqueId();
        if(players.containsKey(uuid)) {
            remove(player);
        }
        Bukkit.getScheduler().runTaskAsynchronously(ZPotions.getInstance(), () -> {
            PlayerWrapper wrapper = new PlayerWrapper(uuid);
            players.put(uuid, wrapper);
        });
    }

    public static void remove(Player player) {
        UUID uuid = player.getUniqueId();
        if(!players.containsKey(uuid)) {
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(ZPotions.getInstance(), () -> {
            players.get(uuid).save();
            players.remove(uuid);
        });
    }

    public static PlayerWrapper get(Player player) {
        return players.get(player.getUniqueId());
    }

    public static void saveAll() {
        Bukkit.getLogger().log(Level.INFO, "Saving all players.");
        for(PlayerWrapper wrapper : players.values()) {
            wrapper.save();
        }
        Bukkit.getLogger().log(Level.INFO, "Successfully saved all players.");
    }

}
