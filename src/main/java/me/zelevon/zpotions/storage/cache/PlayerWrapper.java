package me.zelevon.zpotions.storage.cache;


import me.zelevon.zpotions.ZPotions;
import me.zelevon.zpotions.config.base.PotionOption;
import me.zelevon.zpotions.storage.Queries;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerWrapper {

    private UUID uuid;
    private int[] potions;
    private int lastSelectedSlot = -1;

    public PlayerWrapper(UUID uuid) {
        this.uuid = uuid;
        this.potions = Queries.getPlayer(uuid);
        addEffects();
    }

    public Integer getPotion(int index) {
        Integer res = potions[index] - 1;
        if(res == -1) {
            res = null;
        }
        return res;
    }

    public void setPotion(int value) {
        potions[lastSelectedSlot] = value + 1;
    }

    public int getLastSelectedSlot() {
        return lastSelectedSlot;
    }

    public void setLastSelectedSlot(int lastSelectedSlot) {
        this.lastSelectedSlot = lastSelectedSlot;
    }

    public void addEffects() {
        Player player = Bukkit.getPlayer(uuid);
        List<PotionOption> availablePotions = ZPotions.getInstance().getConf().getAvailablePotions();
        for(int i = 0; i < 3; i++) {
            if(!player.hasPermission("zp.slots." + (i+1))) {
                potions[i] = 0;
            }
            Integer index = getPotion(i);
            if(index != null) {
                Bukkit.getScheduler().runTask(ZPotions.getInstance(), () -> player.addPotionEffect(availablePotions.get(index).getPotionEffect()));
            }
        }
    }

    public void removeEffects() {
        Player player = Bukkit.getPlayer(uuid);
        List<PotionOption> availablePotions = ZPotions.getInstance().getConf().getAvailablePotions();
        for(int i = 0; i < 3; i++) {
            Integer index = getPotion(i);
            if(index != null) {
                player.removePotionEffect(availablePotions.get(index).getPotionEffect().getType());
            }
        }
    }

    public void clear() {
        this.potions[0] = 0;
        this.potions[1] = 0;
        this.potions[2] = 0;
    }

    public void save() {
        Queries.savePlayer(uuid, potions[0], potions[1], potions[2]);
    }
}
