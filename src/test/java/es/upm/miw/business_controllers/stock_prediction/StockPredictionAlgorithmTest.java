package es.upm.miw.business_controllers.stock_prediction;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

class StockPredictionAlgorithmTest {

    @Test
    void testStockPredictionAlgorithm() {
        double[][] points = {{1, 1028}, {2, 964}, {3, 900}, {4, 837}};
        StockPredictionAlgorithm algorithm = new StockPredictionAlgorithm(1000, points);
        assertThat(algorithm.predict(5), is(227));
        assertThat(algorithm.predict(6), is(-482));
        assertThat(algorithm.predict(7), is(-1127));
        assertThat(algorithm.predict(50), is(-39532));
    }
}
