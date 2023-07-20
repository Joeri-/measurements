package abb.interview.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PowerMapTest {

    PowerMap powerMap;
    Measurement measurementAIn1, measurementAIn2;
    Measurement measurementAOut1;
    Measurement measurementBIn1, measurementBIn2, measurementBIn3, measurementBIn4;

    @BeforeEach
    void setup() {
        measurementAIn1 = measurementBuilder(DeviceGroup.GROUP_A, Direction.IN, 1,
                List.of( new Power(100.0, 200.0, 300.0, 1L)));

        measurementAIn2 = measurementBuilder(DeviceGroup.GROUP_A, Direction.IN, 2,
                List.of( new Power(50.0, 100.0, 150.0, 1L)));

        measurementAOut1 = measurementBuilder(DeviceGroup.GROUP_A, Direction.OUT, 1,
                List.of(new Power(10.0, 20.0, 30.0, 1L)));

        measurementBIn1 = measurementBuilder(DeviceGroup.GROUP_B, Direction.IN, 1,
                List.of(new Power(1.0, 2.0, 3.0, 1L)));

        measurementBIn2 = measurementBuilder(DeviceGroup.GROUP_B, Direction.IN, 2,
                List.of(new Power(100.0, 200.0, 300.0, 1L)));

        measurementBIn3 = measurementBuilder(DeviceGroup.GROUP_B, Direction.IN, 3,
                List.of(new Power(10.0, 20.0, 30.0, 1L)));

        measurementBIn4 = measurementBuilder(DeviceGroup.GROUP_B, Direction.IN, 4,
                List.of(new Power(1000.0, 2000.0, 3000.0, 1L)));

        // 2x A - IN
        // 1x A - OUT
        // 4x B - IN
        // 0x B - OUT (check for edge cases)
        Measurements measurements = new Measurements();
        measurements.put(measurementAIn1.getKey(), measurementAIn1);
        measurements.put(measurementAIn2.getKey(), measurementAIn2);
        measurements.put(measurementAOut1.getKey(), measurementAOut1);
        measurements.put(measurementBIn1.getKey(), measurementBIn1);
        measurements.put(measurementBIn2.getKey(), measurementBIn2);
        measurements.put(measurementBIn3.getKey(), measurementBIn3);
        measurements.put(measurementBIn4.getKey(), measurementBIn4);

        powerMap = new PowerMap();
        powerMap.parseMeasurements(measurements);
    }

    @Test
    void parseMeasurements_shouldFillPowerMap() {

        assertAll(
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_A).get(Direction.IN).contains(measurementAIn1)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_A).get(Direction.IN).contains(measurementAIn2)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_A).get(Direction.OUT).contains(measurementAOut1)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_B).get(Direction.IN).contains(measurementBIn1)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_B).get(Direction.IN).contains(measurementBIn2)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_B).get(Direction.IN).contains(measurementBIn3)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_B).get(Direction.IN).contains(measurementBIn4)),
                () -> assertTrue(powerMap.get(DeviceGroup.GROUP_B).get(Direction.OUT).isEmpty())
        );
    }

    @Test
    void calcTotalPower_shouldReturnTotalPowerObject() {
        Power totalPowerInA = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_A, Direction.IN);
        Power totalPowerOutA = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_A, Direction.OUT);
        Power totalPowerInB = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_B, Direction.IN);
        Power totalPowerOutB = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_B, Direction.OUT);

        assertAll(
                () -> assertEquals(150.0, totalPowerInA.getMin()),
                () -> assertEquals(300.0, totalPowerInA.getMax()),
                () -> assertEquals(450.0, totalPowerInA.getAvg()),
                () -> assertEquals(10.0, totalPowerOutA.getMin()),
                () -> assertEquals(20.0, totalPowerOutA.getMax()),
                () -> assertEquals(30.0, totalPowerOutA.getAvg()),
                () -> assertEquals(1111.0, totalPowerInB.getMin()),
                () -> assertEquals(2222.0, totalPowerInB.getMax()),
                () -> assertEquals(3333.0, totalPowerInB.getAvg()),
                () -> assertEquals(0, totalPowerOutB.getMin()),
                () -> assertEquals(0, totalPowerOutB.getMax()),
                () -> assertEquals(0, totalPowerOutB.getAvg())
        );
    }

    @Test
    void constructSortedMaxPowerDevicesPerGroupAndDirection_shouldReturnCorrectlySortedList() {
        List<PrintableDeviceWithMaxPower> sortedList = powerMap.constructSortedMaxPowerDevicesPerGroupAndDirection();

        assertAll(
                () -> assertEquals(7, sortedList.size()),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementAIn2), sortedList.get(0)),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementAIn1), sortedList.get(1)),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementAOut1), sortedList.get(2)),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementBIn1), sortedList.get(3)),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementBIn3), sortedList.get(4)),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementBIn2), sortedList.get(5)),
                () -> assertEquals(new PrintableDeviceWithMaxPower(measurementBIn4), sortedList.get(6))
        );
    }

    Measurement measurementBuilder(DeviceGroup deviceGroup, Direction direction, int index, List<Power> power) {
        var measurement = new Measurement();
        String name = String.valueOf(deviceGroup) + direction + index;
        measurement.setResourceId("resource_" + name);
        measurement.setDeviceName("device_" + name);
        measurement.setDeviceGroup(deviceGroup);
        measurement.setDirection(direction);
        measurement.setPower(power);
        return measurement;
    }
}