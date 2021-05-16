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
            return;
        }
        players.put(uuid, new PlayerWrapper(uuid));
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
