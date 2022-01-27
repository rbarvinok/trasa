package ua.trasa.javaclass.geo;

import static java.lang.Math.abs;
import static java.lang.Math.rint;

public class DDtoDMSConverter {
    public static int latD, latM;
    public static double latS;
    public static int longD, longM;
    public static double longS, altDMS;

    public void DDtoDMS(String latDD, String lonDD, String alt) {
        latD = (int)abs(Integer.parseInt(latDD));
        latM = abs(Integer.parseInt(latDD) - latD) * 60;
        latS = rint((((Integer.parseInt(latDD) - latD) * 60) - latM) * 60 * 10000000) / 10000000;

        longD = abs(Integer.parseInt(lonDD));
        longM = abs(Integer.parseInt(lonDD) - longD) * 60;
        longS = rint((((Integer.parseInt(lonDD) - longD) * 60) - longM) * 60 * 10000000) / 10000000;

        altDMS = Double.parseDouble(alt);

    }
}






