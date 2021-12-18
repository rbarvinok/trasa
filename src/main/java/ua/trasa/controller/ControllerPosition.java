package ua.trasa.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import ua.trasa.javaclass.domain.NMEA;
import ua.trasa.javaclass.servisClass.AlertAndInform;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static ua.trasa.controller.Controller.*;

public class ControllerPosition implements Initializable {
    static AlertAndInform inform = new AlertAndInform();

    public static List<NMEA> resultsNMEA = new ArrayList<>();

    @FXML
    public TableView positionTable;
    public TextField statusBar, labelLineCount;
    public TextField mLat, mLon, mAlt;
    public TextField sLat, sLon, sAlt;
    public TextField launcherLat, launcherLon, launcherAlt;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mLat.setText(radarLat);
        mLon.setText(radarLon);
        mAlt.setText(radarAlt);

        sLat.setText(slaveLat);
        sLon.setText(slaveLon);
        sAlt.setText(slaveAlt);

        launcherLat.setText(launchLat);
        launcherLon.setText(launchLon);
        launcherAlt.setText(launchAlt);
    }

    public void onClickNew(ActionEvent e) {
        positionTable.getColumns().clear();
        positionTable.getItems().clear();
        statusBar.setText("");
        labelLineCount.setText("");
    }

    public void onClickSave(ActionEvent e) {
    }

    public void onClickSetup(ActionEvent e) {
    }

    public void onClickCancelBtn(ActionEvent e) {
    }
}
