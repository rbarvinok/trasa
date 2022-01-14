package ua.trasa.javaclass;

import lombok.experimental.UtilityClass;
import ua.trasa.javaclass.domain.InputDataMaster;
import ua.trasa.javaclass.domain.Slave;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static ua.trasa.controller.ControllerPosition.distanceMS;

@UtilityClass
public class SlaveCalculate {

    public static Slave calculateSlave(InputDataMaster inputDataMaster) {
//        http://ru.solverbook.com/spravochnik/formuly-po-geometrii/treugolnik/reshenie-treugolnikov/

        double rRS = sqrt(pow(inputDataMaster.getRadRange(), 2) + pow(distanceMS, 2) - 2 * inputDataMaster.getRadRange() * distanceMS * cos(toRadians(inputDataMaster.getAzimuth())));
        double azS = toDegrees(asin(inputDataMaster.getOffsetZ() * sin(toRadians(inputDataMaster.getAzimuth())) /rRS));
        double elS =  toDegrees(asin(inputDataMaster.getHeightY() * sin(toRadians(inputDataMaster.getElevation())) /rRS));

        Slave slave = new Slave();
        slave.setTime(rint(inputDataMaster.getTime() * 100) / 100);
        slave.setAzimuthS(rint(azS*100)/100);
        slave.setElevationS(rint(elS*100)/100);
        slave.setRadRangeS(rint(rRS*1000)/1000);
        slave.setRadVelocity(inputDataMaster.getRadVelocity());
        slave.setRangeXS(inputDataMaster.getRangeX());
        slave.setHeightYS(inputDataMaster.getHeightY());
        slave.setOffsetZS(inputDataMaster.getOffsetZ());
        slave.setSnr(inputDataMaster.getSnr());

        return slave;
    }

    public static List<Slave> slaveBulk(List<InputDataMaster> inputDataMasters) {
        return inputDataMasters.stream().map(SlaveCalculate::calculateSlave).collect(Collectors.toList());
    }

}