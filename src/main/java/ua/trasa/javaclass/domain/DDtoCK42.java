package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DDtoCK42 {
    private double latitudeDD;
    private double longitudeDD;
    private double altitudeDD;
    private String memo;

    private int latD;
    private int latM;
    private double latS;
    private int longD;
    private int longM;
    private double longS;

    private double xCK42;
    private double yCK42;
    private double hCK42;
    private int zoneCK42;

    @AllArgsConstructor
    @Data
    public static class DD {
        private double latitudeDD;
        private double longitudeDD;
        //private double altitudeDD;
    }

    @AllArgsConstructor
    @Data
    public static class DMS {
        private int latD;
        private int latM;
        private double latS;
        private int longD;
        private int longM;
        private double longS;
    }

    @AllArgsConstructor
    @Data
    public static class CK42 {
        private double xCK42;
        private double yCK42;
        private double hCK42;
        private double zoneCK42;
    }

    @Override
    public String toString() {
        return latitudeDD + ",    " + longitudeDD + ",        " + latD +"°"+latM+"'"+latS+"\",          "+ longD+"°"+longM+"'"+longS+"\"" + ",         " + altitudeDD + ",            " + xCK42 + ",    " + yCK42 + ",    " + hCK42 + ",    " + memo +"\n";
    }
}
