/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Marc de Verdelhan & respective authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ta4jexamples.strategies;

import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.Trade;
import eu.verdelhan.ta4j.analysis.criteria.TotalProfitCriterion;
import java.util.List;
import ta4jexamples.loaders.CsvTradesLoader;

/**
 * Strategies which compares current price to global extrema over a week.
 */
public class GlobalExtremaStrategy {

    // We assume that there were at least one trade every 5 minutes during the whole week
    private static final int NB_TICKS_PER_WEEK = 12 * 24 * 7;

    /**
     * @param series a time series
     * @return a global extrema strategy
     */
    public static Strategy buildStrategy(TimeSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

//        ClosePriceIndicator closePrices = new ClosePriceIndicator(series);
//
//        // Getting the max price over the past week
//        MaxPriceIndicator maxPrices = new MaxPriceIndicator(series);
//        HighestValueIndicator weekMaxPrice = new HighestValueIndicator(maxPrices, NB_TICKS_PER_WEEK);
//        // Getting the min price over the past week
//        MinPriceIndicator minPrices = new MinPriceIndicator(series);
//        LowestValueIndicator weekMinPrice = new LowestValueIndicator(minPrices, NB_TICKS_PER_WEEK);
//
//        // Going long if the close price goes below the min price
//        MultiplierIndicator upWeek = new MultiplierIndicator(weekMinPrice, Decimal.valueOf("1.004"));
//        IndicatorOverIndicatorStrategy buySignal = new IndicatorOverIndicatorStrategy(upWeek, closePrices);
//
//        // Going short if the close price goes above the max price
//        MultiplierIndicator downWeek = new MultiplierIndicator(weekMaxPrice, Decimal.valueOf("0.996"));
//        IndicatorOverIndicatorStrategy sellSignal = new IndicatorOverIndicatorStrategy(closePrices, downWeek);
//
//        Strategy signals = new CombinedEntryAndExitStrategy(buySignal, sellSignal);
//
//        return signals;
        return new Strategy();
    }

    public static void main(String[] args) {

        // Getting the time series
        TimeSeries series = CsvTradesLoader.loadBitstampSeries();

        // Building the trading strategy
        Strategy strategy = buildStrategy(series);

        // Running the strategy
        List<Trade> trades = series.run(strategy).getTrades();
        System.out.println("Number of trades for the strategy: " + trades.size());

        // Analysis
        System.out.println("Total profit for the strategy: " + new TotalProfitCriterion().calculate(series, trades));
    }
}
