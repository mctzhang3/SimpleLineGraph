package com.mzhang.linegraph.lineGraph.chartData;

import com.mzhang.linegraph.lineGraph.dataPoints.Entry;

public interface IBaseLineDataSet<T extends Entry> extends IDataSet<T> {

    /**
     * Returns the color that is used for drawing the highlight indicators.
     *
     * @return
     */
    int getHighLightColor();
}

