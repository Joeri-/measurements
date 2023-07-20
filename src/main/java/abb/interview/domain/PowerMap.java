package abb.interview.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerMap extends HashMap<DeviceGroup, Map<Direction, List<Power>>> {

    public PowerMap() {
        Map<Direction, List<Power>> groupA = new HashMap<>();
        groupA.put(Direction.IN, new ArrayList<>());
        groupA.put(Direction.OUT, new ArrayList<>());

        // generalize to all options for direction
        Map<Direction, List<Power>> groupB = new HashMap<>();
        groupB.put(Direction.IN, new ArrayList<>());
        groupB.put(Direction.OUT, new ArrayList<>());

        // generalize to all options for deviceGroup
        this.put(DeviceGroup.GROUP_A, groupA);
        this.put(DeviceGroup.GROUP_B, groupB);
    }

    public void parseMeasurements(Measurements measurements) {
        for (Key key : measurements.keySet()) {
            Measurement m = measurements.get(key);
            this.get(m.getDeviceGroup()).get(m.getDirection()).addAll(m.getPower());
        }
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
