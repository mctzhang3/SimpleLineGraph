package com.mzhang.linegraph.lineGraph.dataPoints;

import android.graphics.Canvas;

public interface Marker {

    /**
     * @return The desired (general) offset you wish the IMarker to have on the x- and y-axis.
     *         By returning x: -(width / 2) you will center the IMarker horizontally.
     *         By returning y: -(height / 2) you will center the IMarker vertically.
     */
    PointFloat getOffset();

    /**
     * Draws the IMarker on the given position on the screen with the given Canvas object.
     *
     * @param canvas
     * @param posX
     * @param posY
     */
    void draw(Canvas canvas, float posX, float posY);
}

