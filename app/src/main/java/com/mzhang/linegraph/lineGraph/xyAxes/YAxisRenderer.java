package com.mzhang.linegraph.lineGraph.xyAxes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.mzhang.linegraph.lineGraph.chartDefaults.ChartDimens;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartTransformer;
import com.mzhang.linegraph.lineGraph.chartDefaults.ChartUtils;

public class YAxisRenderer extends AxisRenderer {

    protected YAxis mYAxis;

    protected Paint mZeroLinePaint;

    public YAxisRenderer(ChartDimens chartDimens, YAxis yAxis, ChartTransformer trans) {
        super(chartDimens, trans, yAxis);

        this.mYAxis = yAxis;

        if(mChartDimens != null) {

            mAxisLabelPaint.setColor(Color.BLACK);
            mAxisLabelPaint.setTextSize(ChartUtils.convertDpToPixel(10f));

            mZeroLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mZeroLinePaint.setColor(Color.GRAY);
            mZeroLinePaint.setStrokeWidth(1f);
            mZeroLinePaint.setStyle(Paint.Style.STROKE);
        }
    }

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawLabelsEnabled())
            return;

        float[] positions = getTransformedPositions();

        mAxisLabelPaint.setTypeface(mYAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mYAxis.getTextSize());
        mAxisLabelPaint.setColor(mYAxis.getTextColor());

        float xoffset = mYAxis.getXOffset();
        float yoffset = ChartUtils.calcTextHeight(mAxisLabelPaint, "A") / 2.5f + mYAxis.getYOffset();

        YAxis.AxisDependency dependency = mYAxis.getAxisDependency();
        YAxis.YAxisLabelPosition labelPosition = mYAxis.getLabelPosition();

        float xPos = 0f;

        if (dependency == YAxis.AxisDependency.LEFT) {

            if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mChartDimens.offsetLeft() - xoffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mChartDimens.offsetLeft() + xoffset;
            }

        } else {

            if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.setTextAlign(Align.LEFT);
                xPos = mChartDimens.contentRight() + xoffset;
            } else {
                mAxisLabelPaint.setTextAlign(Align.RIGHT);
                xPos = mChartDimens.contentRight() - xoffset;
            }
        }

        drawYLabels(c, xPos, positions, yoffset);
    }

    @Override
    public void renderAxisLine(Canvas c) {

        if (!mYAxis.isEnabled() || !mYAxis.isDrawAxisLineEnabled())
            return;

        mAxisLinePaint.setColor(mYAxis.getAxisLineColor());
        mAxisLinePaint.setStrokeWidth(mYAxis.getAxisLineWidth());

        if (mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT) {
            c.drawLine(mChartDimens.contentLeft(), mChartDimens.contentTop(), mChartDimens.contentLeft(),
                    mChartDimens.contentBottom(), mAxisLinePaint);
        } else {
            c.drawLine(mChartDimens.contentRight(), mChartDimens.contentTop(), mChartDimens.contentRight(),
                    mChartDimens.contentBottom(), mAxisLinePaint);
        }
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {

        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);

        float xOffset = mYAxis.getLabelXOffset();

        // draw
        for (int i = from; i < to; i++) {

            String text = mYAxis.getFormattedLabel(i);

            c.drawText(text,
                    fixedPosition + xOffset,
                    positions[i * 2 + 1] + offset,
                    mAxisLabelPaint);
        }
    }

    protected Path mRenderGridLinesPath = new Path();
    @Override
    public void renderGridLines(Canvas c) {

        if (!mYAxis.isEnabled())
            return;

        if (mYAxis.isDrawGridLinesEnabled()) {

            int clipRestoreCount = c.save();
            c.clipRect(getGridClippingRect());

            float[] positions = getTransformedPositions();

            mGridPaint.setColor(mYAxis.getGridColor());
            mGridPaint.setStrokeWidth(mYAxis.getGridLineWidth());
            mGridPaint.setPathEffect(mYAxis.getGridDashPathEffect());

            Path gridLinePath = mRenderGridLinesPath;
            gridLinePath.reset();

            // draw the grid
            for (int i = 0; i < positions.length; i += 2) {

                // draw a path because lines don't support dashing on lower android versions
                c.drawPath(linePath(gridLinePath, i, positions), mGridPaint);
                gridLinePath.reset();
            }

            c.restoreToCount(clipRestoreCount);
        }

        if (mYAxis.isDrawZeroLineEnabled()) {
            drawZeroLine(c);
        }
    }

    protected RectF mGridClippingRect = new RectF();

    public RectF getGridClippingRect() {
        mGridClippingRect.set(mChartDimens.getContentRect());
        mGridClippingRect.inset(0.f, -mAxis.getGridLineWidth());
        return mGridClippingRect;
    }

    /**
     * Calculates the path for a grid line.
     *
     * @param p
     * @param i
     * @param positions
     * @return
     */
    protected Path linePath(Path p, int i, float[] positions) {

        p.moveTo(mChartDimens.offsetLeft(), positions[i + 1]);
        p.lineTo(mChartDimens.contentRight(), positions[i + 1]);

        return p;
    }

    protected float[] mGetTransformedPositionsBuffer = new float[2];
    /**
     * Transforms the values contained in the axis entries to screen pixels and returns them in form of a float array
     * of x- and y-coordinates.
     *
     * @return
     */
    protected float[] getTransformedPositions() {

        if(mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2){
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }
        float[] positions = mGetTransformedPositionsBuffer;

        for (int i = 0; i < positions.length; i += 2) {
            // only fill y values, x values are not needed for y-labels
            positions[i + 1] = mYAxis.mEntries[i / 2];
        }

        mTrans.pointValuesToPixel(positions);
        return positions;
    }

    protected Path mDrawZeroLinePath = new Path();
    protected RectF mZeroLineClippingRect = new RectF();

    /**
     * Draws the zero line.
     */
    protected void drawZeroLine(Canvas c) {

        int clipRestoreCount = c.save();
        mZeroLineClippingRect.set(mChartDimens.getContentRect());
        mZeroLineClippingRect.inset(0.f, -mYAxis.getZeroLineWidth());
        c.clipRect(mZeroLineClippingRect);

        mZeroLinePaint.setColor(mYAxis.getZeroLineColor());
        mZeroLinePaint.setStrokeWidth(mYAxis.getZeroLineWidth());

        Path zeroLinePath = mDrawZeroLinePath;
        zeroLinePath.reset();

        // draw a path because lines don't support dashing on lower android versions
        c.drawPath(zeroLinePath, mZeroLinePaint);

        c.restoreToCount(clipRestoreCount);
    }
}
