package ua.trasa.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ua.trasa.javaclass.domain.NMEA;
import ua.trasa.javaclass.servisClass.AlertAndInform;
import ua.trasa.javaclass.servisClass.OpenStage;

import java.util.ArrayList;
import java.util.List;

public class ControllerPosition {
    static AlertAndInform inform = new AlertAndInform();
    OpenStage os = new OpenStage();

    public static List<NMEA> resultsNMEA = new ArrayList<>();

    @FXML
    public TableView positionTable;
    public TextField statusBar;

    public void onClickNew(ActionEvent e) {
    }

    public void onClickSave(ActionEvent e) {
    }

    public void onClickSetup(ActionEvent e) {
    }



    public void onClickCancelBtn(ActionEvent e) {
    }
}
