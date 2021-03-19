package com.mzhang.linegraph;


//import com.mzhang.linegraph.lineGraph.chartDefaults.ValueFormatter;
//import com.mzhang.linegraph.lineGraph.xyAxes.AxisBase;
//import com.mzhang.linegraph.lineGraph.xyAxes.XAxis;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class LineChartLibValueFormatter extends ValueFormatter {

    private final DecimalFormat mFormat;

    public LineChartLibValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + " $";
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return getFormattedValue(value);
        } else {
            return mFormat.format(value);
        }
    }

}
