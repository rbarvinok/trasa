package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class InputDataMaster {

    private double time;
    private double timeUTC;
    private double rangeX;
    private double heightY;
    private double offsetZ;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private double radVelocity;
    private double radRange;
    private double azimuth;
    private double elevation;
    private double latitude;
    private double longitude;
    private double tangVelocity;

    public InputDataMaster() { }

    @AllArgsConstructor
    @Data
    public static class DD {
        private double latitude;
        private double longitude;
    }
    @Override
    public String toString() {
        return  time + "," +timeUTC + "," +rangeX+ "," +heightY+ "," +offsetZ+ "," +velocityX+ "," +velocityY+ "," +velocityZ+ "," +radVelocity+ "," +radRange+ "," +azimuth + "," + elevation + "," + latitude+ "," + longitude + ","  + tangVelocity + "\n";
    }

}

