package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Slave {

    private double time;
    private double timeUTC;
    private double rangeX;
    private double heightY;
    private double offsetZ;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private double azimuth;
    private double elevation;
    private double latitude;
    private double longitude;
    private double tangVelocity;

    public Slave() { }

    @AllArgsConstructor
    @Data
    public static class DD {
        private double latitude;
        private double longitude;
    }
    @Override
    public String toString() {
        return  time + "," + azimuth + "," + elevation + "," + rangeX+ "," + heightY + ","  + tangVelocity +"," + "\n";
    }

}

