package com.mzhang.linegraph.lineGraph.chartBase;

import android.content.Context;
import android.util.AttributeSet;

import com.mzhang.linegraph.lineGraph.chartData.LineData;
import com.mzhang.linegraph.lineGraph.chartData.LineDataProvider;
import com.mzhang.linegraph.lineGraph.chartRenderers.LineChartRenderer;

/**
 * Chart that draws lines.
 *
 */

public class LineChart extends LineChartBase<LineData> implements LineDataProvider {

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartRenderer(this, mAnimator, mChartDimens);
    }

    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof LineChartRenderer) {
            ((LineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
