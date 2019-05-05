/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogger;

import gamelogger.database.SQLquerries;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
    private TableColumn<GameBean, String> recentDurationCol;

    @FXML
    private TableColumn<GameBean, String> recentDateCol;
    @FXML
    private BarChart<?, ?> summaryChart;
    private MenuItem deleteItem = new MenuItem("Delete");

    
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
                totalGameTimeLabelInit();
            });
            
            //refresh the total duration gameplay label
            totalGameTimeLabelInit();
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
        tableInit(); //initialize the components
        recentTableViewInit();//initialize the data
        menuItemsInit();//initialize menu items
        totalGameTimeLabelInit();
    }
    
    private void totalGameTimeLabelInit(){
        int totalDuration = new SQLquerries().selectTotalGamesDuration(LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy")));
        totalGameplayLabel.setText(formattedDuration(totalDuration));
        
    }
    
    /**
     * 
     * @param duration to formatting it to int hours and int minutes
     * @return Formatted text
     */
    private String formattedDuration(int duration){
        String formattedDuration;
        if(duration <= 0)
            formattedDuration = "0";
        else if(duration < 60)
            formattedDuration = duration + " minutes";
        else{
            int result = duration / 60;
            int remains = duration % 60;
            if(remains != 0)
                formattedDuration = result + " hour(s) & " + remains + " minutes";
            else
                formattedDuration = result + " hour(s)";
        }
        return formattedDuration;
    }
    
    /**
     * Initialize the data form database
     */
    private void recentTableViewInit(){
        SQLquerries querry = new SQLquerries();
        ObservableList<GameBean> logs = querry.selectLogs();
        recentTableView.setItems(logs);
    }
    /**
     * Initialize the table components, context menu, column value factory.
     */
    private void tableInit(){
        //set value factory to the table columns
        recentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        recentDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        recentDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        //add cutom font
        recentTableView.setId("recentTable");
        
        //initialize the formatted value for the duratoin column
        recentDurationCol.setComparator((String o1, String o2) -> {
            Integer int1 = GameBean.formattedValues.get(o1);
            Integer int2 = GameBean.formattedValues.get(o2);
            return int1.compareTo(int2);
        });
        
        //add context menu to the recent table
        recentTableView.setContextMenu(new ContextMenu(deleteItem));
    }
    /**
     * Initialize the actions to the menu items.
     */
    private void menuItemsInit(){
        deleteItem.setOnAction(event -> {
            SQLquerries query = new SQLquerries();
            GameBean selectedItem = recentTableView.getSelectionModel().getSelectedItem();
            query.deleteLog(selectedItem.getId());
            recentTableView.getItems().remove(selectedItem);
           
            //refresh the total duration gameplay label
            totalGameTimeLabelInit();
        });
    }
    
}
