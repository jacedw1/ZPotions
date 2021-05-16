package me.zelevon.zpotions.gui;

import com.google.common.collect.Lists;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.zelevon.zpotions.storage.cache.PlayerCache;
import me.zelevon.zpotions.storage.cache.PlayerWrapper;
import me.zelevon.zpotions.utils.MessageHandler;
import me.zelevon.zpotions.ZPotions;
import me.zelevon.zpotions.config.Config;
import me.zelevon.zpotions.config.base.PotionOption;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;

public class PotionsGUI implements InventoryProvider {

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

        Pagination pagination = contents.pagination();
        List<ClickableItem> items = Lists.newArrayList();
        List<PotionOption> availablePotions = conf.getAvailablePotions();
        PlayerWrapper wrapper = PlayerCache.get(player);
        for(PotionOption potionOption : availablePotions) {
            if (player.hasPermission(plugin.getAllEffects()) ||
                    player.hasPermission(potionOption.getPermission())) {
                ItemStack item = potionOption.getPotionItem();
                items.add(ClickableItem.of(item, e -> {
                    Integer oldIndex = wrapper.getPotion(wrapper.getLastSelectedSlot());
                    if(oldIndex != null) {
                        PotionOption oldPotion = availablePotions.get(oldIndex);
                        player.removePotionEffect(oldPotion.getPotionEffect().getType());
                    }
                    wrapper.setPotion(availablePotions.indexOf(potionOption));
                    plugin.getEffects().open(player);
                    wrapper.addEffects();
                }));
            }
            else {
                ItemStack noPerm = potionOption.getDeniedItem();
                items.add(ClickableItem.of(noPerm, e -> {
                    MessageHandler.msg(player, "&cYou do not have permission to select that potion.");
                    plugin.getEffects().open(player);
                }));
            }
        }
        pagination.setItems(items.toArray(new ClickableItem[0]));
        pagination.setItemsPerPage(7);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        ItemStack cancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(MessageHandler.colorize("&cBack"));
        cancel.setItemMeta(cancelMeta);
        contents.set(2, 4, ClickableItem.of(cancel, e -> plugin.getEffects().open(player)));

        if(items.size() > 7) {
            ItemStack pages = new ItemStack(Material.ARROW);
            ItemMeta pagesMeta = pages.getItemMeta();

            pagesMeta.setDisplayName(MessageHandler.colorize("&7Previous Page"));
            pages.setItemMeta(pagesMeta);
            contents.set(2, 2, ClickableItem.of(pages, e -> plugin.getPotions().open(player, pagination.previous().getPage())));

            pagesMeta.setDisplayName(MessageHandler.colorize("&7Next Page"));
            pages.setItemMeta(pagesMeta);
            contents.set(2, 6, ClickableItem.of(pages, e -> plugin.getPotions().open(player, pagination.next().getPage())));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
