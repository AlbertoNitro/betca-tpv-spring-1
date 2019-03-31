package es.upm.miw.business_controllers.stock_prediction;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static es.upm.miw.business_controllers.stock_prediction.MapToPointArrayConverter.convertToPointArray;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

class MapToPointArrayConverterTest {

    @Test
    void testConvertToPointArray() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1028);
        map.put("b", 964);
        map.put("c", 900);
        map.put("d", 837);

        double[][] points = convertToPointArray(map);
        assertThat(points[0][0], is(1d));
        assertThat(points[1][0], is(2d));
        assertThat(points[2][0], is(3d));
        assertThat(points[3][0], is(4d));
    }
}
