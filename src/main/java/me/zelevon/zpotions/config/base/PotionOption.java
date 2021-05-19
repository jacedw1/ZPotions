package me.zelevon.zpotions.config.base;

import com.google.common.collect.Lists;
import me.zelevon.zpotions.utils.MessageHandler;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class PotionOption {

    @Setting
    private String potionType = "SPEED";

    @Setting
    private int amplifier = 1;

    @Setting
    private String permission = "zp.speedII";

    @Setting
    private String itemName = "&bSpeed II";

    @Setting
    private List<String> itemLore = Lists.newArrayList("", "&7Gain permanent Speed II");

    public PotionEffect getPotionEffect() {
        return new PotionEffect(PotionEffectType.getByName(potionType), 1000000, amplifier, true, true, true);
    }

    public String getPermission() {
        return permission;
    }

    public ItemStack getPotionItem() {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        PotionType type;
        try {
            type = PotionType.valueOf(potionType);
        }
        catch (IllegalArgumentException e) {
            type = null;
        }
        PotionData potionData = type != null ? new PotionData(type) : new PotionData(PotionType.LUCK);
        potionMeta.setBasePotionData(potionData);
        potionMeta.setDisplayName(MessageHandler.colorize(itemName));
        potionMeta.setLore(MessageHandler.colorize(itemLore));
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(potionMeta);
        return item;
    }

    public ItemStack getDeniedItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageHandler.colorize(itemName));
        meta.setLore(MessageHandler.colorize(itemLore));
        item.setItemMeta(meta);
        return item;
    }
}
