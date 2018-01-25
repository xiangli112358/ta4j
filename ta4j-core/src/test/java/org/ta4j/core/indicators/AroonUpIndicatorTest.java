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
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.Num.Num;
import org.ta4j.core.TATestsUtils;
import org.ta4j.core.TimeSeries;

import java.time.ZonedDateTime;

import static junit.framework.TestCase.assertEquals;
import static org.ta4j.core.Num.AbstractNum.NaN;
import static org.ta4j.core.TATestsUtils.assertNumEquals;


public class AroonUpIndicatorTest {

    private TimeSeries data;

    @Before
    public void init() {
        data = new BaseTimeSeries();
        data.addBar(ZonedDateTime.now().plusDays(1),168.28, 169.87, 167.15, 169.64, 0);
        data.addBar(ZonedDateTime.now().plusDays(2),168.84, 169.36, 168.2, 168.71, 0);
        data.addBar(ZonedDateTime.now().plusDays(3),168.88, 169.29, 166.41, 167.74, 0);
        data.addBar(ZonedDateTime.now().plusDays(4),168, 168.38, 166.18, 166.32, 0);
        data.addBar(ZonedDateTime.now().plusDays(5),166.89, 167.7, 166.33, 167.24, 0);
        data.addBar(ZonedDateTime.now().plusDays(6),165.25, 168.43, 165, 168.05, 0);
        data.addBar(ZonedDateTime.now().plusDays(7),168.17, 170.18, 167.63, 169.92, 0);
        data.addBar(ZonedDateTime.now().plusDays(8),170.42, 172.15, 170.06, 171.97, 0);
        data.addBar(ZonedDateTime.now().plusDays(9),172.41, 172.92, 171.31, 172.02, 0);
        data.addBar(ZonedDateTime.now().plusDays(10),171.2, 172.39, 169.55, 170.72, 0);
        data.addBar(ZonedDateTime.now().plusDays(11),170.91, 172.48, 169.57, 172.09, 0);
        data.addBar(ZonedDateTime.now().plusDays(12),171.8, 173.31, 170.27, 173.21, 0);
        data.addBar(ZonedDateTime.now().plusDays(13),173.09, 173.49, 170.8, 170.95, 0);
        data.addBar(ZonedDateTime.now().plusDays(14),172.41, 173.89, 172.2, 173.51, 0);
        data.addBar(ZonedDateTime.now().plusDays(15),173.87, 174.17, 175, 172.96, 0);
        data.addBar(ZonedDateTime.now().plusDays(16),173, 173.17, 172.06, 173.05, 0);
        data.addBar(ZonedDateTime.now().plusDays(17),172.26, 172.28, 170.5, 170.96, 0);
        data.addBar(ZonedDateTime.now().plusDays(18),170.88, 172.34, 170.26, 171.64, 0);
        data.addBar(ZonedDateTime.now().plusDays(19),171.85, 172.07, 169.34, 170.01, 0);
        data.addBar(ZonedDateTime.now().plusDays(20),170.75, 172.56, 170.36, 172.52, 0); // FB, daily, 9.19.'17

    }

    @Test
    public void upAndSlowDown(){
        AroonUpIndicator arronUp = new AroonUpIndicator(data, 5);
        TATestsUtils.assertNumEquals(arronUp.getValue(19),0);
        TATestsUtils.assertNumEquals(arronUp.getValue(18),20);
        TATestsUtils.assertNumEquals(arronUp.getValue(17),40);
        TATestsUtils.assertNumEquals(arronUp.getValue(16),60);
        TATestsUtils.assertNumEquals(arronUp.getValue(15),80);
        TATestsUtils.assertNumEquals(arronUp.getValue(14),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(13),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(12),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(11),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(10),60);
        TATestsUtils.assertNumEquals(arronUp.getValue(9),80);
        TATestsUtils.assertNumEquals(arronUp.getValue(8),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(7),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(6),100);
        TATestsUtils.assertNumEquals(arronUp.getValue(5),0);

    }


    @Test
    public void onlyNaNValues(){
        BaseTimeSeries series = new BaseTimeSeries("NaN test");
        for (long i = 0; i<= 1000; i++){
            series.addBar(ZonedDateTime.now().plusDays(i), NaN, NaN, NaN, NaN, NaN);
        }


        AroonUpIndicator aroonUpIndicator = new AroonUpIndicator(series, 5);
        for (int i = series.getBeginIndex(); i<= series.getEndIndex(); i++){
            assertEquals(NaN.toString(), aroonUpIndicator.getValue(i).toString());
        }
    }

    @Test
    public void naNValuesInIntervall(){
        BaseTimeSeries series = new BaseTimeSeries("NaN test");
        for (long i = 0; i<= 10; i++){ // (0, NaN, 2, NaN, 4, NaN, 6, NaN, 8, ...)
            Num maxPrice = i % 2 == 0 ? series.valueOf(i): NaN;
            series.addBar(ZonedDateTime.now().plusDays(i), NaN, maxPrice,NaN, NaN, NaN);
        }
        
        AroonUpIndicator aroonUpIndicator = new AroonUpIndicator(series, 5);
        for (int i = series.getBeginIndex(); i<= series.getEndIndex(); i++){
            if (i % 2 != 0){
                assertEquals(NaN.toString(), aroonUpIndicator.getValue(i).toString());
            } else {
                assertNumEquals(series.valueOf(100), aroonUpIndicator.getValue(i).toString());
            }
        }
    }
}
