package ua.trasa.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import ua.trasa.javaclass.servisClass.OpenStage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ua.trasa.controller.Controller.*;

public class ExposeController implements Initializable {
    OpenStage os = new OpenStage();

    public String timerStatus = "stoped";

    @FXML
    public TableView outputTable;
    public Label timerLabel;
    public TextField statusLabel, statusBar;
    public TextField labelLineCount;
    public Button tPosition, tMaster, tSlave, tChart;
    public Button tTimerPlay, tTimerStop;


    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusLabel.setText(status);
        labelLineCount.setText("Строк: " + lineCount);
        statusBar.setText("Файл: " + openFile + "      ID: " + missionID + "     Час супроводження: " + allTime + " сек");
    }

    public void onClickMaster() {
        status = "MASTER";
        statusLabel.setText(status);
    }

    public void onClickSlave() {
        status = "SLAVE";
        statusLabel.setText(status);
    }

    public void timer() {

    }

    public void onClickTimerControls(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        Button clickedButton = (Button) source;
        switch (clickedButton.getId()) {
            case "tTimerPlay":
                if (timerStatus.equals("playing")) {
                    timerStatus = "paused";
                    tTimerPlay.setGraphic(new ImageView("/images/player/pause.png"));
                } else if (timerStatus.equals("paused")||timerStatus.equals("stoped")) {
                    timerStatus = "playing";
                    tTimerPlay.setGraphic(new ImageView("/images/player/play.png"));
                }
                break;
            case "tTimerStop":
                timerStatus = "stoped";
                tTimerPlay.setGraphic(new ImageView("/images/player/play.png"));
                break;
        }
    }

    public void onClickChart() throws IOException {
        os.viewURL = "/view/lineChart.fxml";
        os.title = "Графік   " + openFile;
        os.openStage();
    }

    @SneakyThrows
    public void positionDefinition() {
        os.viewURL = "/view/position.fxml";
        os.title = "Визначення координат об'єктів";
        os.openStage();
    }

    public void onClickSetup(ActionEvent actionEvent) {
    }
}
