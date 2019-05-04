/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogger;


import gamelogger.database.SQLquerries;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author hawdi
 */
public class GameLogger extends Application {

    private boolean firstTime;

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = null;
        //We check in configuration table in the database is set to 1, it means it's the first runtime of the app here. otherwise we call the default application window
        SQLquerries querry = new SQLquerries();
        Map<String, Integer> conf = querry.getUserConfig();
        //check if the program is first time running
        if (conf.isEmpty() || conf.get("firstInteraction") == 1) {

            try {
                root = FXMLLoader.load(getClass().getResource("FirstInteractionLayout.fxml"));
            } catch (IOException ex) {
                Logger.getLogger(GameLogger.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
            }
            querry.insertUserConfig("firstInteraction", 0);
            Scene scene = new Scene(root);

            primaryStage.setTitle("Game Logger");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
//        primaryStage.setMinWidth(400);
//        primaryStage.setMinHeight(300);
            primaryStage.show();
        }
        //if the program is not in first run
        else{
            try{
                root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("mainwindowcss.css").toExternalForm());
                primaryStage.setTitle("Game Logger");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (IOException ex) {
                Logger.getLogger(GameLogger.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
