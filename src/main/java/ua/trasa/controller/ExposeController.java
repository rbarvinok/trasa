package ua.trasa.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import ua.trasa.javaclass.domain.Master;
import ua.trasa.javaclass.domain.Slave;
import ua.trasa.javaclass.servisClass.InputDate;
import ua.trasa.javaclass.servisClass.OpenStage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Math.rint;
import static ua.trasa.controller.Controller.*;
import static ua.trasa.javaclass.MasterCalculate.mastersBulk;
import static ua.trasa.javaclass.SlaveCalculate.slaveBulk;

public class ExposeController implements Initializable {
    OpenStage os = new OpenStage();

    double currentTime;
    public int colsInpDate = 0;
    List<String> rateOfDeclinesStrings;
    public ObservableList<InputDate> inputDatesList = FXCollections.observableArrayList();
    public ObservableList<XYChart.Data> az;
    public ObservableList<XYChart.Data> el;

    @FXML
    public TableView table;
    public Label timerLabel, reversTime;
    public TextField statusLabel, statusBar;
    public TextField labelLineCount;
    public Button tPosition, tMaster, tSlave, tChart;
    public Button bStartStop, bTimerStop;
    public Button tMax, tMin;
    public LineChart chartAz, chartEl;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusLabel.setText(status);
        labelLineCount.setText("Строк: " + lineCount);
        statusBar.setText("Файл: " + openFile + "     Час супроводження: " + allTime + " сек");
        bStartStop.requestFocus();
        reversTime.setText("/  " + rint(allTime * 100) / 100);
        if (status.equals("MASTER"))
            onClickMaster();
        else onClickSlave();
        timer();
        charts();
    }

    public void charts() {
        chartAz.getData().clear();
        chartEl.getData().clear();

        if (status.equals("MASTER")) {
            getAzDataM();
            getElDataM();
        } else if (status.equals("SLAVE")) {
            getAzDataS();
            getElDataS();
        }

        XYChart.Series seriesAz = new XYChart.Series();
        seriesAz.setData(az);

        chartAz.getData().addAll(seriesAz);

        XYChart.Series seriesEl = new XYChart.Series();

        seriesEl.setData(el);
        chartEl.getData().addAll(seriesEl);
    }

    public void onClickMaster() {
        status = "MASTER";
        masters = mastersBulk(inputDataMasters);
        rateOfDeclinesStrings = masters.stream().map(Master::toString).collect(Collectors.toList());
        getTableData();
        charts();
        statusLabel.setText(status);
    }

    public void onClickSlave() {
        status = "SLAVE";
        slaves = slaveBulk(inputDataMasters);
        rateOfDeclinesStrings = slaves.stream().map(Slave::toString).collect(Collectors.toList());
        getTableData();
        charts();
        statusLabel.setText(status);
    }

    public void getTableData() {
        inputDates(rateOfDeclinesStrings);
        TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
        TableColumn<InputDate, String> tAz = new TableColumn<>("Азимут");
        TableColumn<InputDate, String> tEl = new TableColumn<>("Кут місця");
        TableColumn<InputDate, String> tRrad = new TableColumn<>("Похила дальність");
        TableColumn<InputDate, String> tVrad = new TableColumn<>("Радіальна швидкість");

        for (int i = 0; i <= colsInpDate - 5; i++) {
            final int indexColumn = i;
            tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
            tAz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
            tEl.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
            tRrad.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
            tVrad.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
        }
        table.getColumns().addAll(tTime, tAz, tEl, tRrad, tVrad);
        table.setItems(inputDatesList);
    }

    public void findInTable(Double searchId) {
        table.getItems().stream()
                .filter(item -> item == searchId)
                //.filter(item -> item.toString().equals(searchId.toString()))
                .findAny()
                .ifPresent(item -> {
                    table.getSelectionModel().select(item);
                    table.scrollTo(item);
                    System.out.println(searchId);
                });
    }

    public void timer() {
        DoubleProperty time = new SimpleDoubleProperty();
        timerLabel.textProperty().bind(time.asString("%.2f "));

        BooleanProperty running = new SimpleBooleanProperty();
        AnimationTimer timer = new AnimationTimer() {

            private long startTime;
            public long stopTime = 0;

            @Override
            public void start() {
                if (stopTime > 0)
                    startTime = System.currentTimeMillis() - (stopTime - startTime);
                else
                    startTime = System.currentTimeMillis();
                running.set(true);
                super.start();
                bStartStop.setGraphic(new ImageView("/images/player/pause1.png"));
                bTimerStop.setDisable(true);
            }

            @Override
            public void stop() {
                stopTime = System.currentTimeMillis();
                running.set(false);
                super.stop();
                bStartStop.setGraphic(new ImageView("/images/player/play1.png"));
                bTimerStop.setDisable(false);
            }

            @Override
            public void handle(long timestamp) {
                long now = System.currentTimeMillis();
                time.set((now - startTime) / 1000.0);
                currentTime = rint(time.getValue() * 100) / 100;
                reversTime.setText("/  " + rint((allTime - currentTime) * 100) / 100);
                if ((allTime - currentTime)<0)
                    reversTime.setStyle("-fx-text-fill: red");
                findInTable(currentTime);
                //findInTable(4.0);
            }
        };

        bStartStop.setOnAction(e -> {
            if (running.get()) {
                timer.stop();
            } else {
                timer.start();
            }
        });
    }

    public void onClickChart() throws IOException {
        os.viewURL = "/view/lineChart.fxml";
        os.title = "Графік   " + openFile;
        os.openStage();
    }

    public void getAzDataM() {
        List<Master.TimeAz> azList = masters.stream().map(listAz -> {
            return new Master.TimeAz(listAz.getTime(), listAz.getAzimuth());
        }).collect(Collectors.toList());

        az = FXCollections.observableArrayList();
        for (Master.TimeAz azs : azList) {
            az.add(new XYChart.Data(azs.getTime(), azs.getAzimuth()));
        }
    }

    public void getElDataM() {
        List<Master.TimeEl> elList = masters.stream().map(listEl -> {
            return new Master.TimeEl(listEl.getTime(), listEl.getElevation());
        }).collect(Collectors.toList());

        el = FXCollections.observableArrayList();
        for (Master.TimeEl els : elList) {
            el.add(new XYChart.Data(els.getTime(), els.getElevation()));
        }
    }

    public void getAzDataS() {
        List<Slave.TimeAz> azList = slaves.stream().map(listAz -> {
            return new Slave.TimeAz(listAz.getTime(), listAz.getAzimuthS());
        }).collect(Collectors.toList());

        az = FXCollections.observableArrayList();
        for (Slave.TimeAz azs : azList) {
            az.add(new XYChart.Data(azs.getTime(), azs.getAzimuthS()));
        }
    }

    public void getElDataS() {
        List<Slave.TimeEl> elList = slaves.stream().map(listEl -> {
            return new Slave.TimeEl(listEl.getTime(), listEl.getElevationS());
        }).collect(Collectors.toList());

        el = FXCollections.observableArrayList();
        for (Slave.TimeEl els : elList) {
            el.add(new XYChart.Data(els.getTime(), els.getElevationS()));
        }
    }

    @SneakyThrows
    public void positionDefinition() {
        os.viewURL = "/view/position.fxml";
        os.title = "Визначення координат об'єктів";
        os.openStage();
    }

    public void inputDates(List<String> source) {
        table.getColumns().clear();
        table.getItems().clear();
        table.setEditable(false);
        int rowsInpDate = 0;
        String line;
        String csvSplitBy = ",";
        for (int j = 0; j < source.size(); j++) {
            line = source.get(j);
            line = line.replace("[", "").replace("]", "");
            rowsInpDate += 1;
            String[] fields = line.split(csvSplitBy, -1);
            colsInpDate = fields.length;
            InputDate inpd = new InputDate(fields);
            inputDatesList.add(inpd);
        }
    }

    @SneakyThrows
    public void onClickMinimize() {
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/exposeMinimize.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.setScene(new Scene(root));
        stage.setAlwaysOnTop(true);
        stage.setOpacity(0.9);
        //stage.setX(sSize.width - 350);
        stage.setX(1);
        stage.setY(sSize.height - 430);
        //stage.initOwner();
        stage.show();
        stage = (Stage) tMin.getScene().getWindow();
        stage.close();
    }

    public void onClickMax() {
        Stage stage = (Stage) tMax.getScene().getWindow();
        stage.getOwner();
        stage.close();
    }
}
