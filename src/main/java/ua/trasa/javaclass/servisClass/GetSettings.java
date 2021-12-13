package ua.trasa.javaclass.servisClass;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static ua.trasa.controller.Controller.localZone;



public class GetSettings {
    public void getSettings() throws IOException {

        FileReader fileReader1 = new FileReader("settings.txt");
        BufferedReader bufferedReader1 = new BufferedReader(fileReader1);

        int lineNumber1 = 0;
        String line1 = "";
        while ((line1 = bufferedReader1.readLine()) != null) {
            if (lineNumber1 == 0) {
                localZone = (line1.split("=")[1]);
            }
            lineNumber1++;
        }
        fileReader1.close();
    }

}
