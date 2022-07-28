package ua.trasa.javaclass.servisClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static ua.trasa.controller.SettingsController.opacity;
import static ua.trasa.controller.SettingsController.placeView;


public class GetSettings {
    public void getSettings() throws IOException {

        FileReader fileReader = new FileReader("settings.txt");
        BufferedReader bufferedReader1 = new BufferedReader(fileReader);

        int lineNumber = 0;
        String line1 = "";
        while ((line1 = bufferedReader1.readLine()) != null) {
            if (lineNumber == 0) {
                placeView = (line1.split("=")[1]);
            }
            if (lineNumber == 1) {
                opacity = Double.parseDouble((line1.split("=")[1]));
            }
            lineNumber++;
        }
        fileReader.close();
    }

}
