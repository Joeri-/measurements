package abb.interview.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerMap extends HashMap<DeviceGroup, Map<Direction, List<Power>>> {

    public PowerMap() {
    }

    public Power calcTotalPowerPerGroupAndDirection(DeviceGroup groupName, Direction direction) {
        Power totalPower = new Power();

        totalPower.setAvg(
                this.get(groupName).get(direction).stream().map(power -> power.getAvg())
                        .mapToDouble(Double::doubleValue).sum()
        );

        totalPower.setMin(
                this.get(groupName).get(direction).stream().map(power -> power.getMin())
                        .mapToDouble(Double::doubleValue).sum()
        );

        totalPower.setMax(
                this.get(groupName).get(direction).stream().map(power -> power.getMax())
                        .mapToDouble(Double::doubleValue).sum()
        );

        return totalPower;
    }

    public void printTotalPowerPerGroupAndDirection(DeviceGroup groupName, Direction direction) {
        Power totalPower = this.calcTotalPowerPerGroupAndDirection(groupName, direction);

        System.out.println("\n" + groupName);
        System.out.println("Direction: " + direction);
        System.out.println("\tAVG: " + String.format("%.4f", totalPower.getAvg()));
        System.out.println("\tMIN: " + String.format("%.4f", totalPower.getMin()));
        System.out.println("\tMAX: " + String.format("%.4f", totalPower.getMax()));
    }
}
