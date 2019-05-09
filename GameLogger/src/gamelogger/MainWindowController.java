package gamelogger;

import gamelogger.database.SQLquerries;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
    private BarChart<String, Number> summaryChart;
    @FXML
    private CategoryAxis gameAxis;
    @FXML
    private NumberAxis numAxis;
    private XYChart.Series gamePlay;

    private MenuItem deleteItem = new MenuItem("Delete");

    @FXML
    void addRecordBtnActionPerformed(ActionEvent event) {
        AnchorPane pane;
        try {
            pane = FXMLLoader.load(getClass().getResource("AddRecordLayout.fxml"));
            Scene scene = new Scene(pane);
            Stage addRecord = new Stage();
            addRecord.setScene(scene);
            addRecord.setTitle("Add New Record");
            addRecord.setResizable(false);
            addRecord.show();
            addRecord.setOnHidden((WindowEvent closEvent) -> {
                recentTableView.setItems(new SQLquerries().selectLogs()); //refresh the table
                //refresh data for the total label gameplay and chart
                totalGameTimeLabelInit();
                gamePlay.getData().clear();
                summaryChart.getData().removeAll(gamePlay);
                barChartInit();
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
        try {
            AnchorPane settings = FXMLLoader.load(getClass().getResource("SettingsLayout.fxml"));
            Scene scene = new Scene(settings);
            Stage setting = new Stage();
            setting.setScene(scene);
            setting.setTitle("Settings");
            setting.setResizable(false);
            setting.show();
            setting.setOnHidden((WindowEvent e) -> {
                refreshComponentsData();
            });
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
        }
    }

    /**
     * This Method is called when changes applied to the database and components need update.
     * Table and Chart will update.
     */
    private void refreshComponentsData() {
        recentTableView.setItems(new SQLquerries().selectLogs()); //refresh the table
        //refresh data for the total label gameplay and chart
        totalGameTimeLabelInit();
        gamePlay.getData().clear();
        summaryChart.getData().removeAll(gamePlay);
        barChartInit();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabPane.tabMinWidthProperty().bind(root.widthProperty().divide(tabPane.getTabs().size()).subtract(23));
        Font.loadFont(getClass().getResource("/gamelogger/fonts/Ubuntu-R.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/gamelogger/fonts/Ubuntu-B.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("/gamelogger/fonts/Sahel.ttf").toExternalForm(), 12);
        tableInit(); //initialize the components
        barChartInit(); //initialize the bar chart
        recentTableViewInit();//initialize the data
        menuItemsInit();//initialize menu items
        totalGameTimeLabelInit();
    }

    /**
     * Set total game play today duration to the 'totalDuration' label
     */
    private void totalGameTimeLabelInit() {
        int totalDuration = new SQLquerries().selectTotalGamesDuration(LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy")));
        totalGameplayLabel.setText(_formattedDuration(totalDuration));

    }

    /**
     *
     * @param duration to formatting it to int hours and int minutes. like <u>12
     * minutes</u> or <u>2 hours & 20 minutes</u>.
     * @return (String) Formatted text
     */
    private String _formattedDuration(int duration) {
        String formattedDuration;
        if (duration <= 0) {
            formattedDuration = "0";
        } else if (duration < 60) {
            formattedDuration = duration + " minutes";
        } else {
            int result = duration / 60;
            int remains = duration % 60;
            if (remains != 0) {
                formattedDuration = result + " hour(s) & " + remains + " minutes";
            } else {
                formattedDuration = result + " hour(s)";
            }
        }
        return formattedDuration;
    }

    /**
     * Initialize the data form database
     */
    private void recentTableViewInit() {
        SQLquerries querry = new SQLquerries();
        ObservableList<GameBean> logs = querry.selectLogs();
        recentTableView.setItems(logs);
    }

    /**
     * Initialize the table components, context menu, column value factory.
     */
    private void tableInit() {
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
    private void menuItemsInit() {
        deleteItem.setOnAction(event -> {
            SQLquerries query = new SQLquerries();
            GameBean selectedItem = recentTableView.getSelectionModel().getSelectedItem();  //get selected bean
            query.deleteLog(selectedItem.getId());  //remove selected item from database
            recentTableView.getItems().remove(selectedItem); //remove selected item from table

            //refresh the total duration gameplay label & chart
            totalGameTimeLabelInit();
            gamePlay.getData().clear();
            summaryChart.getData().removeAll(gamePlay);
            barChartInit();
        });
    }

    /**
     * Initialize bar chart in chart tab.
     */
    private void barChartInit() {
        summaryChart.setId("barChart"); //add css id to the chart
        gameAxis.setLabel("Game");
        //gameAxis.setTickLabelRotation(90);
        numAxis.setLabel("Play Time (minutes)");
        gamePlay = new XYChart.Series<>();
        gamePlay.setName("Statistics");

        SQLquerries query = new SQLquerries();
        ObservableList<GameBean> logs = query.selectName_Duration();    //observable list contains all gamebeans with valid name & duration values. !!! id & date is null
        ObservableList<String> names = query.selectGameNames(); //names contains all game names in database
        Map<String, Integer> result = new HashMap<>();

        for (GameBean log : logs) {
            if (result.isEmpty() || !result.containsKey(log.getName())) {
                result.put(log.getName(), log.getIntegerDuration());
            } else {
                int value = result.get(log.getName());
                value += log.getIntegerDuration();
                result.replace(log.getName(), value);
            }
        }

        for (String name : names) {
            if (result.get(name) != null) {
                gamePlay.getData().add(new XYChart.Data<>(name, result.get(name)));
            }
        }
        summaryChart.getData().add(gamePlay);
    }

}
