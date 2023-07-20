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
            Key key = new Key(measurement.getResourceId(), measurement.getDeviceName(), measurement.getDeviceGroup());
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

    private void getTotalPerGroupAndPerDirection(Measurements measurements) {

        Map<String, Map<Direction, List<Power>>> powerMap = new HashMap<>();
        Map<Direction, List<Power>> groupA = new HashMap<>();
        groupA.put(Direction.IN, new ArrayList<>());
        groupA.put(Direction.OUT, new ArrayList<>());

        // generalize to all options for direction
        Map<Direction, List<Power>> groupB = new HashMap<>();
        groupB.put(Direction.IN, new ArrayList<>());
        groupB.put(Direction.OUT, new ArrayList<>());

        powerMap.put("group_a", groupA);
        powerMap.put("group_b", groupB);

        for (Key key : measurements.keySet()) {
            Measurement m = measurements.get(key);
            powerMap.get(m.getDeviceGroup()).get(m.getDirection()).addAll(m.getPower());
        }

        printTotalPower(powerMap);
    }

    private Power calcTotalPower(Map<String, Map<Direction, List<Power>>> powerMap, String groupName, Direction direction) {
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

    private void printTotalPower(Map<String, Map<Direction, List<Power>>> powerMap) {
        Power totalPowerGroupADirectionIn = calcTotalPower(powerMap, "group_a", Direction.IN);
        Power totalPowerGroupADirectionOut = calcTotalPower(powerMap, "group_a", Direction.OUT);
        Power totalPowerGroupBDirectionIn = calcTotalPower(powerMap, "group_b", Direction.IN);
        Power totalPowerGroupBDirectionOut = calcTotalPower(powerMap, "group_b", Direction.OUT);

        System.out.println("Group A: ");
        System.out.println("IN:");
        System.out.println("\tAVG: " + String.format("%.4f", totalPowerGroupADirectionIn.getAvg()));
        System.out.println("\tMIN: " + String.format("%.4f", totalPowerGroupADirectionIn.getMin()));
        System.out.println("\tMAX: " + String.format("%.4f", totalPowerGroupADirectionIn.getMax()));
        System.out.println("OUT:");
        System.out.println("\tAVG: " + String.format("%.4f", totalPowerGroupADirectionOut.getAvg()));
        System.out.println("\tMIN: " + String.format("%.4f", totalPowerGroupADirectionOut.getMin()));
        System.out.println("\tMAX: " + String.format("%.4f", totalPowerGroupADirectionOut.getMax()));

        System.out.println("");
        System.out.println("Group B: ");
        System.out.println("IN:");
        System.out.println("\tAVG: " + String.format("%.4f", totalPowerGroupBDirectionIn.getAvg()));
        System.out.println("\tMIN: " + String.format("%.4f", totalPowerGroupBDirectionIn.getMin()));
        System.out.println("\tMAX: " + String.format("%.4f", totalPowerGroupBDirectionIn.getMax()));
        System.out.println("OUT:");
        System.out.println("\tAVG: " + String.format("%.4f", totalPowerGroupBDirectionOut.getAvg()));
        System.out.println("\tMIN: " + String.format("%.4f", totalPowerGroupBDirectionOut.getMin()));
        System.out.println("\tMAX: " + String.format("%.4f", totalPowerGroupBDirectionOut.getMax()));
    }
}
