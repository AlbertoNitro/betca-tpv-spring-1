package es.upm.miw.business_controllers.stock_prediction;

import java.util.Map;

public class MapToPointArrayConverter {

    private MapToPointArrayConverter() {
        super();
    }

    public static double[][] convertToPointArray(Map<String, Integer> integerMap) {
        int i = 0;
        int rows = integerMap.entrySet().size();
        double[][] points = new double[rows][2];
        for (Map.Entry<String, Integer> entry : integerMap.entrySet()) {
            points[i] = new double[]{++i, entry.getValue().doubleValue()};
        }
        return points;
    }
}