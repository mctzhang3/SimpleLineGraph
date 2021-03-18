package com.mzhang.linegraph.lineGraph.chartData;

import java.util.List;

/**
 * Baseclass for Line data.
 *
 */
public abstract class BaseLineData<T extends ILineDataSet>
        extends ChartData<T> {

    public BaseLineData(List<T> sets) {
        super(sets);
    }
}

