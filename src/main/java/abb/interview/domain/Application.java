package abb.interview.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Application {

    private static final Measurements measurements = new Measurements();
    private static final String MEASUREMENTS_FILE = "measurements.json";

    public static void main(String[] args) throws IOException {
        Application app = new Application();

        List<Measurement> measurementList = app.readMeasurementsFromFile(MEASUREMENTS_FILE);

        for (Measurement measurement : measurementList) {
            Key key = new Key(measurement.getResourceId(), measurement.getDeviceName(), measurement.getDeviceGroup().toString());
            measurements.put(key, measurement);
        }

        app.getTotalPerGroupAndPerDirection(measurements);
    }

    private List<Measurement> readMeasurementsFromFile(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            System.out.println("File not found: " + fileName);
            return Collections.emptyList();
        }

        try {
            return new ObjectMapper().readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return Collections.emptyList();
        }
    }

    public void getTotalPerGroupAndPerDirection(Measurements measurements) {

        Map<DeviceGroup, Map<Direction, List<Power>>> powerMap = new HashMap<>();
        Map<Direction, List<Power>> groupA = new HashMap<>();
        groupA.put(Direction.IN, new ArrayList<>());
        groupA.put(Direction.OUT, new ArrayList<>());

        // generalize to all options for direction
        Map<Direction, List<Power>> groupB = new HashMap<>();
        groupB.put(Direction.IN, new ArrayList<>());
        groupB.put(Direction.OUT, new ArrayList<>());

        // generalize to all options for deviceGroup
        powerMap.put(DeviceGroup.GROUP_A, groupA);
        powerMap.put(DeviceGroup.GROUP_B, groupB);

        for (Key key : measurements.keySet()) {
            Measurement m = measurements.get(key);
            powerMap.get(m.getDeviceGroup()).get(m.getDirection()).addAll(m.getPower());
        }

        printTotalPowerPerGroupAndDirection(powerMap, DeviceGroup.GROUP_A, Direction.IN);
        printTotalPowerPerGroupAndDirection(powerMap, DeviceGroup.GROUP_A, Direction.OUT);
        printTotalPowerPerGroupAndDirection(powerMap, DeviceGroup.GROUP_B, Direction.IN);
        printTotalPowerPerGroupAndDirection(powerMap, DeviceGroup.GROUP_B, Direction.OUT);
    }

    public Power calcTotalPowerPerGroupAndDirection(Map<DeviceGroup, Map<Direction, List<Power>>> powerMap,
                                                     DeviceGroup groupName,
                                                     Direction direction) {
        Power totalPower = new Power();

        totalPower.setAvg(
                powerMap.get(groupName).get(direction).stream().map(power -> power.getAvg())
                        .mapToDouble(Double::doubleValue).sum()
        );

        totalPower.setMin(
                powerMap.get(groupName).get(direction).stream().map(power -> power.getMin())
                        .mapToDouble(Double::doubleValue).sum()
        );

        totalPower.setMax(
                powerMap.get(groupName).get(direction).stream().map(power -> power.getMax())
                        .mapToDouble(Double::doubleValue).sum()
        );

        return totalPower;
    }

    public void printTotalPowerPerGroupAndDirection(Map<DeviceGroup, Map<Direction, List<Power>>> powerMap,
                                                     DeviceGroup groupName,
                                                     Direction direction) {
        Power totalPower = calcTotalPowerPerGroupAndDirection(powerMap, groupName, direction);

        System.out.println("\n" + groupName);
        System.out.println("Direction: " + direction);
        System.out.println("\tAVG: " + String.format("%.4f", totalPower.getAvg()));
        System.out.println("\tMIN: " + String.format("%.4f", totalPower.getMin()));
        System.out.println("\tMAX: " + String.format("%.4f", totalPower.getMax()));
    }
}
