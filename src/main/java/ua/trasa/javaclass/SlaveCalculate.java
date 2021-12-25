package ua.trasa.javaclass;

import lombok.experimental.UtilityClass;
import ua.trasa.javaclass.domain.InputDataMaster;
import ua.trasa.javaclass.domain.Slave;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SlaveCalculate {

    public static Slave calculateSlave(InputDataMaster inputDataMaster) {

        Slave slave = new Slave();

        slave.setTime(Math.rint(inputDataMaster.getTime()*100)/100);
        slave.setAzimuth(inputDataMaster.getAzimuth());
        slave.setElevation(inputDataMaster.getElevation());
        slave.setRadRange(inputDataMaster.getRadRange());
        slave.setRadVelocity(inputDataMaster.getRadVelocity());
        slave.setRangeX(inputDataMaster.getRangeX());
        slave.setHeightY(inputDataMaster.getHeightY());
        slave.setOffsetZ(inputDataMaster.getOffsetZ());
        slave.setSnr(inputDataMaster.getSnr());

        return slave;
    }

    public static List<Slave> slaveBulk(List<InputDataMaster> inputDataMasters) {
        return inputDataMasters.stream().map(SlaveCalculate::calculateSlave).collect(Collectors.toList());
    }

}