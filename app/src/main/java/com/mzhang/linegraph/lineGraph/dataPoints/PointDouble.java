package com.mzhang.linegraph.lineGraph.dataPoints;

import com.mzhang.linegraph.lineGraph.chartDefaults.ObjectPool;

import java.util.List;

/**
 * Point encapsulating two double values.
 *
 */
public class PointDouble extends ObjectPool.Poolable {

    private static ObjectPool<PointDouble> pool;

    static {
        pool = ObjectPool.create(64, new PointDouble(0,0));
        pool.setReplenishPercentage(0.5f);
    }

    public static PointDouble getInstance(double x, double y){
        PointDouble result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static void recycleInstance(PointDouble instance){
        pool.recycle(instance);
    }

    public static void recycleInstances(List<PointDouble> instances){
        pool.recycle(instances);
    }

    public double x;
    public double y;

    protected ObjectPool.Poolable instantiate(){
        return new PointDouble(0,0);
    }

    private PointDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * returns a string representation of the object
     */
    public String toString() {
        return "PointDouble, x: " + x + ", y: " + y;
    }
}
