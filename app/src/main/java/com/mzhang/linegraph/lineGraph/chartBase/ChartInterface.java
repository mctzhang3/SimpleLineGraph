package com.mzhang.linegraph.lineGraph.chartBase;

import com.mzhang.linegraph.lineGraph.chartData.ChartData;

/**
 * Interface that provides everything there is to know about the dimensions,
 * bounds, and range of the chart.
 *
 */
public interface ChartInterface {

    int getWidth();

    int getHeight();

    ChartData getData();

    int getMaxVisibleCount();

    /**
     * Returns the minimum y value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    float getYChartMin();

    /**
     * Returns the maximum y value of the chart, regardless of zoom or translation.
     *
     * @return
     */
    float getYChartMax();

}

