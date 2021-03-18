package com.mzhang.linegraph.lineGraph.chartData;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.mzhang.linegraph.lineGraph.chartDefaults.ChartUtils;
import com.mzhang.linegraph.lineGraph.dataPoints.Entry;
import com.mzhang.linegraph.lineGraph.labelFormatters.DefaultFillFormatter;
import com.mzhang.linegraph.lineGraph.labelFormatters.IFillFormatter;

import java.util.ArrayList;
import java.util.List;

public class LineDataSet extends BaseLineDataSet<Entry> implements ILineDataSet {

    /**
     * Drawing mode for this line dataset
     **/
    private Mode mMode = Mode.LINEAR;

    /**
     * List representing all colors that are used for the circles
     */
    private List<Integer> mCircleColors = null;

    /**
     * the color of the inner circles
     */
    private int mCircleHoleColor = Color.WHITE;

    /**
     * the radius of the circle-shaped value indicators
     */
    private float mCircleRadius = 8f;

    /**
     * the hole radius of the circle-shaped value indicators
     */
    private float mCircleHoleRadius = 4f;

    /**
     * sets the intensity of the cubic lines
     */
    private float mCubicIntensity = 0.2f;

    private float mLineWidth = 2.5f;

    /**
     * the color that is used for filling the line surface
     */
    private int mFillColor = Color.rgb(140, 234, 255);

    /**
     * transparency used for filling line surface
     */
    private int mFillAlpha = 30;

    /**
     * the drawable to be used for filling the line surface
     */
    protected Drawable mFillDrawable;
    /**
     * formatter for customizing the position of the fill-line
     */
    private IFillFormatter mFillFormatter = new DefaultFillFormatter();

    @Override
    public IFillFormatter getFillFormatter() {
        return mFillFormatter;
    }

    public void setLineWidth(float width) {

        if (width < 0.0f)
            width = 0.0f;
        if (width > 10.0f)
            width = 10.0f;
        mLineWidth = ChartUtils.convertDpToPixel(width);
    }

    protected DashPathEffect mHighlightDashPathEffect = null;

    public void enableDashedHighlightLine(float lineLength, float spaceLength, float phase) {
        mHighlightDashPathEffect = new DashPathEffect(new float[]{
                lineLength, spaceLength
        }, phase);
    }

    @Override
    public Drawable getFillDrawable() {
        return mFillDrawable;
    }

    /**
     * Sets the drawable to be used to fill the area below the line.
     *
     * @param drawable
     */
    @TargetApi(18)
    public void setFillDrawable(Drawable drawable) {
        this.mFillDrawable = drawable;
    }

    /**
     * Sets the color that is used for filling the area below the line.
     * Resets an eventually set "fillDrawable".
     *
     * @param color
     */
    public void setFillColor(int color) {
        mFillColor = color;
        mFillDrawable = null;
    }

    @Override
    public int getFillColor() {
        return mFillColor;
    }

    @Override
    public int getFillAlpha() {
        return mFillAlpha;
    }

    @Override
    public float getLineWidth() {
        return mLineWidth;
    }


    /**
     * the path effect of this DataSet that makes dashed lines possible
     */
    private DashPathEffect mDashPathEffect = null;

    /**
     * if true, drawing circles is enabled
     */
    private boolean mDrawCircles = true;

    private boolean mDrawCircleHole = true;


    public LineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);

        if (mCircleColors == null) {
            mCircleColors = new ArrayList<Integer>();
        }
        mCircleColors.clear();
        mCircleColors.add(Color.rgb(140, 234, 255));
    }

    @Override
    public DataSet<Entry> copy() {
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < mEntries.size(); i++) {
            entries.add(mEntries.get(i).copy());
        }
        LineDataSet copied = new LineDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(LineDataSet lineDataSet) {
        super.copy(lineDataSet);
        lineDataSet.mCircleColors = mCircleColors;
        lineDataSet.mCircleHoleColor = mCircleHoleColor;
        lineDataSet.mCircleHoleRadius = mCircleHoleRadius;
        lineDataSet.mCircleRadius = mCircleRadius;
        lineDataSet.mCubicIntensity = mCubicIntensity;
        lineDataSet.mDashPathEffect = mDashPathEffect;
        lineDataSet.mDrawCircleHole = mDrawCircleHole;
        lineDataSet.mFillDrawable = mFillDrawable;
        lineDataSet.mDrawCircles = mDrawCircleHole;
        lineDataSet.mMode = mMode;
    }

    /**
     * Returns the drawing mode for this line dataset
     *
     * @return
     */
    @Override
    public Mode getMode() {
        return mMode;
    }

    /**
     * Returns the drawing mode for this LineDataSet
     *
     * @return
     */
    public void setMode(Mode mode) {
        mMode = mode;
    }

    /**
     * Sets the intensity for cubic lines (if enabled). Max = 1f = very cubic,
     * Min = 0.05f = low cubic effect, Default: 0.2f
     *
     * @param intensity
     */
    public void setCubicIntensity(float intensity) {

        if (intensity > 1f)
            intensity = 1f;
        if (intensity < 0.05f)
            intensity = 0.05f;

        mCubicIntensity = intensity;
    }

    @Override
    public float getCubicIntensity() {
        return mCubicIntensity;
    }


    /**
     * Sets the radius of the drawn circles.
     * Default radius = 4f, Min = 1f
     *
     * @param radius
     */
    public void setCircleRadius(float radius) {

        if (radius >= 1f) {
            mCircleRadius = ChartUtils.convertDpToPixel(radius);
        } else {
            Log.e("LineDataSet", "Circle radius cannot be < 1");
        }
    }

    @Override
    public float getCircleRadius() {
        return mCircleRadius;
    }

    /**
     * Sets the hole radius of the drawn circles.
     * Default radius = 2f, Min = 0.5f
     *
     * @param holeRadius
     */
    public void setCircleHoleRadius(float holeRadius) {

        if (holeRadius >= 0.5f) {
            mCircleHoleRadius = ChartUtils.convertDpToPixel(holeRadius);
        } else {
            Log.e("LineDataSet", "Circle radius cannot be < 0.5");
        }
    }

    @Override
    public float getCircleHoleRadius() {
        return mCircleHoleRadius;
    }

    /**
     * sets the size (radius) of the circle shpaed value indicators,
     * default size = 4f
     * <p/>
     * This method is deprecated because of unclarity. Use setCircleRadius instead.
     *
     * @param size
     */
    @Deprecated
    public void setCircleSize(float size) {
        setCircleRadius(size);
    }

    /**
     * This function is deprecated because of unclarity. Use getCircleRadius instead.
     */
    @Deprecated
    public float getCircleSize() {
        return getCircleRadius();
    }

    /**
     * Enables the line to be drawn in dashed mode, e.g. like this
     * "- - - - - -". THIS ONLY WORKS IF HARDWARE-ACCELERATION IS TURNED OFF.
     * Keep in mind that hardware acceleration boosts performance.
     *
     * @param lineLength  the length of the line pieces
     * @param spaceLength the length of space in between the pieces
     * @param phase       offset, in degrees (normally, use 0)
     */
    public void enableDashedLine(float lineLength, float spaceLength, float phase) {
        mDashPathEffect = new DashPathEffect(new float[]{
                lineLength, spaceLength
        }, phase);
    }

    /**
     * Disables the line to be drawn in dashed mode.
     */
    public void disableDashedLine() {
        mDashPathEffect = null;
    }

    @Override
    public boolean isDashedLineEnabled() {
        return mDashPathEffect != null;
    }

    @Override
    public DashPathEffect getDashPathEffect() {
        return mDashPathEffect;
    }

    @Override
    public boolean isDrawCirclesEnabled() {
        return mDrawCircles;
    }

    @Deprecated
    @Override
    public boolean isDrawCubicEnabled() {
        return mMode == Mode.CUBIC_BEZIER;
    }

    @Deprecated
    @Override
    public boolean isDrawSteppedEnabled() {
        return mMode == Mode.STEPPED;
    }

    @Override
    public int getCircleColor(int index) {
        return mCircleColors.get(index);
    }

    @Override
    public int getCircleColorCount() {
        return mCircleColors.size();
    }

    /**
     * Sets the one and ONLY color that should be used for this DataSet.
     * Internally, this recreates the colors array and adds the specified color.
     *
     * @param color
     */
    public void setCircleColor(int color) {
        resetCircleColors();
        mCircleColors.add(color);
    }

    /**
     * resets the circle-colors array and creates a new one
     */
    public void resetCircleColors() {
        if (mCircleColors == null) {
            mCircleColors = new ArrayList<Integer>();
        }
        mCircleColors.clear();
    }

    @Override
    public int getCircleHoleColor() {
        return mCircleHoleColor;
    }

    /**
     * Set this to true to allow drawing a hole in each data circle.
     *
     * @param enabled
     */
    public void setDrawCircleHole(boolean enabled) {
        mDrawCircleHole = enabled;
    }

    @Override
    public boolean isDrawCircleHoleEnabled() {
        return mDrawCircleHole;
    }

    public enum Mode {
        LINEAR,
        STEPPED,
        CUBIC_BEZIER,
        HORIZONTAL_BEZIER
    }
}

