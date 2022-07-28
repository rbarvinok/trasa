package ua.trasa.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.rint;


public class SettingsController implements Initializable {

    public static String placeView;
    public static double opacity;

    @FXML
    public Button bLeftUp, bLeftDown, bRightUp, bRightDown;
    public Button okButton;
    public TextField tOpacity;
    public Slider slider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider.setValue(opacity);
        tOpacity.setText(String.valueOf(opacity));
    }

    public void changeOpacity() {
        slider.valueProperty().addListener(((observable, oldValue, newValue) ->
                tOpacity.setText(String.valueOf(rint((Double) newValue)))));
    }

    @SneakyThrows
    public void actionButtonPressed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();

        // если нажата не кнопка - выходим из метода
        if (!(source instanceof Button)) {
            return;
        }

        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "bLeftUp":
                placeView = "leftUp";
                break;
            case "bLeftDown":
                placeView = "leftDown";
                break;
            case "bRightUp":
                placeView = "rightUp";
                break;
            case "bRightDown":
                placeView = "rightDown";
                break;
            case "okButton":
                opacity = Double.parseDouble(tOpacity.getText());
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("settings.txt", false), "Cp1251");
                osw.write("PlaceView=" + placeView);
                osw.write("\n");
                osw.write("Opacity=" + opacity);
                osw.close();

                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
                break;
        }

    }


}
