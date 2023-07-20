package abb.interview.domain;

import java.util.*;
import java.util.stream.Collectors;

public class PowerMap extends HashMap<DeviceGroup, Map<Direction, List<Measurement>>> {

    public PowerMap() {
        Map<Direction, List<Measurement>> groupA = new HashMap<>();
        groupA.put(Direction.IN, new ArrayList<>());
        groupA.put(Direction.OUT, new ArrayList<>());

        // Todo: generalize to all options for direction
        Map<Direction, List<Measurement>> groupB = new HashMap<>();
        groupB.put(Direction.IN, new ArrayList<>());
        groupB.put(Direction.OUT, new ArrayList<>());

        // Todo: generalize to all options for deviceGroup
        this.put(DeviceGroup.GROUP_A, groupA);
        this.put(DeviceGroup.GROUP_B, groupB);
    }

    public void parseMeasurements(Measurements measurements) {
        for (Key key : measurements.keySet()) {
            Measurement m = measurements.get(key);
            this.get(m.getDeviceGroup()).get(m.getDirection()).add(m);
        }
    }

    public void printTotalPowerPerGroupAndDirection() {
        List<DeviceGroup> sortedDeviceGroup = Arrays.stream(DeviceGroup.values())
                .sorted()
                .collect(Collectors.toList());

        List<Direction> sortedDirection = Arrays.stream(Direction.values())
                .sorted()
                .collect(Collectors.toList());

        for (DeviceGroup deviceGroup : sortedDeviceGroup) {
            for (Direction direction : sortedDirection) {
                Power totalPower = this.calcTotalPowerPerGroupAndDirection(deviceGroup, direction);

                System.out.println("\n" + deviceGroup);
                System.out.println("Direction: " + direction);
                System.out.println("\tAVG: " + String.format("%.4f", totalPower.getAvg()));
                System.out.println("\tMIN: " + String.format("%.4f", totalPower.getMin()));
                System.out.println("\tMAX: " + String.format("%.4f", totalPower.getMax()));
            }
        }
    }

    public Power calcTotalPowerPerGroupAndDirection(DeviceGroup groupName, Direction direction) {
        Power totalPower = new Power();

        totalPower.setAvg(
                this.get(groupName).get(direction).stream()
                        .map(measurement -> measurement.getPower())
                        .flatMap(List::stream)
                        .map(power -> power.getAvg())
                        .mapToDouble(Double::doubleValue).sum()
        );

        totalPower.setMin(
                this.get(groupName).get(direction).stream()
                        .map(measurement -> measurement.getPower())
                        .flatMap(List::stream)
                        .map(power -> power.getMin())
                        .mapToDouble(Double::doubleValue).sum()
        );

        totalPower.setMax(
                this.get(groupName).get(direction).stream()
                        .map(measurement -> measurement.getPower())
                        .flatMap(List::stream)
                        .map(power -> power.getMax())
                        .mapToDouble(Double::doubleValue).sum()
        );

        return totalPower;
    }

    public void printSortedMaxPowerDevicesPerGroupAndDirection() {

        List<DeviceGroup> sortedDeviceGroup = Arrays.stream(DeviceGroup.values()).sorted().collect(Collectors.toList());
        List<Direction> sortedDirection = Arrays.stream(Direction.values()).sorted().collect(Collectors.toList());

        for (DeviceGroup deviceGroup : sortedDeviceGroup) {
            for (Direction direction : sortedDirection) {

                List<PrintableDeviceWithMaxPower> devicesWithMaxPower = this.get(deviceGroup).get(direction).stream()
                        .map(measurement -> new PrintableDeviceWithMaxPower(measurement))
                        .sorted(Comparator.comparingDouble(PrintableDeviceWithMaxPower::getMaxPower))
                        .collect(Collectors.toList());

                System.out.println(devicesWithMaxPower);
            }
        }
    }
}
