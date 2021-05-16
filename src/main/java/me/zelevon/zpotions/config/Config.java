package me.zelevon.zpotions.config;

import com.google.common.collect.Lists;
import me.zelevon.zpotions.ZPotions;
import me.zelevon.zpotions.config.base.PotionOption;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;

public class Config {

    private static ZPotions plugin;

    public static Settings setup() {
        plugin = ZPotions.getInstance();
        try {
            Path file = Paths.get(plugin.getDirectory() + File.separator + "potions.json");
            if (file.toFile().exists() || file.toFile().createNewFile()) {
                final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                        .defaultOptions(opts -> opts.shouldCopyDefaults(true))
                        .path(file)
                        .build();
                BasicConfigurationNode node = loader.load();
                Settings conf = node.get(Config.Settings.class);
                loader.save(node);
                return conf;
            }
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Config could not be created/read, plugin disabling.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            e.printStackTrace();
        }
        return null;
    }

    @ConfigSerializable
    public static class Settings {

        @Setting
        private List<PotionOption> availablePotions = Lists.newArrayList(new PotionOption());

        public List<PotionOption> getAvailablePotions() {
            return availablePotions;
        }
    }
}
