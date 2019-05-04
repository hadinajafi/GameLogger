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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author hawdi
 */
public class MainWindowController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private AnchorPane root;

    @FXML
    private Button addRecordButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Label totalGameplayLabel;
    @FXML
    private TableView<GameBean> recentTableView;

    @FXML
    private TableColumn<GameBean, String> recentNameCol;

    @FXML
    private TableColumn<GameBean, Integer> recentDurationCol;

    @FXML
    private TableColumn<GameBean, String> recentDateCol;

    
    @FXML
    void addRecordBtnActionPerformed(ActionEvent event) {
        AnchorPane pane;
        try{
            pane = FXMLLoader.load(getClass().getResource("AddRecordLayout.fxml"));
            Scene scene = new Scene(pane);
            Stage addRecord = new Stage();
            addRecord.setScene(scene);
            addRecord.setTitle("Add New Record");
            addRecord.setResizable(false);
            addRecord.show();
            addRecord.setOnHidden((WindowEvent closEvent) -> {
                recentTableView.setItems(new SQLquerries().selectLogs());
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    void settingBtnActionPerformed(ActionEvent event) {

    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabPane.tabMinWidthProperty().bind(root.widthProperty().divide(tabPane.getTabs().size()).subtract(23));
        Font.loadFont(getClass().getResource("/gamelogger/fonts/Ubuntu-R.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/gamelogger/fonts/Ubuntu-B.ttf").toExternalForm(), 12);
        tableInit();
        recentTableViewInit();
    }
    
    private void recentTableViewInit(){
        SQLquerries querry = new SQLquerries();
        ObservableList<GameBean> logs = querry.selectLogs();
        recentTableView.setItems(logs);
    }
    
    private void tableInit(){
        recentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        recentDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        recentDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        recentTableView.setId("recentTable");
    }
    
}
