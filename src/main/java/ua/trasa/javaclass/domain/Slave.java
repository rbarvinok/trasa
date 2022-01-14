package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Slave {

    private double time;
    private double azimuthS;
    private double elevationS;
    private double radRangeS;
    private double radVelocity;
    private double rangeXS;
    private double heightYS;
    private double offsetZS;
    private double snr;

    public Slave() { }

    @AllArgsConstructor
    @Data
    public static class TimeAz {
        private double time;
        private double azimuthS;
    }

    @AllArgsConstructor
    @Data
    public static class TimeEl {
        private double time;
        private double elevationS;
    }

    @AllArgsConstructor
    @Data
    public static class GrandeTrack {
        private double rangeXS;
        private double offsetZS;
    }

    @AllArgsConstructor
    @Data
    public static class TimeHeight {
        private double time;
        private double heightYS;
    }

    @Override
    public String toString() {
        return  time + "," + azimuthS + "," + elevationS + "," + radRangeS+ "," + radVelocity +  "\n";
    }
}

