package com.mzhang.linegraph.lineGraph.chartData;

import android.graphics.Color;

import com.mzhang.linegraph.lineGraph.dataPoints.Entry;

import java.util.List;

/**
 * Baseclass of Line DataSets.
 *
 */
public abstract class BaseLineDataSet<T extends Entry>
        extends DataSet<T> implements IBaseLineDataSet<T> {

    /**
     * default highlight color
     */
    protected int mHighLightColor = Color.rgb(255, 187, 115);

    public BaseLineDataSet(List<T> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getHighLightColor() {
        return mHighLightColor;
    }

    protected void copy(BaseLineDataSet baseLineDataSet) {
        super.copy(baseLineDataSet);
        baseLineDataSet.mHighLightColor = mHighLightColor;
    }
}

