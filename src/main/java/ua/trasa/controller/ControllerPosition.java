package ua.trasa.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import ua.trasa.javaclass.geo.DDtoDMSConverter;
import ua.trasa.javaclass.geo.DMStoDDConverter;
import ua.trasa.javaclass.geo.OgzWGS84Calculator;
import ua.trasa.javaclass.servisClass.AlertAndInform;

import java.net.URL;
import java.util.ResourceBundle;

import static ua.trasa.controller.Controller.*;
import static ua.trasa.javaclass.geo.DDtoDMSConverter.*;
import static ua.trasa.javaclass.geo.DMStoDDConverter.*;

public class ControllerPosition implements Initializable {
    static AlertAndInform inform = new AlertAndInform();
    DMStoDDConverter dmsToDDConverter = new DMStoDDConverter();
    DDtoDMSConverter dDtoDMSConverter = new DDtoDMSConverter();
    OgzWGS84Calculator ogz = new OgzWGS84Calculator();

    public static double distanceMS, angleMS;

    @FXML
    public TextField statusBar, labelLineCount;
    public TextField mLat, mLon, mAlt;
    public TextField sLat, sLon, sAlt;
    public TextField launcherLat, launcherLon, launcherAlt;
    public TextField lDistM, lAngM;
    public TextField lDistS, lAngS;
    public TextField lDistL, lAngL;
    public Button tEdit, tSave, tDMSorDD;
    public Button tMaster, tSlave;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
        onClickMaster();
    }

    public void update() {
        if (radarLat.substring(0, 1).equals("+")) {
            DMStoDD();
        }
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

    public void DMStoDD() {
        dmsToDDConverter.DMStoDD(radarLat, radarLon, radarAlt);
        radarLat = String.valueOf(latitudeDD);
        radarLon = String.valueOf(longitudeDD);
        radarAlt = String.valueOf(altitudeDD);

        dmsToDDConverter.DMStoDD(slaveLat, slaveLon, slaveAlt);
        slaveLat = String.valueOf(latitudeDD);
        slaveLon = String.valueOf(longitudeDD);
        slaveAlt = String.valueOf(altitudeDD);

        dmsToDDConverter.DMStoDD(launchLat, launchLon, launchAlt);
        launchLat = String.valueOf(latitudeDD);
        launchLon = String.valueOf(longitudeDD);
        launchAlt = String.valueOf(altitudeDD);
    }

    public void DDtoDMS() {
        dDtoDMSConverter.DDtoDMS(radarLat, radarLon, radarAlt);
        radarLat = "+0"+latD+"°"+latM+"'"+latS+"''";
        radarLon = "+0"+longD+"°"+longM+"'"+longS+"''";
        radarAlt = String.valueOf(altDMS);

        dDtoDMSConverter.DDtoDMS(slaveLat, slaveLon, slaveAlt);
        slaveLat = "+0"+latD+"°"+latM+"'"+latS+"''";
        slaveLon = "+0"+longD+"°"+longM+"'"+longS+"''";
        slaveAlt = String.valueOf(altDMS);

        dDtoDMSConverter.DDtoDMS(launchLat, launchLon, launchAlt);
        launchLat = "+0"+latD+"°"+latM+"'"+latS+"''";
        launchLon = "+0"+longD+"°"+longM+"'"+longS+"''";
        launchAlt = String.valueOf(altDMS);

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

    public void ChoiceDDorDMS() {
        if (tDMSorDD.getText().equals("Градуси, мінути, секунди")) {
            tDMSorDD.setText("Градуси");
            DDtoDMS();
        } else if (tDMSorDD.getText().equals("Градуси")) {
            tDMSorDD.setText("Градуси, мінути, секунди");
            DMStoDD();
        }
    }

    public void onClickMaster() {
        tMaster.setDisable(true);
        tSlave.setDisable(false);
        if (radarLat.equals(slaveLat) && radarLon.equals(slaveLon)) {
            distanceMS = 0.000;
            angleMS = 0.00;
        } else {
            ogz.ogz84(radarLat, radarLon, radarAlt, slaveLat, slaveLon, slaveAlt);
            distanceMS = ogz.distance;
            angleMS = ogz.ang;
        }
        lDistM.setText("0.000");
        lAngM.setText("0.00");
        lDistS.setText(String.valueOf(distanceMS));
        lAngS.setText(String.valueOf(angleMS));

        ogz.ogz84(radarLat, radarLon, radarAlt, launchLat, launchLon, launchAlt);
        lDistL.setText(String.valueOf(ogz.distance));
        lAngL.setText(String.valueOf(ogz.ang));

        statusBar.setText("MASTER");
    }

    public void onClickSlave() {
        tMaster.setDisable(false);
        tSlave.setDisable(true);
        ogz.ogz84(slaveLat, slaveLon, slaveAlt, radarLat, radarLon, radarAlt);
        distanceMS = ogz.distance;
        angleMS = ogz.ang;

        lDistM.setText(String.valueOf(ogz.distance));
        lAngM.setText(String.valueOf(ogz.ang));
        lDistS.setText("0.000");
        lAngS.setText("0.00");

        ogz.ogz84(slaveLat, slaveLon, slaveAlt, launchLat, launchLon, launchAlt);
        lDistL.setText(String.valueOf(ogz.distance));
        lAngL.setText(String.valueOf(ogz.ang));

        statusBar.setText("SLAVE");
    }

    public void onClickEdit() {
        if (tEdit.getText().equals("Редагувати")) {
            tSave.setVisible(true);
            tEdit.setText("Відхилити");
            Image image = new Image(getClass().getResourceAsStream("/images/delete1.png"));
            tEdit.setGraphic(new ImageView(image));
            mLat.setEditable(true);
            mLon.setEditable(true);
            mAlt.setEditable(true);
            sLat.setEditable(true);
            sLon.setEditable(true);
            sAlt.setEditable(true);
            launcherLat.setEditable(true);
            launcherLon.setEditable(true);
            launcherAlt.setEditable(true);
        } else {
            onClickCancel();
            update();
        }
    }

    public void onClickApply() {
        radarLat = mLat.getText();
        radarLon = mLon.getText();
        radarAlt = mAlt.getText();
        slaveLat = sLat.getText();
        slaveLon = sLon.getText();
        slaveAlt = sAlt.getText();
        launchLat = launcherLat.getText();
        launchLon = launcherLon.getText();
        launchAlt = launcherAlt.getText();
        update();
        onClickMaster();
        onClickCancel();
    }

    public void onClickCancel() {
        tSave.setVisible(false);
        tEdit.setText("Редагувати");
        Image image = new Image(getClass().getResourceAsStream("/images/edit1.png"));
        tEdit.setGraphic(new ImageView(image));
        mLat.setEditable(false);
        mLon.setEditable(false);
        mAlt.setEditable(false);
        sLat.setEditable(false);
        sLon.setEditable(false);
        sAlt.setEditable(false);
        launcherLat.setEditable(false);
        launcherLon.setEditable(false);
        launcherAlt.setEditable(false);
    }
}
