
package gamelogger.database;

import gamelogger.GameBean;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author hawdi
 */
public class SQLquerries {

    private final String path = System.getProperty("user.home") + "/.GameLogger/";
    private final String url = "jdbc:sqlite:/" + path + "logs.db";

    public SQLquerries() {
        //create the required tables and database file for the first time.
        createNewDatabase();
        //create required tables
        createRequiredTables();
    }
    
    /**
     * Create connection to the database
     * @return A Connection
     */
    private Connection connect(){
        try{
            Connection conn = DriverManager.getConnection(url);
            return conn;
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Create New Database file in URL location
     */
    private void createNewDatabase() {
        createDirs();
        try {
            Connection conn = this.connect();
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("a new db has been created!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            System.out.println("Can't create the Database");
        }
    }

    /**
     * Create required directories for the database location
     */
    private void createDirs() {
        File directory = new File(path);
        directory.mkdirs();
    }

    /**
     * Create required tables for the created database. <br>
     * Tables:<br>
     *      <b>gamelist</b>: Store saved and inserted games from the user.<br>
     *      <b>logs</b>: Store logs about game plays, time, date, duration, which game, etc.<br>
     *      <b>user</b>: Store users and some important configuration about them.<br>
     */
    private void createRequiredTables() {
        //create table 'gamelist' to store list of games
        String sql = "CREATE TABLE IF NOT EXISTS gamelist (gameid integer PRIMARY KEY AUTOINCREMENT, name text not null);";
        try {
            Connection conn = this.connect();
            Statement st = conn.createStatement();
            st.execute(sql); //create table gamelist
            //create table logs to store gameplay logs
            //id is a foreign key and references to the gameid in gamelist table.
            sql = "CREATE TABLE IF NOT EXISTS logs ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, duration integer DEFAULT 0, indate TEXT NOT NULL);";
            st.execute(sql); //create table logs
            //create table user to save configurations
            sql = "CREATE TABLE IF NOT EXISTS configs("
                    + "config TEXT PRIMARY KEY, value INTEGER);";
            st.execute(sql);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * 
     * @param name is name of the game.
     * @param duration of the game played. for example 2 hours.
     * @param date is the date of the play time.
     */
    public void insertLog(String name, int duration, String date){
        String sql = "INSERT INTO logs(name, duration, indate) VALUES (?, ?, ?)";
        try{
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, duration);
            ps.setString(3, date);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /**
     * 
     * @param name is the game name. user can save one game in multiple times, because the primary key is gameid and it's auto increment.
     */
    public void insertNewGame(String name){
        String sql = "INSERT INTO gamelist(name) VALUES(?)";
        try{
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    /**
     * 
     * @param conf is the setting field want to insert.
     * @param value is the configuration value for that setting.
     */
    public void insertUserConfig(String conf, int value){
        String sql = "INSERT INTO configs VALUES (?, ?);";
        try{
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, conf);
            ps.setInt(2, value);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
    }
    
    public void deleteLog(int id){
        String sql = "DELETE FROM logs WHERE id = ?";
        try{
            Connection conn = this.connect();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    public void deleteGame(){
        
    }
    
    /**
     * 
     * @return ArrayList<Integer> for all game IDs in the gamelist table.
     */
    public ArrayList<Integer> selectGameId(){
        ArrayList<Integer> gameIDs = new ArrayList<>();
        String sql = "SELECT gameid FROM gamelist;";
        try{
            Connection conn = this.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                gameIDs.add(rs.getInt("gameid"));
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        return gameIDs;
    }
    
    /**
     * 
     * @return returns all game names in the database.
     */
    public ObservableList<String> selectGameNames(){
        ObservableList<String> games = FXCollections.observableArrayList();
        String sql = "SELECT name FROM gamelist;";
        try{
            Connection conn = this.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                games.add(rs.getString("name"));
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
        return games;
    }
    /**
     * Getting config and its value from the configs table
     * @return Map<String ConfigName, Integer value>, String config name, integer value for config.
     */
    public Map<String, Integer> getUserConfig(){
        Map<String, Integer> userConfigs = new HashMap<>();
        String sql = "SELECT * FROM configs";
        try{
            Connection conn = this.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
                userConfigs.put(rs.getString("config"), rs.getInt("value"));
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return userConfigs;
    }
    /**
     * 
     * @return An Observable contains all data from Logs table
     */
    public ObservableList<GameBean> selectLogs(){
        ObservableList<GameBean> logs = FXCollections.observableArrayList();
        String sql = "SELECT * FROM logs";
        try{
            Connection conn = this.connect();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                logs.add(new GameBean(rs.getInt("id"), rs.getString("name"), rs.getInt("duration"), rs.getString("indate")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLquerries.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        return logs;
    }
}
