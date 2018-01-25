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
package org.ta4j.core.indicators.helpers;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Num.Num;
import org.ta4j.core.TATestsUtils;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.mocks.MockTimeSeries;

import java.time.ZonedDateTime;

import static junit.framework.TestCase.assertEquals;
import static org.ta4j.core.Num.AbstractNum.NaN;

public class LowestValueIndicatorTest {

    private TimeSeries data;

    @Before
    public void setUp() {
        data = new MockTimeSeries(1, 2, 3, 4, 3, 4, 5, 6, 4, 3, 2, 4, 3, 1);
    }

    @Test
    public void lowestValueIndicatorUsingTimeFrame5UsingClosePrice() {
        LowestValueIndicator lowestValue = new LowestValueIndicator(new ClosePriceIndicator(data), 5);
        TATestsUtils.assertNumEquals(lowestValue.getValue(1), "1.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(2), "1.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(3), "1.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(4), "1.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(5), "2.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(6), "3.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(7), "3.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(8), "3.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(9), "3.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(10), "2.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(11), "2.0");
        TATestsUtils.assertNumEquals(lowestValue.getValue(12), "2.0");

    }

    @Test
    public void lowestValueIndicatorValueShouldBeEqualsToFirstDataValue() {
        LowestValueIndicator lowestValue = new LowestValueIndicator(new ClosePriceIndicator(data), 5);
        TATestsUtils.assertNumEquals(lowestValue.getValue(0), "1.0");
    }

    @Test
    public void lowestValueIndicatorWhenTimeFrameIsGreaterThanIndex() {
        LowestValueIndicator lowestValue = new LowestValueIndicator(new ClosePriceIndicator(data), 500);
        TATestsUtils.assertNumEquals(lowestValue.getValue(12), "1.0");
    }

    @Test
    public void onlyNaNValues(){
        BaseTimeSeries series = new BaseTimeSeries("NaN test");
        for (long i = 0; i<= 10000; i++){
            series.addBar(ZonedDateTime.now().plusDays(i),NaN,NaN,NaN,NaN,NaN);
        }


        LowestValueIndicator lowestValue = new LowestValueIndicator(new ClosePriceIndicator(series), 5);
        for (int i = series.getBeginIndex(); i<= series.getEndIndex(); i++){
            assertEquals(NaN.toString(),lowestValue.getValue(i).toString());
        }
    }

    @Test
    public void naNValuesInIntervall(){
        BaseTimeSeries series = new BaseTimeSeries("NaN test");
        for (long i = 0; i<= 10; i++){ // (NaN, 1, NaN, 2, NaN, 3, NaN, 4, ...)
            Num closePrice = i % 2 == 0 ?series.valueOf(i):NaN;
            series.addBar(ZonedDateTime.now().plusDays(i),NaN,NaN,NaN,NaN,NaN);
        }


        LowestValueIndicator lowestValue = new LowestValueIndicator(new ClosePriceIndicator(series), 2);
        for (int i = series.getBeginIndex(); i<= series.getEndIndex(); i++){
            if (i % 2 != 0){
                assertEquals(series.getBar(i-1).getClosePrice().toString(),lowestValue.getValue(i).toString());
            } else
            assertEquals(series.getBar(Math.max(0,i-1)).getClosePrice().toString(),lowestValue.getValue(i).toString());
        }
    }
}
