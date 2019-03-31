package es.upm.miw.business_controllers.stock_prediction;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.security.InvalidParameterException;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class StockPredictionAlgorithm {
    private Integer stock;
    private double[][] observationData;
    private SimpleRegression simpleRegressionFunction;

    public StockPredictionAlgorithm(Integer stock, double[][] observationData) {
        this.stock = stock;
        this.observationData = observationData;
        this.simpleRegressionFunction = simpleRegressionFunction(observationData);
    }

    public int predict(int periodToPredict) {
        int observatedPeriods = observationData.length;
        if (periodToPredict <= observatedPeriods) {
            throw new InvalidParameterException("El periodo a predecir no deberia ser inferior al numero de periodos suministrado en los datos observados");
        }
        int accumulatedPrediction = (int) rangeStream(observatedPeriods, periodToPredict)
                .mapToDouble(period -> abs(simpleRegressionFunction.predict(period))).sum();
        return stock - accumulatedPrediction;
    }

    private IntStream rangeStream(int from, int to) {
        return IntStream.range(from + 1, to + 1);
    }

    private SimpleRegression simpleRegressionFunction(double[][] points) {
        SimpleRegression simpleRegression = new SimpleRegression();
        simpleRegression.addData(points);
        return simpleRegression;
    }

}