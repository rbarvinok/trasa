package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InputDataMaster {

    private double time;
    private double timeUTC;
    private double azimuth;
    private double elevation;

    private double radRange;
    private double radVelocity;
    private double rangeX;
    private double heightY;
    private double offsetZ;

    private double latitude;
    private double longitude;
    private double altitude;
    private double snr;

    @AllArgsConstructor
    @Data
    public static class DD {
        private double latitude;
        private double longitude;
    }

    @AllArgsConstructor
    @Data
    public static class Altitude {
        private double time;
        private double altitude;
    }

    @Override
    public String toString() {
        return  time + ","  +azimuth+ "," +elevation+ "," +radRange+ "," +radVelocity+ "," +rangeX+ "," +heightY+ "," +offsetZ+ "," +latitude+ "," +longitude + "," + altitude + "," + snr + "\n";
    }

    public String toStringKML() {
        return  longitude+ "," + latitude + ","  + altitude + "\n";
    }

}

