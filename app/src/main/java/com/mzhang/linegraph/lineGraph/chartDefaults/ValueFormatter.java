package com.mzhang.linegraph.lineGraph.chartDefaults;

import com.mzhang.linegraph.lineGraph.dataPoints.Entry;
import com.mzhang.linegraph.lineGraph.xyAxes.AxisBase;

/**
 * Class to format all values before they are drawn as labels.
 */
public abstract class ValueFormatter{

    /**
     * Called when drawing any label, used to change numbers into formatted strings.
     *
     * @param value float to be formatted
     * @return formatted string label
     */
    public String getFormattedValue(float value) {
        return String.valueOf(value);
    }

    /**
     * Used to draw axis labels, calls {@link #getFormattedValue(float)} by default.
     *
     * @param value float to be formatted
     * @param axis  axis being labeled
     * @return formatted string label
     */
    public String getAxisLabel(float value, AxisBase axis) {
        return getFormattedValue(value);
    }


    /**
     * Used to draw line and scatter labels, calls {@link #getFormattedValue(float)} by default.
     *
     * @param entry point being labeled, contains X value
     * @return formatted string label
     */
    public String getPointLabel(Entry entry) {
        return getFormattedValue(entry.getY());
    }

}

