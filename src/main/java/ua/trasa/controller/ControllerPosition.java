package ua.trasa.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import ua.trasa.javaclass.geo.DMStoDDConverter;
import ua.trasa.javaclass.geo.OgzWGS84Calculator;
import ua.trasa.javaclass.servisClass.AlertAndInform;

import java.net.URL;
import java.util.ResourceBundle;

import static ua.trasa.controller.Controller.*;
import static ua.trasa.javaclass.geo.DMStoDDConverter.*;

public class ControllerPosition implements Initializable {
    static AlertAndInform inform = new AlertAndInform();
    DMStoDDConverter dmsToDDConverter = new DMStoDDConverter();
    OgzWGS84Calculator ogz = new OgzWGS84Calculator();



    public static double distanceMS, angleMS;

    @FXML
    public TableView positionTable;
    public TextField statusBar, labelLineCount;
    public TextField mLat, mLon, mAlt;
    public TextField sLat, sLon, sAlt;
    public TextField launcherLat, launcherLon, launcherAlt;
    public Button tDMStoDD;
    public Label lDistMS, lAngMS;

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

        getDistAndAng ();
        lDistMS.setText(String.valueOf(distanceMS));
        lAngMS.setText(String.valueOf(angleMS));

    }

    public void getDistAndAng (){
//        dmsToDDConverter.DMStoDD(radarLat,radarLon,radarAlt);
//        dmsToDDConverter.DMStoDD(slaveLat,slaveLon,slaveAlt);

        ogz.ogz84(radarLat,radarLon,radarAlt,slaveLat,slaveLon,slaveAlt);
        distanceMS=ogz.distance;
        angleMS=ogz.ang;

    }

    public void DMStoDD(){
        dmsToDDConverter.DMStoDD(radarLat,radarLon,radarAlt);
        mLat.setText(String.valueOf(latitudeDD));
        mLon.setText(String.valueOf(longitudeDD));
        mAlt.setText(String.valueOf(altitudeDD));

        dmsToDDConverter.DMStoDD(slaveLat,slaveLon,slaveAlt);
        sLat.setText(String.valueOf(latitudeDD));
        sLon.setText(String.valueOf(longitudeDD));
        sAlt.setText(String.valueOf(altitudeDD));

        dmsToDDConverter.DMStoDD(launchLat,launchLon,launchAlt);
        launcherLat.setText(String.valueOf(latitudeDD));
        launcherLon.setText(String.valueOf(longitudeDD));
        launcherAlt.setText(String.valueOf(altitudeDD));
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
