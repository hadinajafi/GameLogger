/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogger;

import gamelogger.database.SQLquerries;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author hadi
 */
public class SettingsLayoutController implements Initializable {

    //components & attributes
    @FXML
    private ListView<String> gameListView;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button addNewBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button renameBtn;
    //list view observable list
    private ObservableList<String> gameList;
    private ObservableList<GameBean> gameBeans;

    //buttons actions
    @FXML
    void addNewBtnOnAction(ActionEvent event) {
        SQLquerries query = new SQLquerries();
        query.insertNewGame(nameTextField.getText());
        gameList.add(nameTextField.getText());
    }

    @FXML
    void deleteBtnOnAction(ActionEvent event) {

        if (gameListView.getSelectionModel().getSelectedItem() != null) {
            SQLquerries query = new SQLquerries();
            gameBeans = query.selectGame();
            int selectedIndex = gameListView.getSelectionModel().getSelectedIndex();
            query.deleteGame(gameBeans.get(selectedIndex).getId(), gameBeans.get(selectedIndex).getName());
            gameBeans.remove(selectedIndex);
            gameList.remove(selectedIndex);
        }

    }

    @FXML
    void renameBtnAction(ActionEvent event) {

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewInit();
    }

    /**
     * Initialize the list view items and load game names from the database
     */
    private void listViewInit() {
        SQLquerries query = new SQLquerries();
        gameList = query.selectGameNames();
        gameListView.setItems(gameList);
        //add listener
        gameList.addListener((ListChangeListener.Change<? extends String> c) -> {
            gameListView.setItems(query.selectGameNames());
        });
    }

}
