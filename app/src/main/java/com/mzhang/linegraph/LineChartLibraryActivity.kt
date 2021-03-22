package com.mzhang.linegraph

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.mzhang.linegraph.custom.MyMarkerView
//import com.github.mikephil.charting.components.LimitLine
//import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
//import com.mzhang.linegraph.lineGraph.chartBase.LineChart
//import com.mzhang.linegraph.lineGraph.chartData.ILineDataSet
//import com.mzhang.linegraph.lineGraph.chartData.LineData
//import com.mzhang.linegraph.lineGraph.chartData.LineDataSet
//import com.mzhang.linegraph.lineGraph.dataPoints.Entry
//import com.mzhang.linegraph.lineGraph.xyAxes.XAxis
//import com.mzhang.linegraph.lineGraph.xyAxes.YAxis
import java.util.*

class LineChartLibraryActivity : AppCompatActivity(), OnChartValueSelectedListener {
    private var chart: LineChart? = null
    private var tvY: TextView? = null
    private var tvX: TextView? = null
    private var yRightAxis: YAxis? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_chart_lib)

        tvX = findViewById(R.id.tvMovingText1)
        tvY = findViewById(R.id.tvValue)
//        initLineGraphWithLocalLib()
        initLineGraphMP()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initLineGraphMP() {
        run {   // // Chart Style // //
            chart = findViewById(R.id.chart1)

            // background color
            chart?.setBackgroundColor(Color.WHITE)

            // disable description text
            chart?.getDescription()?.setEnabled(false)

            // enable touch gestures
            chart?.setTouchEnabled(true)

            // set listeners
            chart?.setOnChartValueSelectedListener(this)
            chart?.setDrawGridBackground(false)

            // for drag
            chart?.isHighlightPerDragEnabled = true

//             create marker to display box when values are selected
            val mv = MyMarkerView(this, R.layout.custom_marker_view)

            // Set the marker to the chart
            mv.setChartView(chart)
            chart?.setMarker(mv)

            // enable scaling and dragging
            chart?.setDragEnabled(true)
            chart?.setScaleEnabled(false)
            // chart?.setScaleXEnabled(true);
            // chart?.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart?.setPinchZoom(false)
        }

        var xAxis: XAxis
        run {   // // X-Axis Style // //
            xAxis = chart!!.xAxis

            // vertical grid lines
//            xAxis.enableGridDashedLine(10f, 10f, 0f)
            // hide grid lines
            xAxis.setDrawGridLines(false)

            // x Axis label position
            xAxis.position = XAxis.XAxisPosition.BOTTOM
        }

        var yAxis: YAxis
        run {   // // Y-Axis Style // //
            yAxis = chart!!.axisLeft

            // disable dual axis (only use LEFT axis)
            chart!!.axisLeft.isEnabled = false

            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
//            yAxis.axisMaximum = 200f
//            yAxis.axisMinimum = -50f
        }
//        var yRightAxis: YAxis
        run {   // // Y-Axis Style // //
            yRightAxis = chart!!.axisRight

            // disable dual axis (only use LEFT axis)
//            chart!!.axisRight.isEnabled = true

            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
//            yRightAxis.axisMaximum = 200f
//            yRightAxis.axisMinimum = -50f
        }

        // add data
        setData(45, 180f)

        // draw points over time

        // draw points over time
        chart!!.animateX(1500)

        // remove highlight for untouch
        chart?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                chart?.highlightValue(null)
            }
            return@setOnTouchListener false
        }

    }

    private fun setData(count: Int, range: Float) {
        val values: ArrayList<Entry> = ArrayList<Entry>()
        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat()
            values.add(Entry(i.toFloat(), value, null))
            Log.i("SETDATA", "(i, value) = ($i, $value)")
        }
        val set1: LineDataSet
        if (chart?.data != null &&
                chart?.data?.dataSetCount!! > 0) {
            set1 = chart?.data?.getDataSetByIndex(0) as LineDataSet
            set1.setValues(values)
            set1.notifyDataSetChanged()
            chart?.data?.notifyDataChanged()
            chart?.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f)
            set1.valueTextSize = 0f

            // black lines and points
            set1.setColor(Color.BLACK)
            set1.setCircleColor(Color.TRANSPARENT)

            // line thickness and point size
            set1.setLineWidth(1f)
            set1.setCircleRadius(3f)

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.setFormLineWidth(1f)
//            set1.setFormLineDashEffect(DashPathEffect(floatArrayOf(10f, 5f), 0f))
            set1.setFormSize(15f)

            // text size of values
            set1.valueTextSize = 0f
//            set1.setDrawHorizontalHighlightIndicator(false)
            set1.highLightColor = Color.RED
            set1.highlightLineWidth = 2f
            set1.setDrawHighlightIndicators(true)
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.isHighlightEnabled = true

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 0f, 0f)

            // Right Axis displays the correct value
            set1.axisDependency = YAxis.AxisDependency.RIGHT
            // set the filled area
//            set1.setDrawFilled(true)
//            set1.setFillFormatter(object : IFillFormatter() {
//                fun getFillLinePosition(dataSet: ILineDataSet?, dataProvider: LineDataProvider?): Float {
//                    return chart?.axisLeft.getAxisMinimum()
//                }
//            })

//            // set color of filled area
//            if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
//                set1.setFillDrawable(drawable)
//            } else {
//                set1.setFillColor(Color.BLACK)
//            }

            val yAxis = chart!!.axisRight
            yAxis.setAxisMaximum(200f)
            yAxis.setAxisMinimum(0f)
            yAxis.setLabelCount(8, true)

            val dataSets: ArrayList<ILineDataSet> = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart?.data = data
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.i("Entry selected", e.toString())
        Log.i("LOW HIGH", "low: " + chart!!.lowestVisibleX + ", high: " + chart!!.highestVisibleX)
        Log.i("MIN MAX", "xMin: " + chart!!.xChartMin + ", xMax: " + chart!!.xChartMax + ", yMin: " + chart!!.yChartMin + ", yMax: " + chart!!.yChartMax)

//        tvY?.text = h?.y.toString()
//        tvX?.text = h?.x.toString()
        tvY?.text = e?.y.toString()
        tvX?.text = e?.x.toString()
        val width = tvX?.width ?: 0
        tvX?.x = h?.xPx?.minus(width / 2) ?: 0f
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
        chart?.highlightValue(null)
    }

}