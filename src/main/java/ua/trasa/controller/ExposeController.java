package ua.trasa.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
import static ua.trasa.controller.SettingsController.opacity;
import static ua.trasa.controller.SettingsController.placeView;
import static ua.trasa.javaclass.MasterCalculate.mastersBulk;
import static ua.trasa.javaclass.SlaveCalculate.slaveBulk;

public class ExposeController implements Initializable {
    OpenStage os = new OpenStage();
    public static Stage expStage;
    public double currentTime;
    public int colsInpDate = 0;
    public boolean isMinimize = false;
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
    public Button tMax, tMin, tInfo;
    public LineChart chartAz, chartEl;
    public MenuBar menuBar;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //expStage = (Stage) tMin.getScene().getWindow();
        statusLabel.setText(status);
        labelLineCount.setText("Строк: " + lineCount);
        statusBar.setText("Файл: " + openFile + "     Час супроводження: " + allTime + " сек");
        bStartStop.requestFocus();
        //timerLabelMin.setText(String.valueOf(currentTime));
        reversTime.setText("/  " + rint((allTime - currentTime) * 100) / 100);
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
        TableColumn<InputDate, String> tRrad = new TableColumn<>("Дальність");
        TableColumn<InputDate, String> tVrad = new TableColumn<>("Швидкість");

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
        ObservableList<InputDate> items = table.getItems();
        items.forEach(item -> {
            item.getItems().stream().filter(rowItem -> String.valueOf(searchId).equals(rowItem))
                    .findAny()
                    .ifPresent(s -> {
                        table.getSelectionModel().select(item);
                        table.scrollTo(item);
                    });
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
                if ((allTime - currentTime) < 0)
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
        if (!(isMinimize)) {
            isMinimize = true;
            Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
            expStage = (Stage) tMin.getScene().getWindow();
            //expStage.initStyle(StageStyle.UNDECORATED);
            expStage.setResizable(true);
            expStage.setAlwaysOnTop(true);
            expStage.setOpacity(opacity / 100);

            switch (placeView) {
                case "leftUp":
                    expStage.setX(1);
                    expStage.setY(1);
                    break;
                case "leftDown":
                    expStage.setX(1);
                    expStage.setY(sSize.height - 430);
                    break;
                case "rightUp":
                    expStage.setX(sSize.width - 350);
                    expStage.setY(1);
                    break;
                case "rightDown":
                    expStage.setX(sSize.width - 350);
                    expStage.setY(sSize.height - 430);
                    break;
            }

            menuBar.setVisible(false);
            expStage.setWidth(350);
            expStage.setHeight(350);

        } else if (isMinimize) {
            isMinimize = false;
            expStage.setWidth(1070.0);
            expStage.setHeight(655);
            menuBar.setVisible(true);
            expStage.setX(10);
            expStage.setY(10);
            expStage.setResizable(true);
            expStage.setAlwaysOnTop(false);
            expStage.setOpacity(1.0);
        }
    }

    @SneakyThrows
    public void onClickSettings() {
        os.viewURL = "/view/settings.fxml";
        os.title = "Налаштування";
        os.isModality = true;
        os.isResizable = false;
        os.openStage();
    }

    public void onClickClose(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void onClickMoreInformation(ActionEvent actionEvent) {
        primaryStage.show();
        //expStage.hide();
    }
}
