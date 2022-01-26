package ua.trasa.javaclass;

import lombok.experimental.UtilityClass;
import ua.trasa.javaclass.domain.InputDataMaster;
import ua.trasa.javaclass.domain.Master;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.rint;

@UtilityClass
public class MasterCalculate {

    public static Master calculateMaster(InputDataMaster inputDataMaster) {

        Master master = new Master();

        master.setTime(rint(inputDataMaster.getTime()*100)/100);
        master.setAzimuth(rint(inputDataMaster.getAzimuth()*100)/100);
        master.setElevation(rint(inputDataMaster.getElevation()*100)/100);
        master.setRadRange(rint(inputDataMaster.getRadRange()*1000)/1000);
        master.setRadVelocity(inputDataMaster.getRadVelocity());
        master.setRangeX(inputDataMaster.getRangeX());
        master.setHeightY(inputDataMaster.getHeightY());
        master.setOffsetZ(inputDataMaster.getOffsetZ());
        master.setSnr(inputDataMaster.getSnr());

        return master;
    }

    public static List<Master> mastersBulk(List<InputDataMaster> inputDataMasters) {
        return inputDataMasters.stream().map(MasterCalculate::calculateMaster).collect(Collectors.toList());
    }

}