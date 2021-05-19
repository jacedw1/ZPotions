package me.zelevon.zpotions.listeners;

import me.zelevon.zpotions.ZPotions;
import me.zelevon.zpotions.storage.cache.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(ZPotions.getInstance(), () -> PlayerCache.add(e.getPlayer()));
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        PlayerCache.get(e.getPlayer()).removeEffects();
        PlayerCache.remove(e.getPlayer());
    }
}
