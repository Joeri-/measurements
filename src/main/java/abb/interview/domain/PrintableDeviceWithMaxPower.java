package abb.interview.domain;

import java.util.Locale;
import java.util.Objects;

public class PrintableDeviceWithMaxPower {

    private final String deviceId;
    private final DeviceGroup deviceGroup;
    private final Direction direction;
    private final double maxPower;


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
                ", maxPower=" + String.format(Locale.US, "%.4f", maxPower) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintableDeviceWithMaxPower that = (PrintableDeviceWithMaxPower) o;
        return Double.compare(that.maxPower, maxPower) == 0 && Objects.equals(deviceId, that.deviceId) && deviceGroup == that.deviceGroup && direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, deviceGroup, direction, maxPower);
    }
}
