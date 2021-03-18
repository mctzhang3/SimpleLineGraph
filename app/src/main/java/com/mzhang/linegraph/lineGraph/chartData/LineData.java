package com.mzhang.linegraph.lineGraph.chartData;

import java.util.List;

/**
 * Data object that encapsulates all data associated with a LineChart.
 *
 */

public class LineData extends BaseLineData<ILineDataSet> {

    public LineData(List<ILineDataSet> dataSets) {
        super(dataSets);
    }
}
