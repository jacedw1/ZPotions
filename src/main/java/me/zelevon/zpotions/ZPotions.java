package me.zelevon.zpotions;

import co.aikar.commands.PaperCommandManager;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import me.zelevon.zpotions.commands.PotionGUICommand;
import me.zelevon.zpotions.config.Config;
import me.zelevon.zpotions.gui.EffectsGUI;
import me.zelevon.zpotions.gui.PotionsGUI;
import me.zelevon.zpotions.listeners.PlayerConnectionListener;
import me.zelevon.zpotions.storage.DatabaseManager;
import me.zelevon.zpotions.storage.Queries;
import me.zelevon.zpotions.storage.cache.PlayerCache;
import me.zelevon.zpotions.utils.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

public final class ZPotions extends JavaPlugin {

    private static ZPotions instance;
    private Config.Settings conf;
    private SmartInventory effects;
    private SmartInventory potions;
    private Path directory;
    private DatabaseManager dbManager;

    private Permission allEffects;
    private Permission threeSlots;
    private Permission twoSlots;
    private Permission oneSlot;
    private Permission allPerms;

    @Override
    public void onEnable() {
        instance = this;

        this.directory = Paths.get("plugins" + File.separator + "Z-Potions");
        if (!directory.toFile().exists()) {
            directory.toFile().mkdir();
        }

        this.dbManager = new DatabaseManager("ZPotions.db");
        Queries.createTable();

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new PotionGUICommand());

        Bukkit.getPluginManager().registerEvents(new PlayerConnectionListener(), this);

        this.conf = Config.setup();


        InventoryManager invManager = new InventoryManager(this);
        invManager.init();
        this.effects = SmartInventory.builder()
                .id("effectsInv")
                .title(MessageHandler.colorize("&b&lEffects"))
                .manager(invManager)
                .provider(new EffectsGUI())
                .size(3, 9)
                .closeable(true)
                .build();
        this.potions = SmartInventory.builder()
                .id("potionInv")
                .title(MessageHandler.colorize("&b&lPotions"))
                .manager(invManager)
                .provider(new PotionsGUI())
                .size(3, 9)
                .closeable(true)
                .build();

        this.allPerms = new Permission("zp.*", PermissionDefault.OP);
        this.allEffects = new Permission("zp.effects.*", PermissionDefault.OP);
        this.threeSlots = new Permission("zp.slots.3", PermissionDefault.OP);
        this.twoSlots = new Permission("zp.slots.2", PermissionDefault.OP);
        this.oneSlot = new Permission("zp.slots.1", PermissionDefault.TRUE);
        this.allEffects.addParent(allPerms, true);
        this.threeSlots.addParent(allPerms, true);
        this.twoSlots.addParent(threeSlots, true);
        this.oneSlot.addParent(twoSlots, true);

        addPermissions(allPerms, allEffects, threeSlots, twoSlots, oneSlot);
    }

    @Override
    public void onDisable() {
        PlayerCache.saveAll();
        try {
            dbManager.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ZPotions getInstance() {
        return instance;
    }

    public Config.Settings getConf() {
        return conf;
    }

    public SmartInventory getEffects() {
        return effects;
    }

    public SmartInventory getPotions() {
        return potions;
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    public Path getDirectory() {
        return directory;
    }

    private void addPermissions(Permission... permissions) {
        for(Permission perm : permissions) {
            Bukkit.getPluginManager().addPermission(perm);
        }
    }

    public Permission getAllEffects() {
        return allEffects;
    }

    public Permission getThreeSlots() {
        return threeSlots;
    }

    public Permission getTwoSlots() {
        return twoSlots;
    }

    public Permission getOneSlot() {
        return oneSlot;
    }

    public Permission getAllPerms() {
        return allPerms;
    }
}
