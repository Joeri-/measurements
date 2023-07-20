package abb.interview.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    static final Application app = new Application();

    @Test
    void firstTest() {
        Map<DeviceGroup, Map<Direction, List<Power>>> powerMap = new HashMap<>();
        Map<Direction, List<Power>> groupADirectionInPowerList = new HashMap<>();

        groupADirectionInPowerList.put(Direction.IN, List.of(
                new Power(100.0, 200.0, 300.0, 1L),
                new Power(100.0, 200.0, 300.0, 1L))
        );

        powerMap.put(DeviceGroup.GROUP_A, groupADirectionInPowerList);

        Power totalPower = app.calcTotalPowerPerGroupAndDirection(powerMap, DeviceGroup.GROUP_A, Direction.IN);

        assertAll(
                () -> assertEquals(200.0, totalPower.getMin()),
                () -> assertEquals(400.0, totalPower.getMax()),
                () -> assertEquals(600.0, totalPower.getAvg())
        );
    }

}