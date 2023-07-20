package abb.interview.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@JsonRootName("measurement")
public class Measurement {
    @JsonProperty
    private String resourceId;
    @JsonProperty
    private String deviceName;
    @JsonProperty
    private DeviceGroup deviceGroup;
    @JsonProperty
    private Direction direction;
    @JsonProperty
    private List<Power> power = new ArrayList<>();

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceGroup getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(DeviceGroup deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Power> getPower() {
        return power;
    }

    public void setPower(List<Power> power) {
        this.power = power;
    }

    public Key getKey() {
        return new Key(resourceId,deviceName,deviceGroup.toString());
    }

    public OptionalDouble getMaxPowerOfDevice() {
        return this.getPower().stream()
                .map(Power::getMax)
                .mapToDouble(Double::doubleValue)
                .max();
    }
}
