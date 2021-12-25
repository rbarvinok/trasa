package ua.trasa.controller;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import ua.trasa.javaclass.domain.Master;
import ua.trasa.javaclass.domain.Slave;
import ua.trasa.javaclass.servisClass.InputDate;
import ua.trasa.javaclass.servisClass.OpenStage;

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

    @FXML
    public TableView table;
    public Label timerLabel, testLabel;
    public TextField statusLabel, statusBar;
    public TextField labelLineCount;
    public Button tPosition, tMaster, tSlave, tChart;
    public Button tStartStop, tTimerStop;


    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusLabel.setText(status);
        labelLineCount.setText("Строк: " + lineCount);
        statusBar.setText("Файл: " + openFile + "     Час супроводження: " + allTime + " сек");
        tStartStop.requestFocus();
        testLabel.setText(String.valueOf(allTime));
        if (status.equals("MASTER"))
            onClickMaster();
        else onClickSlave();
        timer();
    }

    public void onClickMaster() {
        status = "MASTER";
        masters = mastersBulk(inputDataMasters);
        rateOfDeclinesStrings = masters.stream().map(Master::toString).collect(Collectors.toList());
        getTableData();
        statusLabel.setText(status);
    }

    public void onClickSlave() {
        status = "SLAVE";
        slaves = slaveBulk(inputDataMasters);
        rateOfDeclinesStrings = slaves.stream().map(Slave::toString).collect(Collectors.toList());
        getTableData();
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
                //.filter(item -> item.getId() == searchId)
                .findAny()
                .ifPresent(item -> {
                    table.getSelectionModel().select(item);
                    table.scrollTo(item);
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
                tStartStop.setGraphic(new ImageView("/images/player/pause.png"));
                tTimerStop.setDisable(true);
            }

            @Override
            public void stop() {
                stopTime = System.currentTimeMillis();
                running.set(false);
                super.stop();
                tStartStop.setGraphic(new ImageView("/images/player/play.png"));
                tTimerStop.setDisable(false);
            }

            @Override
            public void handle(long timestamp) {
                long now = System.currentTimeMillis();
                time.set((now - startTime) / 1000.0);
                currentTime = rint(time.getValue() * 100) / 100;
                testLabel.setText(String.valueOf(rint((allTime - currentTime) * 100) / 100));
                findInTable(time.getValue());
            }
        };

        tStartStop.setOnAction(e -> {
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
            line = source.get(j).toString();
            line = line.replace("[", "").replace("]", "");
            rowsInpDate += 1;
            String[] fields = line.split(csvSplitBy, -1);
            colsInpDate = fields.length;
            InputDate inpd = new InputDate(fields);
            inputDatesList.add(inpd);
        }
    }

    public void onClickSetup(ActionEvent actionEvent) {
    }
}
