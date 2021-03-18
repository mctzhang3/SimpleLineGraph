package com.mzhang.linegraph.lineGraph.chartRenderers;

import com.mzhang.linegraph.lineGraph.chartDefaults.ChartDimens;

/**
 * Abstract baseclass of Renderer.
 *
 */
public abstract class BaseRenderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ChartDimens mChartDimens;

    public BaseRenderer(ChartDimens chartDimens) {
        this.mChartDimens = chartDimens;
    }
}

