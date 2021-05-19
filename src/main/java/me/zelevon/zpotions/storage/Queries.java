package me.zelevon.zpotions.storage;

import me.zelevon.zpotions.ZPotions;

import java.sql.*;
import java.util.UUID;

public class Queries {

    private static DatabaseManager dbManager;

    private static String TABLE = "player_potions";

    private static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "(" +
            "player_uuid VARCHAR(36) NOT NULL PRIMARY KEY," +
            "potion_slot_one INT," +
            "potion_slot_two INT," +
            "potion_slot_three INT);";

    private static String GET_PLAYER = "SELECT * FROM " + TABLE + " WHERE player_uuid = ?;";

    private static String SAVE_PLAYER = "REPLACE INTO " + TABLE + " (player_uuid, potion_slot_one, potion_slot_two, potion_slot_three) VALUES(?,?,?,?);";

    public static void createTable() {
        dbManager = ZPotions.getInstance().getDbManager();
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(CREATE_TABLE)) {
            stmt.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int[] getPlayer(UUID uuid) {
        int[] result = {0, 0, 0};
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(GET_PLAYER)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                result[0] = rs.getInt(2);
                result[1] = rs.getInt(3);
                result[2] = rs.getInt(4);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void savePlayer(UUID uuid, int potionSlotOne, int potionSlotTwo, int potionSlotThree) {
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(SAVE_PLAYER)) {
            stmt.setString(1, uuid.toString());
            stmt.setInt(2, potionSlotOne);
            stmt.setInt(3, potionSlotTwo);
            stmt.setInt(4, potionSlotThree);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
