package ua.trasa.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ua.trasa.javaclass.servisClass.OpenStage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ua.trasa.controller.Controller.isCalc;
import static ua.trasa.controller.Controller.openFile;
import static ua.trasa.controller.ChartConverterController.*;

public class ScatterChartConverterController implements Initializable {
    OpenStage os = new OpenStage();
    @FXML
    public ScatterChart scatterChart;
    @FXML
    public Button lineChartButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberAxis x = new NumberAxis();
        x.setAutoRanging(false);
        x.setForceZeroInRange(false);

        NumberAxis y = new NumberAxis();
        y.setAutoRanging(false);
        y.setForceZeroInRange(false);

        ScatterChart<Number, Number> cccGPS = new ScatterChart<Number, Number>(x, y);
        cccGPS.setTitle("Графік GPS " + openFile);
        x.setLabel("Latitude");
        y.setLabel("Longitude");

        if (isCalc == "DMStoDD") {
            XYChart.Series series1 = new XYChart.Series();
            series1.setName("Координати");
            getChartDataDMStoDD();
            series1.setData(gps);
            scatterChart.getData().addAll(series1);
        }

        if (isCalc == "DDtoDMS") {
            XYChart.Series series2 = new XYChart.Series();
            series2.setName("Координати");
            getChartDataDDtoDMS();
            series2.setData(gps);
            scatterChart.getData().addAll(series2);
        }

        if (isCalc == "DDtoCK42") {
            XYChart.Series seriesCK42 = new XYChart.Series();
            seriesCK42.setName("Координати");
            getChartDataDDtoCK42();
            seriesCK42.setData(gps);
            scatterChart.getData().addAll(seriesCK42);
        }

        if (isCalc == "DMStoCK42") {
            XYChart.Series seriesDMStoCK42 = new XYChart.Series();
            seriesDMStoCK42.setName("Координати");
            getChartDataDMStoCK42();
            seriesDMStoCK42.setData(gps);
            scatterChart.getData().addAll(seriesDMStoCK42);
        }

        if (isCalc == "CK42toDD") {
            XYChart.Series seriesWGS = new XYChart.Series();
            seriesWGS.setName("Координати");
            getChartDataCK42toDD();
            seriesWGS.setData(gps);
            scatterChart.getData().addAll(seriesWGS);
        }

        if (isCalc == "OGZ84") {
            XYChart.Series seriesOGZ1 = new XYChart.Series();
            seriesOGZ1.setName("Набір даних 1");
            XYChart.Series seriesOGZ2 = new XYChart.Series();
            seriesOGZ2.setName("Набір даних 2");
            getChartDataOGZ();
            seriesOGZ1.setData(gps1);
            seriesOGZ2.setData(gps2);
            scatterChart.getData().addAll(seriesOGZ1, seriesOGZ2);

        }
        if (isCalc == "PGZ84") {
            XYChart.Series seriesPGZ1 = new XYChart.Series();
            seriesPGZ1.setName("Набір даних 1");
            XYChart.Series seriesPGZ2 = new XYChart.Series();
            seriesPGZ2.setName("Набір даних 2");
            getChartDataPGZ();
            seriesPGZ1.setData(gps1);
            seriesPGZ2.setData(gps2);
            scatterChart.getData().addAll(seriesPGZ1, seriesPGZ2);
        }

        if (isCalc == "NMEA") {
            XYChart.Series seriesNMEA = new XYChart.Series();
            seriesNMEA.setName("Координати");
            getChartDataNMEA();
            seriesNMEA.setData(gps);
            scatterChart.getData().addAll(seriesNMEA);
        }
    }

    public void onClickLineChart(ActionEvent actionEvent) throws IOException {
        os.viewURL = "/view/chartConverter.fxml";
        os.title = "Графік GPS   " + openFile;
        os.maximized = false;
        os.openStage();

        Stage stage = (Stage) lineChartButton.getScene().getWindow();
        stage.close();
    }
}

