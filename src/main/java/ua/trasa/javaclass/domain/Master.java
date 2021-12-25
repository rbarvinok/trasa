package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Master {

    private double time;
    private double azimuth;
    private double elevation;
    private double radRange;
    private double radVelocity;
    private double rangeX;
    private double heightY;
    private double offsetZ;
    private double snr;

    public Master() { }

    @AllArgsConstructor
    @Data
    public static class GrandeTrack {
        private double rangeX;
        private double offsetZ;
    }

    @AllArgsConstructor
    @Data
    public static class TimeHeight {
        private double time;
        private double heightY;
    }

    @Override
    public String toString() {
        return  time + "," + azimuth + "," + elevation + "," + radRange+ "," + radVelocity +  "\n";
    }

}

