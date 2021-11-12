package ua.trasa.javaclass.servisClass;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;

import static ua.trasa.controller.Controller.localZone;

public class GetSettings {
    @SneakyThrows
    public void getSettings() {

        FileReader fileReader1 = new FileReader("settings.txt");
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);

        int lineNumber1 = 0;
        String line1;
        while ((line1 = bufferedReader1.readLine()) != null) {
            lineNumber1++;

            if (lineNumber1 == 1) {
                localZone = (int) Double.parseDouble(line1.split("=")[1]);
            }

        }
        fileReader1.close();
    }

}
