package abb.interview.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PowerMapTest {

    Measurement measurementGroupADirectionIn;
    Measurement measurementGroupADirectionOut;
    Measurement measurementGroupBDirectionIn;
    Measurement measurementGroupBDirectionOut;

    @BeforeEach
    void setup() {
        measurementGroupADirectionIn = new Measurement();
        measurementGroupADirectionIn.setPower(List.of(new Power(100.0, 200.0, 300.0, 1L)));
        measurementGroupADirectionOut = new Measurement();
        measurementGroupADirectionOut.setPower(List.of(new Power(10.0, 20.0, 30.0, 1L)));
        measurementGroupBDirectionIn = new Measurement();
        measurementGroupBDirectionIn.setPower(List.of(new Power(11.0, 22.0, 33.0, 1L)));
        measurementGroupBDirectionOut = new Measurement();
        measurementGroupBDirectionOut.setPower(List.of(new Power(1111.0, 2222.0, 3333.0, 1L)));
    }
    @Test
    void calcTotalPower_shouldReturnTotalPowerObject() {
        PowerMap powerMap = new PowerMap();
        Map<Direction, List<Measurement>> groupAPowerList = new HashMap<>();
        Map<Direction, List<Measurement>> groupBPowerList = new HashMap<>();

        groupAPowerList.put(Direction.IN, List.of(measurementGroupADirectionIn, measurementGroupADirectionIn));
        groupAPowerList.put(Direction.OUT, List.of(measurementGroupADirectionOut, measurementGroupADirectionOut));

        groupBPowerList.put(Direction.IN, List.of(measurementGroupBDirectionIn, measurementGroupBDirectionIn));
        groupBPowerList.put(Direction.OUT, List.of(measurementGroupBDirectionOut, measurementGroupBDirectionOut));

        powerMap.put(DeviceGroup.GROUP_A, groupAPowerList);
        powerMap.put(DeviceGroup.GROUP_B, groupBPowerList);

        Power totalPowerInA = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_A, Direction.IN);
        Power totalPowerOutA = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_A, Direction.OUT);
        Power totalPowerInB = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_B, Direction.IN);
        Power totalPowerOutB = powerMap.calcTotalPowerPerGroupAndDirection(DeviceGroup.GROUP_B, Direction.OUT);

        assertAll(
                () -> assertEquals(200.0, totalPowerInA.getMin()),
                () -> assertEquals(400.0, totalPowerInA.getMax()),
                () -> assertEquals(600.0, totalPowerInA.getAvg()),
                () -> assertEquals(20.0, totalPowerOutA.getMin()),
                () -> assertEquals(40.0, totalPowerOutA.getMax()),
                () -> assertEquals(60.0, totalPowerOutA.getAvg()),
                () -> assertEquals(22.0, totalPowerInB.getMin()),
                () -> assertEquals(44.0, totalPowerInB.getMax()),
                () -> assertEquals(66.0, totalPowerInB.getAvg()),
                () -> assertEquals(2222.0, totalPowerOutB.getMin()),
                () -> assertEquals(4444.0, totalPowerOutB.getMax()),
                () -> assertEquals(6666.0, totalPowerOutB.getAvg())
        );
    }
}