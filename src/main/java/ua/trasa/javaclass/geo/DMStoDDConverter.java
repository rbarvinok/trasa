package ua.trasa.javaclass.geo;

public class DMStoDDConverter {
    int latD, latM, lonD, lonM;
    double latS, lonS;
    public static double latitudeDD, longitudeDD, altitudeDD;


    public void DMStoDD(String latDMS, String lonDMS, String alt) {

        latD = Integer.parseInt(String.valueOf(latDMS.split("°")[0]));
        latM = Integer.parseInt(String.valueOf(latDMS.replace("'", "°").split("°")[1]));
        latS = Double.parseDouble(latDMS.replaceAll("\"", "")
                .replaceAll(",", ".")
                .split("'")[1]);

        lonD = Integer.parseInt(String.valueOf(lonDMS.split("°")[0]));
        lonM = Integer.parseInt(String.valueOf(lonDMS.replace("'", "°").split("°")[1]));
        lonS = Double.parseDouble(lonDMS.replaceAll("\"", "")
                .replaceAll(",", ".")
                .split("'")[1]);

        latitudeDD = Math.rint((latD + Double.valueOf(latM) / 60 + Double.valueOf(latS) / 60 / 60) * 100000000) / 100000000;
        longitudeDD = Math.rint((lonD + Double.valueOf(lonM) / 60 + Double.valueOf(lonS) / 60 / 60) * 100000000) / 100000000;
        altitudeDD = Double.parseDouble(alt);

    }
}






