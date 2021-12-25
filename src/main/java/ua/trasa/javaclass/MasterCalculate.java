package ua.trasa.javaclass;

import lombok.experimental.UtilityClass;
import ua.trasa.javaclass.domain.InputDataMaster;
import ua.trasa.javaclass.domain.Master;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class MasterCalculate {

    public static Master calculateMaster(InputDataMaster inputDataMaster) {

        Master master = new Master();

        master.setTime(Math.rint(inputDataMaster.getTime()*100)/100);
        master.setAzimuth(inputDataMaster.getAzimuth());
        master.setElevation(inputDataMaster.getElevation());
        master.setRadRange(inputDataMaster.getRadRange());
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