package abb.interview.domain;

import java.util.*;
import java.util.stream.Collectors;

public class PowerMap extends HashMap<DeviceGroup, Map<Direction, List<Measurement>>> {

    public PowerMap() {
        for (DeviceGroup deviceGroup : getSortedListOfDeviceGroups()) {
            Map<Direction, List<Measurement>> deviceGroupMap = new HashMap<>();
            for (Direction direction : getSortedListOfDirections()) {
                deviceGroupMap.put(direction, new ArrayList<>());
            }
            this.put(deviceGroup, deviceGroupMap);
        }
    }

    private List<DeviceGroup> getSortedListOfDeviceGroups() {
        return Arrays.stream(DeviceGroup.values())
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Direction> getSortedListOfDirections() {
        return Arrays.stream(Direction.values())
                .sorted()
                .collect(Collectors.toList());
    }

    public void parseMeasurements(Measurements measurements) {
        for (Key key : measurements.keySet()) {
            Measurement m = measurements.get(key);
            this.get(m.getDeviceGroup()).get(m.getDirection()).add(m);
        }
    }

    public void printTotalPowerPerGroupAndDirection() {
        for (DeviceGroup deviceGroup : getSortedListOfDeviceGroups()) {
            for (Direction direction : getSortedListOfDirections()) {
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
        for (PrintableDeviceWithMaxPower printableDeviceWithMaxPower : constructSortedMaxPowerDevicesPerGroupAndDirection()) {
            System.out.println(printableDeviceWithMaxPower);
        }
    }

    public List<PrintableDeviceWithMaxPower> constructSortedMaxPowerDevicesPerGroupAndDirection() {

        List<PrintableDeviceWithMaxPower> sortedListOfEverything = new ArrayList<>();

        for (DeviceGroup deviceGroup : getSortedListOfDeviceGroups()) {
            for (Direction direction : getSortedListOfDirections()) {
                List<PrintableDeviceWithMaxPower> devicesWithMaxPower = this.get(deviceGroup).get(direction).stream()
                        .map(measurement -> new PrintableDeviceWithMaxPower(measurement))
                        .sorted(Comparator.comparingDouble(PrintableDeviceWithMaxPower::getMaxPower))
                        .collect(Collectors.toList());

                sortedListOfEverything.addAll(devicesWithMaxPower);
            }
        }

        return sortedListOfEverything;
    }
}
