package com.mzhang.linegraph.lineGraph.chartBase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.mzhang.linegraph.lineGraph.chartData.BaseLineData;
import com.mzhang.linegraph.lineGraph.chartData.IBaseLineDataSet;
import com.mzhang.linegraph.lineGraph.chartData.LineDataProvider;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartTransformer;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartUtils;
import com.mzhang.linegraph.lineGraph.dataPoints.Entry;
import com.mzhang.linegraph.lineGraph.dataPoints.PointDouble;
import com.mzhang.linegraph.lineGraph.dataPoints.PointFloat;
import com.mzhang.linegraph.lineGraph.xyAxes.XAxis;
import com.mzhang.linegraph.lineGraph.xyAxes.XAxisRenderer;
import com.mzhang.linegraph.lineGraph.xyAxes.YAxis;
import com.mzhang.linegraph.lineGraph.xyAxes.YAxisRenderer;

/**
 * Base-class of LineChart.
 *
 */
@SuppressLint("RtlHardcoded")
public abstract class LineChartBase<T extends BaseLineData<? extends
        IBaseLineDataSet<? extends Entry>>>
        extends Chart<T> implements LineDataProvider {

    /**
     * the maximum number of entries to which values will be drawn
     * (entry numbers greater than this value will cause value-labels to disappear)
     */
    protected int mMaxVisibleCount = 100;

    /**
     * paint object for the (by default) lightgrey background of the grid
     */
    protected Paint mGridBackgroundPaint;

    protected Paint mBorderPaint;

    /**
     * flag indicating if the grid background should be drawn or not
     */
    protected boolean mDrawGridBackground = false;

    protected boolean mDrawBorders = false;

    protected boolean mClipValuesToContent = false;

    protected boolean mClipDataToContent = true;

    /**
     * Sets the minimum offset (padding) around the chart, defaults to 15
     */
    protected float mMinOffset = 25.f;

    /**
     * the object representing the labels on the left y-axis
     */
    protected YAxis mAxisLeft;

    protected YAxisRenderer mAxisRendererLeft;

    protected ChartTransformer mLeftAxisTransformer;

    protected XAxisRenderer mXAxisRenderer;

    public LineChartBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LineChartBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChartBase(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        super.init();

        mAxisLeft = new YAxis(YAxis.AxisDependency.LEFT);

        mLeftAxisTransformer = new ChartTransformer(mChartDimens);

        mAxisRendererLeft = new YAxisRenderer(mChartDimens, mAxisLeft, mLeftAxisTransformer);

        mXAxisRenderer = new XAxisRenderer(mChartDimens, mXAxis, mLeftAxisTransformer);
        mGridBackgroundPaint = new Paint();
        mGridBackgroundPaint.setStyle(Style.FILL);
        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Style.STROKE);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setStrokeWidth(ChartUtils.convertDpToPixel(1f));
    }

    // for performance tracking
    private long totalTime = 0;
    private long drawCycles = 0;


    /**
     * Returns the maximum value this chart can display on it's y-axis.
     */
    public float getYChartMax() {
        return mAxisLeft.mAxisMaximum;
    }

    /**
     * Returns the minimum value this chart can display on it's y-axis.
     */
    public float getYChartMin() {
        return mAxisLeft.mAxisMinimum;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mData == null)
            return;

        long starttime = System.currentTimeMillis();

        // execute all drawing commands
        drawGridBackground(canvas);

        if (mAxisLeft.isEnabled())
            mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, mAxisLeft.isInverted());

        if (mXAxis.isEnabled())
            mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        mXAxisRenderer.renderAxisLine(canvas);
        mAxisRendererLeft.renderAxisLine(canvas);

        if (mXAxis.isDrawGridLinesBehindDataEnabled())
            mXAxisRenderer.renderGridLines(canvas);

        if (mAxisLeft.isDrawGridLinesBehindDataEnabled())
            mAxisRendererLeft.renderGridLines(canvas);

        int clipRestoreCount = canvas.save();

        if (isClipDataToContentEnabled()) {
            // make sure the data cannot be drawn outside the content-rect
            canvas.clipRect(mChartDimens.getContentRect());
        }

        mRenderer.drawData(canvas);

        if (!mXAxis.isDrawGridLinesBehindDataEnabled())
            mXAxisRenderer.renderGridLines(canvas);

        if (!mAxisLeft.isDrawGridLinesBehindDataEnabled())
            mAxisRendererLeft.renderGridLines(canvas);

        // Removes clipping rectangle
        canvas.restoreToCount(clipRestoreCount);

        mXAxisRenderer.renderAxisLabels(canvas);
        mAxisRendererLeft.renderAxisLabels(canvas);

        if (isClipValuesToContentEnabled()) {
            clipRestoreCount = canvas.save();
            canvas.clipRect(mChartDimens.getContentRect());

            mRenderer.drawValues(canvas);

            canvas.restoreToCount(clipRestoreCount);
        } else {
            mRenderer.drawValues(canvas);
        }

        // Disabling Legend
        //  mLegendRenderer.renderLegend(canvas);

        if (mLogEnabled) {
            long drawtime = (System.currentTimeMillis() - starttime);
            totalTime += drawtime;
            drawCycles += 1;
            long average = totalTime / drawCycles;
            Log.i(LOG_TAG, "Drawtime: " + drawtime + " ms, average: " + average + " ms, cycles: "
                    + drawCycles);
        }
    }

    protected void prepareValuePxMatrix() {

        if (mLogEnabled)
            Log.i(LOG_TAG, "Preparing Value-Px Matrix, xmin: " + mXAxis.mAxisMinimum + ", xmax: "
                    + mXAxis.mAxisMaximum + ", xdelta: " + mXAxis.mAxisRange);

      mLeftAxisTransformer.prepareMatrixValuePx(mXAxis.mAxisMinimum,
                mXAxis.mAxisRange,
                mAxisLeft.mAxisRange,
                mAxisLeft.mAxisMinimum);
    }

    protected void prepareOffsetMatrix() {
        mLeftAxisTransformer.prepareMatrixOffset(mAxisLeft.isInverted());
    }

    @Override
    public void notifyDataSetChanged() {

        if (mData == null) {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing... DATA NOT SET.");
            return;
        } else {
            if (mLogEnabled)
                Log.i(LOG_TAG, "Preparing...");
        }

        if (mRenderer != null)
            mRenderer.initBuffers();

        calcMinMax();

        mAxisRendererLeft.computeAxis(mAxisLeft.mAxisMinimum, mAxisLeft.mAxisMaximum, mAxisLeft.isInverted());
        mXAxisRenderer.computeAxis(mXAxis.mAxisMinimum, mXAxis.mAxisMaximum, false);

        if (mLegend != null)
            mLegendRenderer.computeLegend(mData);

        calculateOffsets();
    }

    /**
     * Performs auto scaling of the axis by recalculating the minimum and maximum y-values based on the entries currently in view.
     */
    protected void autoScale() {

        final float fromX = getLowestVisibleX();
        final float toX = getHighestVisibleX();

        mData.calcMinMaxY(fromX, toX);

        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        // calculate axis range (min / max) according to provided data

        if (mAxisLeft.isEnabled())
            mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.RIGHT),
                    mData.getYMax(YAxis.AxisDependency.RIGHT));

       calculateOffsets();
    }

    @Override
    protected void calcMinMax() {

        mXAxis.calculate(mData.getXMin(), mData.getXMax());

        // calculate axis range (min / max) according to provided data
        mAxisLeft.calculate(mData.getYMin(YAxis.AxisDependency.RIGHT), mData.getYMax(YAxis.AxisDependency.RIGHT));
    }

    protected void calculateLegendOffsets(RectF offsets) {

        offsets.left = 0.f;
        offsets.right = 0.f;
        offsets.top = 0.f;
        offsets.bottom = 0.f;

        if (mLegend == null || !mLegend.isEnabled() || mLegend.isDrawInsideEnabled())
            return;

        switch (mLegend.getOrientation()) {
            case VERTICAL:

                switch (mLegend.getHorizontalAlignment()) {
                    case LEFT:
                        offsets.left += Math.min(mLegend.mNeededWidth,
                                mChartDimens.getChartWidth() * mLegend.getMaxSizePercent())
                                + mLegend.getXOffset();
                        break;

                    case RIGHT:
                        offsets.right += Math.min(mLegend.mNeededWidth,
                                mChartDimens.getChartWidth() * mLegend.getMaxSizePercent())
                                + mLegend.getXOffset();
                        break;

                    case CENTER:

                        switch (mLegend.getVerticalAlignment()) {
                            case TOP:
                                offsets.top += Math.min(mLegend.mNeededHeight,
                                        mChartDimens.getChartHeight() * mLegend.getMaxSizePercent())
                                        + mLegend.getYOffset();
                                break;

                            case BOTTOM:
                                offsets.bottom += Math.min(mLegend.mNeededHeight,
                                        mChartDimens.getChartHeight() * mLegend.getMaxSizePercent())
                                        + mLegend.getYOffset();
                                break;

                            default:
                                break;
                        }
                }

                break;

            case HORIZONTAL:

                switch (mLegend.getVerticalAlignment()) {
                    case TOP:
                        offsets.top += Math.min(mLegend.mNeededHeight,
                                mChartDimens.getChartHeight() * mLegend.getMaxSizePercent())
                                + mLegend.getYOffset();


                        break;

                    case BOTTOM:
                        offsets.bottom += Math.min(mLegend.mNeededHeight,
                                mChartDimens.getChartHeight() * mLegend.getMaxSizePercent())
                                + mLegend.getYOffset();


                        break;

                    default:
                        break;
                }
                break;
        }
    }

    private RectF mOffsetsBuffer = new RectF();

    @Override
    public void calculateOffsets() {

        if (!mCustomViewPortEnabled) {

            float offsetLeft = 0f, offsetRight = 0f, offsetTop = 0f, offsetBottom = 0f;

            calculateLegendOffsets(mOffsetsBuffer);

            offsetLeft += mOffsetsBuffer.left;
            offsetTop += mOffsetsBuffer.top;
            offsetRight += mOffsetsBuffer.right;
            offsetBottom += mOffsetsBuffer.bottom;

            // offsets for y-labels
            if (mAxisLeft.needsOffset()) {
                offsetLeft += mAxisLeft.getRequiredWidthSpace(mAxisRendererLeft
                        .getPaintAxisLabels());
            }

            if (mXAxis.isEnabled() && mXAxis.isDrawLabelsEnabled()) {

                float xLabelHeight = mXAxis.mLabelRotatedHeight + mXAxis.getYOffset();

                // offsets for x-labels
                if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {

                    offsetBottom += xLabelHeight;

                } else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {

                    offsetTop += xLabelHeight;

                } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {

                    offsetBottom += xLabelHeight;
                    offsetTop += xLabelHeight;
                }
            }

            offsetTop += getExtraTopOffset();
            offsetRight += getExtraRightOffset();
            offsetBottom += getExtraBottomOffset() + 70f;
            offsetLeft += getExtraLeftOffset();

            float minOffset = ChartUtils.convertDpToPixel(mMinOffset);

            mChartDimens.restrainViewPort(
                    Math.max(minOffset, offsetLeft),
                    Math.max(minOffset, offsetTop),
                    Math.max(minOffset, offsetRight),
                    Math.max(minOffset, offsetBottom));

            if (mLogEnabled) {
                Log.i(LOG_TAG, "offsetLeft: " + offsetLeft + ", offsetTop: " + offsetTop
                        + ", offsetRight: " + offsetRight + ", offsetBottom: " + offsetBottom);
                Log.i(LOG_TAG, "Content: " + mChartDimens.getContentRect().toString());
            }
        }

        prepareOffsetMatrix();
        prepareValuePxMatrix();
    }

    /**
     * draws the grid background
     */
    protected void drawGridBackground(Canvas c) {

        if (mDrawGridBackground) {

            // draw the grid background
            c.drawRect(mChartDimens.getContentRect(), mGridBackgroundPaint);
        }

        if (mDrawBorders) {
            c.drawRect(mChartDimens.getContentRect(), mBorderPaint);
        }
    }

    /**
     * flag that indicates if a custom viewport offset has been set
     */
    private boolean mCustomViewPortEnabled = false;

    /**
     * Resets all custom offsets set via setViewPortOffsets(...) method. Allows
     * the chart to again calculate all offsets automatically.
     */
    public void resetViewPortOffsets() {
        mCustomViewPortEnabled = false;
        calculateOffsets();
    }

    /**
     * ################ ################ ################ ################
     */
    /** CODE BELOW IS GETTERS AND SETTERS */

    /**
     * Returns the range of the specified axis.
     *
     * @param axis
     * @return
     */
    protected float getAxisRange(YAxis.AxisDependency axis) {
            return mAxisLeft.mAxisRange;
    }

    protected float[] mGetPositionBuffer = new float[2];

    /**
     * Returns a recyclable MPPointF instance.
     * Returns the position (in pixels) the provided Entry has inside the chart
     * view or null, if the provided Entry is null.
     *
     * @param e
     * @return
     */
    public PointFloat getPosition(Entry e, YAxis.AxisDependency axis) {

        if (e == null)
            return null;

        mGetPositionBuffer[0] = e.getX();
        mGetPositionBuffer[1] = e.getY();

        getTransformer(axis).pointValuesToPixel(mGetPositionBuffer);

        return PointFloat.getInstance(mGetPositionBuffer[0], mGetPositionBuffer[1]);
    }

    public int getMaxVisibleCount() {
        return mMaxVisibleCount;
    }

    /**
     * set this to true to draw the grid background, false if not
     *
     * @param enabled
     */
    public void setDrawGridBackground(boolean enabled) {
        mDrawGridBackground = enabled;
    }

    /**
     * When enabled, the borders rectangle will be rendered.
     * If this is enabled, there is no point drawing the axis-lines of x- and y-axis.
     *
     * @param enabled
     */
    public void setDrawBorders(boolean enabled) {
        mDrawBorders = enabled;
    }

    /**
     * When enabled, the values will be clipped to contentRect,
     * otherwise they can bleed outside the content rect.
     *
     * @return
     */
    public boolean isClipValuesToContentEnabled() {
        return mClipValuesToContent;
    }

    /**
     * When disabled, the data and/or highlights will not be clipped to contentRect. Disabling this option can
     *   be useful, when the data lies fully within the content rect, but is drawn in such a way (such as thick lines)
     *   that there is unwanted clipping.
     *
     * @return
     */
    public boolean isClipDataToContentEnabled() {
        return mClipDataToContent;
    }


    /**
     * buffer for storing lowest visible x point
     */
    protected PointDouble posForGetLowestVisibleX = PointDouble.getInstance(0, 0);

    /**
     * Returns the lowest x-index (value on the x-axis) that is still visible on
     * the chart.
     *
     * @return
     */
    @Override
    public float getLowestVisibleX() {
        getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(mChartDimens.contentLeft(),
                mChartDimens.contentBottom(), posForGetLowestVisibleX);
        float result = (float) Math.max(mXAxis.mAxisMinimum, posForGetLowestVisibleX.x);
        return result;
    }

    /**
     * buffer for storing highest visible x point
     */
    protected PointDouble posForGetHighestVisibleX = PointDouble.getInstance(0, 0);

    /**
     * Returns the highest x-index (value on the x-axis) that is still visible
     * on the chart.
     *
     * @return
     */
    @Override
    public float getHighestVisibleX() {
        getTransformer(YAxis.AxisDependency.RIGHT).getValuesByTouchPoint(mChartDimens.contentRight(),
                mChartDimens.contentBottom(), posForGetHighestVisibleX);
        float result = (float) Math.min(mXAxis.mAxisMaximum, posForGetHighestVisibleX.x);
        return result;
    }

    /**
     * Returns the range visible on the x-axis.
     *
     * @return
     */
    public float getVisibleXRange() {
        return Math.abs(getHighestVisibleX() - getLowestVisibleX());
    }

    /**
     * returns the current x-scale factor
     */
    public float getScaleX() {
        if (mChartDimens == null)
            return 1f;
        else
            return mChartDimens.getScaleX();
    }

    /**
     * returns the current y-scale factor
     */
    public float getScaleY() {
        if (mChartDimens == null)
            return 1f;
        else
            return mChartDimens.getScaleY();
    }

    /**
     * Returns the left y-axis object. In the horizontal bar-chart, this is the
     * top axis.
     *
     * @return
     */
    public YAxis getAxisLeft() {
        return mAxisLeft;
    }

    /**
     * Returns the y-axis object to the corresponding AxisDependency. In the
     * horizontal bar-chart, LEFT == top, RIGHT == BOTTOM
     *
     * @param axis
     * @return
     */
    public YAxis getAxis(YAxis.AxisDependency axis) {
        return mAxisLeft;
    }


    @Override
    public void setPaint(Paint p, int which) {
        super.setPaint(p, which);

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                mGridBackgroundPaint = p;
                break;
        }
    }

    @Override
    public Paint getPaint(int which) {
        Paint p = super.getPaint(which);
        if (p != null)
            return p;

        switch (which) {
            case PAINT_GRID_BACKGROUND:
                return mGridBackgroundPaint;
        }

        return null;
    }

    protected float[] mOnSizeChangedBuffer = new float[2];

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        // Saving current position of chart.
        mOnSizeChangedBuffer[0] = mOnSizeChangedBuffer[1] = 0;

        //Superclass transforms chart.
        super.onSizeChanged(w, h, oldw, oldh);
        mChartDimens.refresh(mChartDimens.getMatrixTouch(), this, true);
    }

    /**
     * Returns the Transformer class that contains all matrices and is
     * responsible for transforming values into pixels on the screen and
     * backwards.
     *
     * @return
     */
    public ChartTransformer getTransformer(YAxis.AxisDependency which) {
            return mLeftAxisTransformer;
    }
}

