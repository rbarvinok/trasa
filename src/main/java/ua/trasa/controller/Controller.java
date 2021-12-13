package ua.trasa.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ua.trasa.javaclass.domain.InputDataMaster;
import ua.trasa.javaclass.domain.Master;
import ua.trasa.javaclass.domain.Slave;
import ua.trasa.javaclass.servisClass.*;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Math.rint;
import static ua.trasa.javaclass.servisClass.FileChooserRun.selectedOpenFile;

@Slf4j
public class Controller implements Initializable {
    static AlertAndInform inform = new AlertAndInform();
    OpenStage os = new OpenStage();
    FileChooserRun fileChooserRun = new FileChooserRun();
    GetSettings getSettings = new GetSettings();

    public static String localZone;
    public int colsInpDate = 0;
    public static String openFile = "";
    public static String openDirectory;
    public String status;
    public String missionID, radarLat,radarLon, radarAlt;
    public String launchLat, launchLon, launchAlt, launchX, launchY, launchZ;
    public static double allTime, timeStart, timeStop;

    public static List<InputDataMaster> inputDataMaster = new ArrayList<>();
    public static List<Master> master = new ArrayList<>();
    public static List<Slave> slave = new ArrayList<>();
    public ObservableList<InputDate> inputDatesList = FXCollections.observableArrayList();

    @FXML
    public TableView outputTable;
    public TextField statusBar;
    public TextField labelLineCount;
    public Button tPosition, tNew, tSave, tOpenFile, tMaster, tSlave, tChart;
    public Button tKML, tGoogleEarth;

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getSettings.getSettings();
    }

    public void onClickOpenFile() {
        if (inputDataMaster.isEmpty()) {
            statusBar.setText("");
            fileChooserRun.openFileChooser();
            openFile = selectedOpenFile.getName().substring(0, selectedOpenFile.getName().length() - 4);
            openDirectory = selectedOpenFile.getParent();
            openInputData();
        } else {
            inform.hd = "Файл уже відкритий";
            inform.ct = " Повторне відкриття файлу призведе до втрати не збережених даних \n";
            inform.inform();
        }
    }

    @SneakyThrows
    public void openInputData() {
        status = "InputData";

        FileReader fileReader = new FileReader(selectedOpenFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int lineNumber = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {

            if (lineNumber == 5) {
                missionID = line.split(":")[1].trim();
            }
            if (lineNumber == 22) {
                radarLat = line.split(":")[2].replace("Lon","");;
                radarLon = line.split(":")[3].replace("Alt","");;
                radarAlt = line.split(":")[4];
            }
            if (lineNumber == 29) {
                launchLat = line.split(":")[2].replace("Lon","");
                launchLon = line.split(":")[3].replace("Alt","");
                launchAlt = line.split(":")[4];
            }
            if (lineNumber == 30) {
                launchX = line.split(":")[1];
            }
            if (lineNumber == 31) {
                launchY = line.split(":")[1];
            }
            if (lineNumber == 32) {
                launchZ = line.split(":")[1];
            }
            if (lineNumber == 57) {
                timeStart = Double.parseDouble(line.split(",")[0]);
            }

            //line = line.replaceAll(";", ",");
            String[] split = line.split(",");

            if (lineNumber < 57) {
                lineNumber++;
                continue;
            }
            lineNumber++;
            InputDataMaster idm = new InputDataMaster(
                    Double.parseDouble(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]),
                    Double.parseDouble(split[4]),
                    Double.parseDouble(split[5]),
                    Double.parseDouble(split[6]),
                    Double.parseDouble(split[7]),
                    Double.parseDouble(split[8]),
                    Double.parseDouble(split[9]),
                    Double.parseDouble(split[10]),
                    Double.parseDouble(split[11]),
                    Double.parseDouble(split[12]),
                    Double.parseDouble(split[13]),
                    Double.parseDouble(split[14])
            );
            inputDataMaster.add(idm);

            timeStop = Double.parseDouble(line.split(",")[0]);
            lineNumber++;
        }
        fileReader.close();

        tMaster.setDisable(false);
        tSlave.setDisable(false);

        allTime = rint((timeStop-timeStart)*100000)/100000;
        labelLineCount.setText("Строк: " + (lineNumber-57));
        statusBar.setText("Файл: " + openFile + "      ID: " + missionID + "     Час супроводження: " + allTime);

        inputDates(inputDataMaster);
        TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
        TableColumn<InputDate, String> tTimeUTC = new TableColumn<>("Час UTC");
        TableColumn<InputDate, String> tX = new TableColumn<>("Дальність Х");
        TableColumn<InputDate, String> tY = new TableColumn<>("Висота Y");
        TableColumn<InputDate, String> tZ = new TableColumn<>("Зміщення Z");
        TableColumn<InputDate, String> tVx = new TableColumn<>("Vx");
        TableColumn<InputDate, String> tVy = new TableColumn<>("Vy");
        TableColumn<InputDate, String> tVz = new TableColumn<>("Vz");
        TableColumn<InputDate, String> tVrad = new TableColumn<>("V rad");
        TableColumn<InputDate, String> tRrad = new TableColumn<>("Похила дальність");
        TableColumn<InputDate, String> tAz = new TableColumn<>("Азимут");
        TableColumn<InputDate, String> tEl = new TableColumn<>("Кут місця");
        TableColumn<InputDate, String> tLat = new TableColumn<>("Широта");
        TableColumn<InputDate, String> tLon = new TableColumn<>("Довгота");
        TableColumn<InputDate, String> tVtang = new TableColumn<>("V танг");

        for (int i = 0; i <= colsInpDate - 15; i++) {
            final int indexColumn = i;
            tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
            tTimeUTC.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
            tX.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
            tY.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
            tZ.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
            tVx.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(5 + indexColumn)));
            tVy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(6 + indexColumn)));
            tVz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(7 + indexColumn)));
            tVrad.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(8 + indexColumn)));
            tRrad.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(9 + indexColumn)));
            tAz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(10 + indexColumn)));
            tEl.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(11 + indexColumn)));
            tLat.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(12 + indexColumn)));
            tLon.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(13 + indexColumn)));
            tVtang.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(14 + indexColumn)));
        }
        outputTable.getColumns().addAll(tTime, tTimeUTC, tX,tY,tZ, tVx, tVy, tVz, tVrad, tRrad, tAz, tEl, tLat, tLon, tVtang);
        outputTable.setItems(inputDatesList);
    }

    public void onClickSlave() {
        tSave.setDisable(false);
        tKML.setDisable(false);
        tChart.setDisable(false);
    }

    public void onClickMaster() {
        tSave.setDisable(false);
        tKML.setDisable(false);
        tChart.setDisable(false);
    }

    public void onClickChart() throws IOException {
        os.viewURL = "/view/lineChart.fxml";
        os.title = "Графік GPS   " + openFile;
        os.openStage();
    }

    @SneakyThrows
    public void onClickSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти як...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*.xlsx", "*.xlsx"),
                new FileChooser.ExtensionFilter("*.csv", "*.csv"),
                new FileChooser.ExtensionFilter("*.*", "*.*"));
        fileChooser.setInitialFileName(openFile + "_result");
        File userDirectory = new File(openDirectory);
        fileChooser.setInitialDirectory(userDirectory);
        File file = fileChooser.showSaveDialog((new Stage()));


        statusBar.setText("Успішно записано в файл  '" + openFile + "_result.csv");
    }


    public void inputDates(List source) {
        outputTable.getColumns().clear();
        outputTable.getItems().clear();
        outputTable.setEditable(false);
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

    @SneakyThrows
    public void positionDefinition() {
        os.viewURL = "/view/position.fxml";
        os.title = "Визначення координат об'єктів";
        os.openStage();
    }

    @SneakyThrows
    public void onClickKML() {
        if (outputTable.getItems() == null) {
            statusBar.setText("Помилка! Відсутні дані для рохрахунку");
            inform.hd = "Помилка! Відсутні дані для рохрахунку";
            inform.ct = " 1. Відкрити файл  даних \n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
            inform.alert();
            statusBar.setText("");
        } else {
            //output to Table----------------------------------------
            //inputDates(gpsTimes);
            TableColumn<InputDate, String> tLong = new TableColumn<>("Довгота");
            TableColumn<InputDate, String> tLat = new TableColumn<>("Широта");
            TableColumn<InputDate, String> tAlt = new TableColumn<>("Висота");

            for (int i = 0; i <= colsInpDate - 6; i++) {
                final int indexColumn = i;
                tLat.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
                tLong.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
                tAlt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
            }
            outputTable.getColumns().addAll(tLong, tLat, tAlt);
            outputTable.setItems(inputDatesList);
            //--------------------------------------------------------
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Зберегти як...");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(".kml", "*.kml"));
            fileChooser.setInitialFileName(openFile + "_kml");
            File userDirectory = new File(openDirectory);
            fileChooser.setInitialDirectory(userDirectory);

            File file = fileChooser.showSaveDialog((new Stage()));

            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");
            osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            osw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            osw.write("<Document>\n");
            osw.write("<name>" + openFile + "</name>\n");
            osw.write("<visiblity>1</visiblity>\n");
            osw.write("<description>Exported track data\n" + openFile + "</description>\n");
            osw.write("\n");
            osw.write("<Style id=\"trackcolor_" + openFile + "_1\">\n");
            osw.write("<LineStyle>\n");
            osw.write("<color>ff0ff0ff</color>\n");
            osw.write("<width>4</width>\n");
            osw.write("</LineStyle>\n");
            osw.write("</Style>\n\n");
            osw.write("<Placemark>\n");
            osw.write("<name>" + openFile + "</name>\n");
            osw.write("<visibility>1</visibility>\n");
            osw.write("<description>Exported track data</description>\n");
            osw.write("<styleUrl>#trackcolor_" + openFile + "_1\">\n</styleUrl>\n");
            osw.write("<LineString>\n");
            osw.write("<tessellate>1</tessellate>\n");
            osw.write("<altitudeMode>absolute</altitudeMode>\n");
            osw.write("<coordinates>\n");
//            for (GPSTime gpsTimes : gpsTimes) {
//                osw.write(gpsTimes.toStringKML());
//            }
            osw.write("</coordinates>\n");
            osw.write("</LineString>\n");
            osw.write("</Placemark>\n");
            osw.write("</Document>\n");
            osw.write("</kml>\n");
            osw.close();

            statusBar.setText("Успішно записано в файл '" + openFile + "_kml'");
            inform.title = "Збереження файлу";
            inform.hd = null;
            inform.ct = "Успішно записано в файл '" + openFile + "_kml'";
            inform.inform();
        }
    }

    @SneakyThrows
    public void onClickGoogleEarth() {
        Process process = Runtime.getRuntime().exec("cmd.exe /c start " + "googleearth/GoogleEarthPro.exe ");

    }

    @SneakyThrows
    public void onClickManual() {
        if (Desktop.isDesktopSupported()) {
            File url = new File("userManual/UserManual.pdf");
            Desktop desktop = Desktop.getDesktop();
            desktop.open(url);
        }
    }

    public void onClick_menuAbout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/about.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onClickNew(ActionEvent e) {
        tMaster.setDisable(true);
        tSlave.setDisable(true);
        tSave.setDisable(true);
        tKML.setDisable(true);
        tChart.setDisable(true);

        outputTable.getColumns().clear();
        outputTable.getItems().clear();
        statusBar.setText("");
        labelLineCount.setText("");
        inputDataMaster.clear();
        master.clear();
        slave.clear();
    }

    public void onClickSetup() throws IOException {
        os.viewURL = "/view/settings.fxml";
        os.title = "Часовий пояс   " + openFile;
        os.isModality = true;
        os.isResizable = false;
        os.openStage();
    }

    public void onClickCancelBtn(ActionEvent event) {
        System.exit(0);
    }
}