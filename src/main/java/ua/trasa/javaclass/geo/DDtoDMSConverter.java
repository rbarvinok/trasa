package ua.trasa.javaclass.geo;

import static java.lang.Math.abs;
import static java.lang.Math.rint;

public class DDtoDMSConverter {
    int latD;
    int latM;
    double latS;
    int longD;
    int longM;
    double longS;
    double altDMS;

    public void DDtoDMS(String latDD, String lonDD, String alt) {

        latD = abs(Integer.parseInt(latDD));
        latM = abs(Integer.parseInt(latDD) - latD) * 60;
        latS = rint((((Integer.parseInt(latDD) - latD) * 60) - latM) * 60 * 10000000) / 10000000;

        longD = abs(Integer.parseInt(lonDD));
        longM = abs(Integer.parseInt(lonDD) - longD) * 60;
        longS = rint((((Integer.parseInt(lonDD) - longD) * 60) - longM) * 60 * 10000000) / 10000000;

        altDMS = Double.parseDouble(alt);

    }
}






