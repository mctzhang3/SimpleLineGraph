package com.mzhang.linegraph.lineGraph.labelFormatters;

import com.mzhang.linegraph.lineGraph.chartDefaults.ValueFormatter;
import com.mzhang.linegraph.lineGraph.xyAxes.AxisBase;
import com.mzhang.linegraph.lineGraph.xyAxes.XAxis;

import java.text.DecimalFormat;

public class CustomValueFormatter extends ValueFormatter
{

    private final DecimalFormat mFormat;
    private String suffix;

    public CustomValueFormatter(String suffix) {
        mFormat = new DecimalFormat("###,###,###,##0.00");
        this.suffix = suffix;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + suffix;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value) + suffix;
        } else {
            return mFormat.format(value);
        }
    }
}

