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
package org.ta4j.core.indicators.volume;

import org.junit.Before;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.mocks.MockBar;
import org.ta4j.core.mocks.MockTimeSeries;

import java.util.ArrayList;
import java.util.List;

import static org.ta4j.core.TATestsUtils.assertNumEquals;

public class ROCVIndicatorTest {

	TimeSeries series;

    @Before
    public void setUp() {
    		List<Bar> bars = new ArrayList<Bar>();
        bars.add(new MockBar(1355.69, 1000));
        bars.add(new MockBar(1325.51, 3000));
        bars.add(new MockBar(1335.02, 3500));
        bars.add(new MockBar(1313.72, 2200));
        bars.add(new MockBar(1319.99, 2300));
        bars.add(new MockBar(1331.85, 200));
        bars.add(new MockBar(1329.04, 2700));
        bars.add(new MockBar(1362.16, 5000));
        bars.add(new MockBar(1365.51, 1000));
        bars.add(new MockBar(1374.02, 2500));
        series = new MockTimeSeries(bars);
    }

    @Test
    public void test() {
        ROCVIndicator roc = new ROCVIndicator(series, 3);

        assertNumEquals(roc.getValue(0), 0);
        assertNumEquals(roc.getValue(1), 200);
        assertNumEquals(roc.getValue(2), 250);
        assertNumEquals(roc.getValue(3), 120);
        assertNumEquals(roc.getValue(4), -23.333333333333332);
        assertNumEquals(roc.getValue(5), -94.28571428571429);
        assertNumEquals(roc.getValue(6), 22.727272727272727);
        assertNumEquals(roc.getValue(7), 117.3913043478261);
        assertNumEquals(roc.getValue(8), 400);
        assertNumEquals(roc.getValue(9), -7.407407407407407);
    }
}
