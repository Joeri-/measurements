package abb.interview.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PrintableDeviceWithMaxPower {

    private String deviceId;
    private DeviceGroup deviceGroup;
    private Direction direction;
    private double maxPower;


    public PrintableDeviceWithMaxPower(Measurement measurement) {
        this.deviceId = measurement.getResourceId().replace("-", "");
        this.deviceGroup = measurement.getDeviceGroup();
        this.direction = measurement.getDirection();
        this.maxPower = measurement.getMaxPowerOfDevice().orElse(0);
    }

    public double getMaxPower() {
        return maxPower;
    }

    @Override
    public String toString() {
        return "{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceGroup=" + deviceGroup +
                ", direction=" + direction +
                ", maxPower=" + String.format("%.4f", maxPower) +
                '}';
    }
}
