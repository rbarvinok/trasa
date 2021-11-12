package ua.trasa.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SourceDD {

    private double latD;
    private double longD;
    private double altitude;
    private String memo;

    public SourceDD( ) {
        double latD;
        double longD;
        double altitude;
    }

    @AllArgsConstructor
    @Data
    public static class DD {
        private double latD;
        private double longD;
        private double altitude;
    }

    @Override
    public String toString() {
        return latD + "," + longD + "," + altitude + "," +memo +"\n";
    }

}

