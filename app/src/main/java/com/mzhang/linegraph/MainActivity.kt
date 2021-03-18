package com.mzhang.linegraph

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mzhang.linegraph.lineGraph.chartBase.LineChart
import com.mzhang.linegraph.lineGraph.chartData.ILineDataSet
import com.mzhang.linegraph.lineGraph.chartData.LineData
import com.mzhang.linegraph.lineGraph.chartData.LineDataSet
import com.mzhang.linegraph.lineGraph.dataPoints.Entry
import com.mzhang.linegraph.lineGraph.xyAxes.XAxis
import com.mzhang.linegraph.lineGraph.xyAxes.YAxis
import java.util.*

class MainActivity : AppCompatActivity() {
    private var chart: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        run {
            chart = findViewById<LineChart>(R.id.chart1)

            // background color
            chart?.setBackgroundColor(Color.WHITE)
//            chart?.setDrawGridBackground(false)
            chart?.setDrawBorders(false)
            chart?.axisLeft?.setDrawAxisLine(false)

            // disable description text
//            chart.getDescription().setEnabled(false)

            // enable touch gestures
//            chart.setTouchEnabled(true)

            // set listeners
//            chart.setOnChartValueSelectedListener(this)
            chart?.setDrawGridBackground(false)

            // create marker to display box when values are selected
//            val mv = MyMarkerView(this, R.layout.custom_marker_view)
//
//            // Set the marker to the chart
//            mv.setChartView(chart)
//            chart.setMarker(mv)

            // enable scaling and dragging
//            chart.setDragEnabled(true)
//            chart.setScaleEnabled(true)
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
//            chart.setPinchZoom(true)
        }

        var xAxis: XAxis
        run {   // // X-Axis Style // //
            xAxis = chart!!.xAxis

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
        }

        var yAxis: YAxis
        run {   // // Y-Axis Style // //
            yAxis = chart!!.axisLeft

            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(true)

            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f)
            yAxis.valueFormatter = MyAxisValueFormatter()
            // axis range
            yAxis.setAxisMaximum(200f)
            yAxis.setAxisMinimum(-50f)
            yAxis.setLabelCount(8, true)
        }

        setData(45, 180f)

//        val rightAxis: YAxis = chart.getAxisRight()
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(tfLight);
//        rightAxis.setLabelCount(8, false);
        //        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(tfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(MyAxisValueFormatter())
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
    }

    private fun setData(count: Int, range: Float) {
        val values: ArrayList<Entry> = ArrayList<Entry>()
        for (i in 0 until count) {
            val value = (Math.random() * range).toFloat()
            values.add(Entry(i.toFloat(), value, null))
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
            set1.setCircleColor(Color.BLACK)

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
            set1.setValueTextSize(9f)

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
//            set1.setDrawFilled(true)
//            set1.setFillFormatter(object : IFillFormatter() {
//                fun getFillLinePosition(dataSet: ILineDataSet?, dataProvider: LineDataProvider?): Float {
//                    return chart.axisLeft.getAxisMinimum()
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

            val yAxis = chart!!.axisLeft
            yAxis.setAxisMaximum(220f)
            yAxis.setAxisMinimum(-10f)

            val dataSets: ArrayList<ILineDataSet> = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart?.data = data
        }
    }

}