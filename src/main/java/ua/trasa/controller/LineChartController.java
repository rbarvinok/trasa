package ua.trasa.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import ua.trasa.javaclass.domain.InputDataMaster;
import ua.trasa.javaclass.servisClass.OpenStage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.trasa.controller.Controller.*;

public class LineChartController implements Initializable {
    OpenStage os = new OpenStage();
    ControllerPosition controllerPosition = new ControllerPosition();
    public static ObservableList<XYChart.Data> gps;
    public static ObservableList<XYChart.Data> alt;

    @FXML
    public LineChart lineChart, lineChartAlt;
    public Button scatterChartButton, velocityChartButton;
    public  TextArea outputText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (radarLat.substring(0, 1).equals("+"))
            controllerPosition.DMStoDD();

        NumberAxis x = new NumberAxis();
        x.setAutoRanging(false);
        x.setForceZeroInRange(false);

        NumberAxis y = new NumberAxis();
        y.setAutoRanging(false);
        y.setForceZeroInRange(false);


        XYChart.Series seriesMaster = new XYChart.Series();
        seriesMaster.setName("Координати MASTER");
        ObservableList<XYChart.Data> masterList = FXCollections.observableArrayList();
        masterList.add(new XYChart.Data(Double.parseDouble(radarLon), Double.parseDouble(radarLat)));
        seriesMaster.setData(masterList);

        XYChart.Series seriesSlave = new XYChart.Series();
        seriesSlave.setName("Координати SLAVE");
        ObservableList<XYChart.Data> slaveList = FXCollections.observableArrayList();
        slaveList.add(new XYChart.Data(Double.parseDouble(slaveLon), Double.parseDouble(slaveLat)));
        seriesSlave.setData(slaveList);

        XYChart.Series seriesLauncher = new XYChart.Series();
        seriesLauncher.setName("Координати Launcher");
        ObservableList<XYChart.Data> launchList = FXCollections.observableArrayList();
        launchList.add(new XYChart.Data(Double.parseDouble(launchLon), Double.parseDouble(launchLat)));
        seriesLauncher.setData(launchList);

        XYChart.Series seriesGPS = new XYChart.Series();
        seriesGPS.setName("Траєкторія польоту");
        getGPSData();
        seriesGPS.setData(gps);

        lineChart.getData().addAll(seriesGPS, seriesMaster,seriesSlave, seriesLauncher);

        XYChart.Series seriesAlt = new XYChart.Series();
        getAltData();
        seriesAlt.setData(alt);
        lineChartAlt.getData().addAll(seriesAlt);
//
//        consolidatedData(outputText);
    }

    public static void getGPSData() {
        List<InputDataMaster.DD> coordinates = inputDataMasters.stream().map(gpsTimes -> {
            return new InputDataMaster.DD(gpsTimes.getLatitude(), gpsTimes.getLongitude());
        }).collect(Collectors.toList());

        gps = FXCollections.observableArrayList();
        for (InputDataMaster.DD dd : coordinates) {
            gps.add(new XYChart.Data(dd.getLongitude(), dd.getLatitude()));
        }
    }

    public static void getAltData() {
        List<InputDataMaster.Altitude> gpsAlt = Controller.inputDataMasters.stream().map(gpsTimes -> {
            return new InputDataMaster.Altitude(gpsTimes.getTime(), gpsTimes.getAltitude());
        }).collect(Collectors.toList());

        alt = FXCollections.observableArrayList();
        for (InputDataMaster.Altitude altitude : gpsAlt) {
            alt.add(new XYChart.Data(altitude.getTime(), altitude.getAltitude()));
        }
    }
//
//    public static void consolidatedData(TextArea text){
//        text.setText(new StringBuilder()
//                .append("Початок: \n")
//                .append("Час:  " + gpsTimes.get(0).getLocalTime())
//                .append("\n")
//                .append("Широта:  " + gpsTimes.get(0).getLatitude())
//                .append("\n")
//                .append("Довгота:  " + gpsTimes.get(0).getLongitude())
//                .append("\n")
//                .append("Висота:  " + gpsTimes.get(0).getAltitude() + " м")
//                .append("\n")
//                .append("Швидкість:  " + gpsTimes.get(0).getSpeed() + " м/с")
//                .append("\n\n")
//                .append("Кінець: \n")
//                .append("Час:  " + gpsTimes.get(gpsTimes.size()-1).getLocalTime())
//                .append("\n")
//                .append("Широта:  " + gpsTimes.get(gpsTimes.size()-1).getLatitude())
//                .append("\n")
//                .append("Довгота:  " + gpsTimes.get(gpsTimes.size()-1).getLongitude())
//                .append("\n")
//                .append("Висота:  " + gpsTimes.get(gpsTimes.size()-1).getAltitude() + " м")
//                .append("\n")
//                .append("Швидкість:  " + gpsTimes.get(gpsTimes.size()-1).getSpeed() + " м/с")
//                .append("\n\n")
//                .append("Час вимірювання:  " + gpsTimes.get(gpsTimes.size()-1).getTime() + " мс")
//                .append("\n")
//                .append("Всього точок:  " + gpsTimes.size()).toString()
//        );
//
//    }

    @SneakyThrows
    public void onClickScatterChart(ActionEvent actionEvent) {
        os.viewURL = "/view/scatterChart.fxml";
        os.title = "Графік GPS   " + openFile;
        os.maximized = false;
        os.openStage();
        Stage stage = (Stage) scatterChartButton.getScene().getWindow();
        stage.close();
    }

    @SneakyThrows
    public void onClickVelocityChart(ActionEvent actionEvent) {
        os.viewURL = "/view/chartVelocity.fxml";
        os.title = "Графік швидкості - " + openFile;
        os.maximized = false;
        os.openStage();
        Stage stage = (Stage) velocityChartButton.getScene().getWindow();
        stage.close();
    }
}