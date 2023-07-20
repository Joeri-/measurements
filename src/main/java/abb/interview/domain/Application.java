package abb.interview.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Application {

    private static final Measurements measurements = new Measurements();
    private static final String MEASUREMENTS_FILE = "measurements.json";

    public static void main(String[] args) {
        Application app = new Application();

        List<Measurement> measurementList = app.readMeasurementsFromFile(MEASUREMENTS_FILE);

        // Task 1
        for (Measurement measurement : measurementList) {
            Key key = new Key(measurement.getResourceId(), measurement.getDeviceName(), measurement.getDeviceGroup().toString());
            measurements.put(key, measurement);
        }

        System.out.println("\nTask 1:");
        System.out.println("---------");
        System.out.println("\nMeasurements loaded successfully.");

        PowerMap powerMap = new PowerMap();
        powerMap.parseMeasurements(measurements);

        // Task 2
        System.out.println("\nTask 2:");
        System.out.println("---------");
        powerMap.printTotalPowerPerGroupAndDirection();

        // Task 3
        System.out.println("\nTask 3:");
        System.out.println("---------\n");
        powerMap.printSortedMaxPowerDevicesPerGroupAndDirection();
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
}
