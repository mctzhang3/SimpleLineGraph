package com.mzhang.linegraph.lineGraph.chartDefaults;

import android.graphics.Matrix;
import android.graphics.Path;

import com.mzhang.linegraph.lineGraph.chartData.ILineDataSet;
import com.mzhang.linegraph.lineGraph.dataPoints.Entry;
import com.mzhang.linegraph.lineGraph.dataPoints.PointDouble;

/**
 * Transformer class that contains all matrices and is responsible for
 * transforming values into pixels on the screen and backwards.
 *
 */
public class ChartTransformer {

    /**
     * matrix to map the values to the screen pixels
     */
    protected Matrix mMatrixValueToPx = new Matrix();

    /**
     * matrix for handling the different offsets of the chart
     */
    protected Matrix mMatrixOffset = new Matrix();

    protected ChartDimens mChartDimens;

    public ChartTransformer(ChartDimens chartDimens) {
        this.mChartDimens = chartDimens;
    }

    /**
     * Prepares the matrix that transforms values to pixels. Calculates the
     * scale factors from the charts size and offsets.
     *
     * @param xChartMin
     * @param deltaX
     * @param deltaY
     * @param yChartMin
     */
    public void prepareMatrixValuePx(float xChartMin, float deltaX, float deltaY, float yChartMin) {

        float scaleX = (mChartDimens.contentWidth()) / deltaX;
        float scaleY = (mChartDimens.contentHeight()) / deltaY;

        if (Float.isInfinite(scaleX)) {
            scaleX = 0;
        }
        if (Float.isInfinite(scaleY)) {
            scaleY = 0;
        }

        // setup all matrices
        mMatrixValueToPx.reset();
        mMatrixValueToPx.postTranslate(-xChartMin, -yChartMin);
        mMatrixValueToPx.postScale(scaleX, -scaleY);
    }

    /**
     * Prepares the matrix that contains all offsets.
     *
     * @param inverted
     */
    public void prepareMatrixOffset(boolean inverted) {

        mMatrixOffset.reset();

        // offset.postTranslate(mOffsetLeft, getHeight() - mOffsetBottom);

        if (!inverted)
            mMatrixOffset.postTranslate(mChartDimens.offsetLeft(),
                    mChartDimens.getChartHeight() - mChartDimens.offsetBottom());
        else {
            mMatrixOffset
                    .setTranslate(mChartDimens.offsetLeft(), -mChartDimens.offsetTop());
            mMatrixOffset.postScale(1.0f, -1.0f);
        }
    }


    protected float[] valuePointsForGenerateTransformedValuesLine = new float[1];

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the LINECHART.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesLine(ILineDataSet data,
                                                 float phaseX, float phaseY,
                                                 int min, int max) {

        final int count = ((int) ((max - min) * phaseX) + 1) * 2;

        if (valuePointsForGenerateTransformedValuesLine.length != count) {
            valuePointsForGenerateTransformedValuesLine = new float[count];
        }
        float[] valuePoints = valuePointsForGenerateTransformedValuesLine;

        for (int j = 0; j < count; j += 2) {

            Entry e = data.getEntryForIndex(j / 2 + min);

            if (e != null) {
                valuePoints[j] = e.getX();
                valuePoints[j + 1] = e.getY() * phaseY;
            } else {
                valuePoints[j] = 0;
                valuePoints[j + 1] = 0;
            }
        }

        getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }


    /**
     * transform a path with all the given matrices VERY IMPORTANT: keep order
     * to value-touch-offset
     *
     * @param path
     */
    public void pathValueToPixel(Path path) {

        path.transform(mMatrixValueToPx);
        path.transform(mChartDimens.getMatrixTouch());
        path.transform(mMatrixOffset);
    }

    /**
     * Transform an array of points with all matrices. VERY IMPORTANT: Keep
     * matrix order "value-touch-offset" when transforming.
     *
     * @param pts
     */
    public void pointValuesToPixel(float[] pts) {

        mMatrixValueToPx.mapPoints(pts);
        mChartDimens.getMatrixTouch().mapPoints(pts);
        mMatrixOffset.mapPoints(pts);
    }

    protected Matrix mPixelToValueMatrixBuffer = new Matrix();

    /**
     * Transforms the given array of touch positions (pixels) (x, y, x, y, ...)
     * into values on the chart.
     *
     * @param pixels
     */
    public void pixelsToValue(float[] pixels) {

        Matrix tmp = mPixelToValueMatrixBuffer;
        tmp.reset();

        // invert all matrixes to convert back to the original value
        mMatrixOffset.invert(tmp);
        tmp.mapPoints(pixels);

        mChartDimens.getMatrixTouch().invert(tmp);
        tmp.mapPoints(pixels);

        mMatrixValueToPx.invert(tmp);
        tmp.mapPoints(pixels);
    }

    /**
     * buffer for performance
     */
    float[] ptsBuffer = new float[2];

    /**
     * Returns a recyclable MPPointD instance.
     * returns the x and y values in the chart at the given touch point
     * (encapsulated in a MPPointD). This method transforms pixel coordinates to
     * coordinates / values in the chart. This is the opposite method to
     * getPixelForValues(...).
     *
     * @param x
     * @param y
     * @return
     */
    public PointDouble getValuesByTouchPoint(float x, float y) {

        PointDouble result = PointDouble.getInstance(0, 0);
        getValuesByTouchPoint(x, y, result);
        return result;
    }

    public void getValuesByTouchPoint(float x, float y, PointDouble outputPoint) {

        ptsBuffer[0] = x;
        ptsBuffer[1] = y;

        pixelsToValue(ptsBuffer);

        outputPoint.x = ptsBuffer[0];
        outputPoint.y = ptsBuffer[1];
    }

    /**
     * Returns a recyclable MPPointD instance.
     * Returns the x and y coordinates (pixels) for a given x and y value in the chart.
     *
     * @param x
     * @param y
     * @return
     */
    public PointDouble getPixelForValues(float x, float y) {

        ptsBuffer[0] = x;
        ptsBuffer[1] = y;

        pointValuesToPixel(ptsBuffer);

        double xPx = ptsBuffer[0];
        double yPx = ptsBuffer[1];

        return PointDouble.getInstance(xPx, yPx);
    }

    private Matrix mMBuffer1 = new Matrix();

    public Matrix getValueToPixelMatrix() {
        mMBuffer1.set(mMatrixValueToPx);
        mMBuffer1.postConcat(mChartDimens.mMatrixTouch);
        mMBuffer1.postConcat(mMatrixOffset);
        return mMBuffer1;
    }
}

