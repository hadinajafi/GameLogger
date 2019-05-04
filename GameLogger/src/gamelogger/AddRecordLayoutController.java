/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogger;

import gamelogger.database.SQLquerries;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hawdi
 */
public class AddRecordLayoutController implements Initializable {

    @FXML
    private ComboBox<String> nameField;

    @FXML
    private Spinner<Integer> durationSpinner;

    @FXML
    private DatePicker datePicker;
    @FXML
    private Button cancelBtn;

    @FXML
    private Button addBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //initialize spinner
        spinnerInit();
        datePickerInit();
        gameComboInit();
    }

    private void gameComboInit() {
        SQLquerries querry = new SQLquerries();
        nameField.setItems(querry.selectGameNames());
    }

    private void datePickerInit() {
        datePicker.setValue(LocalDate.now());
    }

    private void spinnerInit() {
        if ("".equals(durationSpinner.getEditor().getText()) || durationSpinner.getEditor().getText() == null) {
            durationSpinner.getEditor().setText("0");
        }

        //restrict invalid input from user
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([0-9])*")) {
                return change;
            }
            return null;
        };
        durationSpinner.getEditor().setTextFormatter(new TextFormatter<>(filter));//set the reg match on the spinner

        //define decrement and increment actions
        durationSpinner.setValueFactory(new SpinnerValueFactory<Integer>() {
            @Override
            public void decrement(int steps) {
                int value = Integer.parseInt(durationSpinner.getEditor().getText());
                if (value > 0) {
                    value--;
                    durationSpinner.getEditor().setText(String.valueOf(value));
                }
            }

            @Override
            public void increment(int steps) {
                int value = Integer.parseInt(durationSpinner.getEditor().getText());
                value++;
                durationSpinner.getEditor().setText(String.valueOf(value));
            }
        });
    }

    @FXML
    void addRecordAction(ActionEvent event) {
        if (!nameField.getSelectionModel().isEmpty()) {
            String name = nameField.getSelectionModel().getSelectedItem();
            int duration = Integer.parseInt(durationSpinner.getEditor().getText());
            String date = datePicker.getEditor().getText();
            SQLquerries querry = new SQLquerries();
            querry.insertLog(name, duration, date);

            cancelBtnAction(event);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select a game to confirm!", ButtonType.OK);
            alert.show();
            
        }
    }

    @FXML
    void cancelBtnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

}
