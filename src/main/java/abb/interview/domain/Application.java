package abb.interview.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class Application {

    private final Measurements measurements = new Measurements();
    private static final String MEASUREMENTS_FILE = "measurements.json";

    public static void main(String[] args) throws IOException {
        Application app = new Application();

        List<Measurement> measurements = app.getFileFromResourceAsStream(MEASUREMENTS_FILE);

        for (Measurement measurement : measurements) {
            System.out.println(measurement.getResourceId());
        }
    }

    private List<Measurement> getFileFromResourceAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            System.out.println("File not found: " + fileName);
            return Collections.emptyList();
        } else {
            try {
                return new ObjectMapper().readValue(inputStream, new TypeReference<List<Measurement>>(){});
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                return Collections.emptyList();
            }
        }
    }
}
