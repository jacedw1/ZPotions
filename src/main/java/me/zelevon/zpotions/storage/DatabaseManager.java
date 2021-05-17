package me.zelevon.zpotions.storage;

import me.zelevon.zpotions.ZPotions;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class DatabaseManager {

    private ZPotions plugin;
    private Connection connection;

    public DatabaseManager(String databaseName){
        this.plugin = ZPotions.getInstance();
        String url = "jdbc:sqlite:" + plugin.getDirectory().toAbsolutePath() + File.separator + databaseName;
        try {
            connection = DriverManager.getConnection(url);
        }  catch (SQLException e){
            Bukkit.getLogger().log(Level.SEVERE, "Database Connection to " + databaseName + " Failed.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
