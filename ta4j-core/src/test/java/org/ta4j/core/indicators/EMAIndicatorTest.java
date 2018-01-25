/*
  The MIT License (MIT)

  Copyright (c) 2014-2017 Marc de Verdelhan, Ta4j Organization & respective authors (see AUTHORS)

  Permission is hereby granted, free of charge, to any person obtaining a copy of
  this software and associated documentation files (the "Software"), to deal in
  the Software without restriction, including without limitation the rights to
  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  the Software, and to permit persons to whom the Software is furnished to do so,
  subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core.indicators;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.*;
import org.ta4j.core.Num.Num;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockTimeSeries;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.ta4j.core.TATestsUtils.assertIndicatorEquals;
import static org.ta4j.core.TATestsUtils.assertNumEquals;

public class EMAIndicatorTest extends IndicatorTest<Indicator<Num>, Num> {

    private ExternalIndicatorTest xls;

    public EMAIndicatorTest() throws Exception {
        super((data, params) -> new EMAIndicator((Indicator<Num>) data, (int) params[0]));
        xls = new XLSIndicatorTest(this.getClass(), "EMA.xls", 6);
    }
    private TimeSeries data;

    @Before
    public void setUp() {
        data = new MockTimeSeries(
                64.75, 63.79, 63.73,
                63.73, 63.55, 63.19,
                63.91, 63.85, 62.95,
                63.37, 61.33, 61.51);
    }

    @Test
    public void firstValueShouldBeEqualsToFirstDataValue() throws Exception {
        Indicator<Num> indicator = getIndicator(new ClosePriceIndicator(data), 1);
        assertNumEquals(indicator.getValue(0), 64.75);
    }

    @Test
    public void usingTimeFrame10UsingClosePrice() throws Exception {
        Indicator<Num> indicator = getIndicator(new ClosePriceIndicator(data), 10);
        assertNumEquals(indicator.getValue(9), 63.6948);
        assertNumEquals(indicator.getValue(10), 63.2648);
        assertNumEquals(indicator.getValue(11), 62.9457);
    }

    @Test
    public void stackOverflowError() throws Exception {
        List<Bar> bigListOfBars = new ArrayList<Bar>();
        for (int i = 0; i < 10000; i++) {
            bigListOfBars.add(new MockBar(i));
        }
        MockTimeSeries bigSeries = new MockTimeSeries(bigListOfBars);
        Indicator<Num> indicator = getIndicator(new ClosePriceIndicator(bigSeries), 10);
        // if a StackOverflowError is thrown here, then the RecursiveCachedIndicator does not work as intended.
        assertNumEquals(indicator.getValue(9999), 9994.5);
    }

    @Test
    public void externalData() throws Exception {
        TimeSeries xlsSeries = xls.getSeries();
        Indicator<Num> closePrice = new ClosePriceIndicator(xlsSeries);
        Indicator<Num> indicator;

        indicator = getIndicator(closePrice, 1);
        assertIndicatorEquals(xls.getIndicator(1), indicator);
        assertEquals(329.0, indicator.getValue(indicator.getTimeSeries().getEndIndex()).doubleValue(), TATestsUtils.BIG_DECIMAL_OFFSET);

        indicator = getIndicator(closePrice, 3);
        assertIndicatorEquals(xls.getIndicator(3), indicator);
        assertEquals(327.7748, indicator.getValue(indicator.getTimeSeries().getEndIndex()).doubleValue(), TATestsUtils.BIG_DECIMAL_OFFSET);

        indicator = getIndicator(closePrice, 13);
        assertIndicatorEquals(xls.getIndicator(13), indicator);
        assertEquals(327.4076, indicator.getValue(indicator.getTimeSeries().getEndIndex()).doubleValue(), TATestsUtils.BIG_DECIMAL_OFFSET);
    }

}
