package ua.trasa.javaclass.geo;

import static java.lang.Math.abs;
import static java.lang.Math.rint;

public class DDtoDMSConverter {
    public static int latD, latM;
    public static double latS;
    public static int longD, longM;
    public static double longS, altDMS;

    public void DDtoDMS(String latDD, String lonDD, String alt) {
        latD = (int)abs(Double.parseDouble(latDD));
        latM = (int)abs((Double.parseDouble(latDD) - latD) * 60);
        latS = rint((((Double.parseDouble(latDD) - latD) * 60) - latM) * 60 * 10000000) / 10000000;

        longD = (int)abs(Double.parseDouble(lonDD));
        longM = (int)abs((Double.parseDouble(lonDD) - longD) * 60);
        longS = rint((((Double.parseDouble(lonDD) - longD) * 60) - longM) * 60 * 10000000) / 10000000;

        altDMS = Double.parseDouble(alt);

    }
}






