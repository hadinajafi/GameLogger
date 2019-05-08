/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogger;

import gamelogger.database.SQLquerries;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hawdi
 */
public class FirstInteractionLayoutController implements Initializable {

    //UI components and variables
    @FXML
    private ListView<String> gameList;
    @FXML
    private TextField gameNameTxt;
    @FXML
    private Button addGameButton;
    @FXML
    private Button exitButton;
    @FXML
    private TitledPane recentPane;
    @FXML
    private Button continueButton;
    
    private ObservableList<String> names = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        continueButton.setDisable(true); //disable the continue button at startup
        MenuItem delete = new MenuItem("Delete");   //create context menu item for list context menu
        delete.setOnAction((ActionEvent event) -> { //action will give the selected item and removes it
            try{
                names.remove(gameList.getSelectionModel().getSelectedIndex());
            }catch(ArrayIndexOutOfBoundsException e){
                System.err.println("nothing for delete");   //if nothing selected for delete
            }
            gameList.setItems(names);   //refresh the list for showing remaining items
        });
        //Add key event handler, to add games from text field by pressing ENTER. fast and easy
        gameNameTxt.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER){
                names.add(gameNameTxt.getText());
                gameList.setItems(names);
                gameNameTxt.setText(null);
            }
        });
        //add right click menu to the list view
        gameList.setContextMenu(new ContextMenu(delete));
        //add change listener to the 'name' list for enabling contniue button when user adds a game to the list.
        //otherwise if list is empty, the continue button is set to disable again
        names.addListener((ListChangeListener.Change<? extends String> c) -> {
            if(names.isEmpty()){
                continueButton.setDisable(true); //disable continue button because of list is empty
            }
                else{
                continueButton.setDisable(false);   //enable continue button when list is not empty
            }
        });
    }    
    
    @FXML
    void addGameButtonActionPerformed(ActionEvent event) {
        names.add(gameNameTxt.getText());
        gameList.setItems(names);
        gameNameTxt.setText(null); //emptying the textfield when new game added
    }

    @FXML
    void continueButtonActionPerformed(ActionEvent event) {
        //insert entered games to the list
        SQLquerries querry = new SQLquerries();
        //insert each gameList item in the database
        gameList.getItems().forEach((item) -> {
            querry.insertNewGame(item);
        });
        
        AnchorPane mainWindow;
        try{
            mainWindow = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            Scene scene = new Scene(mainWindow);
            Stage mwstage = new Stage();
            mwstage.setTitle("Game Logger");
            mwstage.setMinWidth(500);
            mwstage.setMinHeight(360);
            mwstage.setScene(scene);
            mwstage.show(); //showing main window

            //closing firstInteraction window
            Stage current = (Stage) continueButton.getScene().getWindow();
            current.close();
        }catch(IOException e){
            e.getMessage();
        }
    }

    @FXML
    void exitButtonActionPerformed(ActionEvent event) {
        System.exit(0);
    }
    
}
