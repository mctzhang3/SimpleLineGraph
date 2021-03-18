package com.mzhang.linegraph.lineGraph.xyAxes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.mzhang.linegraph.lineGraph.chartDefaults.ChartDimens;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartTransformer;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartUtils;
import com.mzhang.linegraph.lineGraph.dataPoints.FSize;
import com.mzhang.linegraph.lineGraph.dataPoints.PointFloat;

public class XAxisRenderer extends AxisRenderer {

    protected XAxis mXAxis;

    public XAxisRenderer(ChartDimens chartDimens, XAxis xAxis, ChartTransformer trans) {
        super(chartDimens, trans, xAxis);

        this.mXAxis = xAxis;

        mAxisLabelPaint.setColor(Color.BLACK);
        mAxisLabelPaint.setTextAlign(Align.CENTER);
        mAxisLabelPaint.setTextSize(ChartUtils.convertDpToPixel(10f));
    }

    protected void setupGridPaint() {
        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());
    }

    @Override
    public void computeAxis(float min, float max, boolean inverted) {
        computeAxisValues(min, max);
    }

    @Override
    protected void computeAxisValues(float min, float max) {
        super.computeAxisValues(min, max);

        computeSize();
    }

    protected void computeSize() {

        String longest = mXAxis.getLongestLabel();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());

        final FSize labelSize = ChartUtils.calcTextSize(mAxisLabelPaint, longest);

        final float labelWidth = labelSize.width;
        final float labelHeight = ChartUtils.calcTextHeight(mAxisLabelPaint, "Q");

        final FSize labelRotatedSize = ChartUtils.getSizeOfRotatedRectangleByDegrees(
                labelWidth,
                labelHeight,
                mXAxis.getLabelRotationAngle());


        mXAxis.mLabelWidth = Math.round(labelWidth);
        mXAxis.mLabelHeight = Math.round(labelHeight);
        mXAxis.mLabelRotatedWidth = Math.round(labelRotatedSize.width);
        mXAxis.mLabelRotatedHeight = Math.round(labelRotatedSize.height);

        FSize.recycleInstance(labelRotatedSize);
        FSize.recycleInstance(labelSize);
    }

    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;

        float yoffset = mXAxis.getYOffset();

        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());

        PointFloat pointF = PointFloat.getInstance(0,0);
        if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mChartDimens.contentTop() - yoffset, pointF);

        } else if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mChartDimens.contentTop() + yoffset + mXAxis.mLabelRotatedHeight, pointF);

        } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mChartDimens.contentBottom() + yoffset, pointF);

        } else if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mChartDimens.contentBottom() - yoffset - mXAxis.mLabelRotatedHeight, pointF);

        } else { // BOTH SIDED
            pointF.x = 0.5f;
            pointF.y = 1.0f;
            drawLabels(c, mChartDimens.contentTop() - yoffset, pointF);
            pointF.x = 0.5f;
            pointF.y = 0.0f;
            drawLabels(c, mChartDimens.contentBottom() + yoffset, pointF);
        }
        PointFloat.recycleInstance(pointF);
    }

    @Override
    public void renderAxisLine(Canvas c) {

        if (!mXAxis.isDrawAxisLineEnabled() || !mXAxis.isEnabled())
            return;

        mAxisLinePaint.setColor(mXAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mXAxis.getAxisLineWidth());
        mAxisLinePaint.setPathEffect(mXAxis.getAxisLineDashPathEffect());

        if (mXAxis.getPosition() == XAxis.XAxisPosition.TOP
                || mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE
                || mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(mChartDimens.contentLeft(),
                    mChartDimens.contentTop(), mChartDimens.contentRight(),
                    mChartDimens.contentTop(), mAxisLinePaint);
        }

        if (mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM
                || mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE
                || mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED) {
            c.drawLine(mChartDimens.contentLeft(),
                    mChartDimens.contentBottom(), mChartDimens.contentRight(),
                    mChartDimens.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    protected void drawLabels(Canvas c, float pos, PointFloat anchor) {

        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];

        for (int i = 0; i < positions.length; i += 2) {

            // only fill x values
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }

        mTrans.pointValuesToPixel(positions);

        for (int i = 0; i < positions.length; i += 2) {

            float x = positions[i];

            if (mChartDimens.isInBoundsX(x)) {

                String label = mXAxis.getValueFormatter().getAxisLabel(mXAxis.mEntries[i / 2], mXAxis);

                if (mXAxis.isAvoidFirstLastClippingEnabled()) {

                    // avoid clipping of the last
                    if (i / 2 == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                        float width = ChartUtils.calcTextWidth(mAxisLabelPaint, label);

                        if (width > mChartDimens.offsetRight() * 2
                                && x + width > mChartDimens.getChartWidth())
                            x -= width / 2;

                        // avoid clipping of the first
                    } else if (i == 0) {

                        float width = ChartUtils.calcTextWidth(mAxisLabelPaint, label);
                        x += width / 2;
                    }
                }

                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }
    }

    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, PointFloat anchor, float angleDegrees) {
        ChartUtils.drawXAxisValue(c, formattedLabel, x, y, mAxisLabelPaint, anchor, angleDegrees);
    }
    protected Path mRenderGridLinesPath = new Path();
    protected float[] mRenderGridLinesBuffer = new float[2];
    @Override
    public void renderGridLines(Canvas c) {

        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;

        int clipRestoreCount = c.save();
        c.clipRect(getGridClippingRect());

        if(mRenderGridLinesBuffer.length != mAxis.mEntryCount * 2){
            mRenderGridLinesBuffer = new float[mXAxis.mEntryCount * 2];
        }
        float[] positions = mRenderGridLinesBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            positions[i] = mXAxis.mEntries[i / 2];
            positions[i + 1] = mXAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);

        setupGridPaint();

        Path gridLinePath = mRenderGridLinesPath;
        gridLinePath.reset();

        for (int i = 0; i < positions.length; i += 2) {

            drawGridLine(c, positions[i], positions[i + 1], gridLinePath);
        }

        c.restoreToCount(clipRestoreCount);
    }

    protected RectF mGridClippingRect = new RectF();

    public RectF getGridClippingRect() {
        mGridClippingRect.set(mChartDimens.getContentRect());
        mGridClippingRect.inset(-mAxis.getGridLineWidth(), 0.f);
        return mGridClippingRect;
    }

    /**
     * Draws the grid line at the specified position using the provided path.
     *
     * @param c
     * @param x
     * @param y
     * @param gridLinePath
     */
    protected void drawGridLine(Canvas c, float x, float y, Path gridLinePath) {

        gridLinePath.moveTo(x, mChartDimens.contentBottom());
        gridLinePath.lineTo(x, mChartDimens.contentTop());

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(gridLinePath, mGridPaint);

        gridLinePath.reset();
    }
}

