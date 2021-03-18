package com.mzhang.linegraph.lineGraph.chartRenderers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

import com.mzhang.linegraph.lineGraph.chartAnimation.ChartAnimator;
import com.mzhang.linegraph.lineGraph.chartBase.ChartInterface;
import com.mzhang.linegraph.lineGraph.chartData.IDataSet;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartDimens;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartUtils;

/**
 * Superclass of LineData Renderer
 *
 */
public abstract class DataRenderer extends BaseRenderer {

    /**
     * the animator object used to perform animations on the chart data
     */
    protected ChartAnimator mAnimator;

    /**
     * main paint object used for rendering
     */
    protected Paint mRenderPaint;

    /**
     * paint used for highlighting values
     */
    protected Paint mHighlightPaint;

    protected Paint mDrawPaint;

    /**
     * paint object for drawing values (text representing values of chart
     * entries)
     */
    protected Paint mValuePaint;

    public DataRenderer(ChartAnimator animator, ChartDimens chartDimens) {
        super(chartDimens);
        this.mAnimator = animator;

        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Style.FILL);

        mDrawPaint = new Paint(Paint.DITHER_FLAG);

        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValuePaint.setColor(Color.rgb(63, 63, 63));
        mValuePaint.setTextAlign(Align.CENTER);
        mValuePaint.setTextSize(ChartUtils.convertDpToPixel(9f));

        mHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setStyle(Style.STROKE);
        mHighlightPaint.setStrokeWidth(2f);
        mHighlightPaint.setColor(Color.rgb(255, 187, 115));
    }

    protected boolean isDrawingValuesAllowed(ChartInterface chart) {
        return chart.getData().getEntryCount() < chart.getMaxVisibleCount()
                * mChartDimens.getScaleX();
    }

    /**
     * Applies the required styling (provided by the DataSet) to the value-paint
     * object.
     *
     * @param set
     */
    protected void applyValueTextStyle(IDataSet set) {

        mValuePaint.setTypeface(set.getValueTypeface());
        mValuePaint.setTextSize(set.getValueTextSize());
    }

    /**
     * Initializes the buffers used for rendering with a new size. Since this
     * method performs memory allocations, it should only be called if
     * necessary.
     */
    public abstract void initBuffers();

    /**
     * Draws the actual data in form of lines, bars, ... depending on Renderer subclass.
     *
     * @param c
     */
    public abstract void drawData(Canvas c);

    /**
     * Loops over all Entrys and draws their values.
     *
     * @param c
     */
    public abstract void drawValues(Canvas c);

    /**
     * Draws the value of the given entry by using the provided IValueFormatter.
     *
     * @param c         canvas
     * @param valueText label to draw
     * @param x         position
     * @param y         position
     * @param color
     */
    public abstract void drawValue(Canvas c, String valueText, float x, float y, int color);
}

