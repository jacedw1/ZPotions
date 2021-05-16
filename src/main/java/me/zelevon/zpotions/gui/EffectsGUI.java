package me.zelevon.zpotions.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.zelevon.zpotions.ZPotions;
import me.zelevon.zpotions.config.Config;
import me.zelevon.zpotions.storage.cache.PlayerCache;
import me.zelevon.zpotions.storage.cache.PlayerWrapper;
import me.zelevon.zpotions.utils.MessageHandler;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class EffectsGUI implements InventoryProvider {

    private ZPotions plugin;
    private Config.Settings conf;

    @Override
    public void init(Player player, InventoryContents contents) {
        this.plugin = ZPotions.getInstance();
        this.conf = plugin.getConf();

        ItemStack fillItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillMeta = fillItem.getItemMeta();
        fillMeta.setDisplayName("");
        fillItem.setItemMeta(fillMeta);
        contents.fillBorders(ClickableItem.empty(fillItem));

        PlayerWrapper wrapper = PlayerCache.get(player);

        ItemStack deniedItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = deniedItem.getItemMeta();
        meta.setDisplayName(MessageHandler.colorize("&c&lLOCKED"));
        deniedItem.setItemMeta(meta);

        int baseCol = 2;
        for(int i = 0; i < 3; i++) {
            if(!player.hasPermission("zp.slots." + (i + 1))) {
                contents.set(1, baseCol + i*2, ClickableItem.empty(deniedItem));
            }
            else {
                Integer index = wrapper.getPotion(i);
                ItemStack potionItem;
                if(index != null && index < conf.getAvailablePotions().size()) {
                    potionItem = conf.getAvailablePotions().get(index).getPotionItem();
                }
                else {
                    potionItem = new ItemStack(Material.POTION);
                    PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
                    potionMeta.setDisplayName(MessageHandler.colorize("&bClick me to select an effect!"));
                    potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                    potionItem.setItemMeta(potionMeta);
                }
                final int slot = i;
                contents.set(1, baseCol + i*2, ClickableItem.of(potionItem, e -> {
                    wrapper.setLastSelectedSlot(slot);
                    plugin.getPotions().open(player);
                }));
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
