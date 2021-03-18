package com.mzhang.linegraph.lineGraph.chartData;


import com.mzhang.linegraph.lineGraph.chartBase.ChartInterface;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartTransformer;
import com.mzhang.linegraph.lineGraph.xyAxes.YAxis;

public interface LineDataProvider extends ChartInterface {

    ChartTransformer getTransformer(YAxis.AxisDependency axis);
    LineData getLineData();

    float getLowestVisibleX();
    float getHighestVisibleX();

    BaseLineData getData();
}

