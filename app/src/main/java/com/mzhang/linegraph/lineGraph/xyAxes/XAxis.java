package com.mzhang.linegraph.lineGraph.xyAxes;

import com.mzhang.linegraph.lineGraph.chartDefaults.ChartUtils;

/**
 * Class representing the x-axis labels settings.
 *
 */
public class XAxis extends AxisBase {

    /**
     * width of the x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    public int mLabelWidth = 1;

    /**
     * height of the x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    public int mLabelHeight = 1;

    /**
     * width of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    public int mLabelRotatedWidth = 1;

    /**
     * height of the (rotated) x-axis labels in pixels - this is automatically
     * calculated by the computeSize() methods in the renderers
     */
    public int mLabelRotatedHeight = 1;

    /**
     * This is the angle for drawing the X axis labels (in degrees)
     */
    protected float mLabelRotationAngle = 0f;

    /**
     * if set to true, the chart will avoid that the first and last label entry
     * in the chart "clip" off the edge of the chart
     */
    private boolean mAvoidFirstLastClipping = false;

    /**
     * the position of the x-labels relative to the chart
     */
    private XAxisPosition mPosition = XAxisPosition.TOP;

    /**
     * enum for the position of the x-labels relative to the chart
     */
    public enum XAxisPosition {
        TOP, BOTTOM, BOTH_SIDED, TOP_INSIDE, BOTTOM_INSIDE
    }

    public XAxis() {
        super();

        mYOffset = ChartUtils.convertDpToPixel(4.f); // -3
    }

    /**
     * returns the position of the x-labels
     */
    public XAxisPosition getPosition() {
        return mPosition;
    }

    /**
     * sets the position of the x-labels
     *
     * @param pos
     */
    public void setPosition(XAxisPosition pos) {
        mPosition = pos;
    }

    /**
     * returns the angle for drawing the X axis labels (in degrees)
     */
    public float getLabelRotationAngle() {
        return mLabelRotationAngle;
    }

    /**
     * returns true if avoid-first-lastclipping is enabled, false if not
     *
     * @return
     */
    public boolean isAvoidFirstLastClippingEnabled() {
        return mAvoidFirstLastClipping;
    }
}

