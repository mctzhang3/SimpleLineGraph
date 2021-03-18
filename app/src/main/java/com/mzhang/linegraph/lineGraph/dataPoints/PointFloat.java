package com.mzhang.linegraph.lineGraph.dataPoints;

import android.os.Parcel;
import android.os.Parcelable;

import com.mzhang.linegraph.lineGraph.chartDefaults.ObjectPool;

import java.util.List;

public class PointFloat extends ObjectPool.Poolable {

    private static ObjectPool<PointFloat> pool;

    public float x;
    public float y;

    static {
        pool = ObjectPool.create(32, new PointFloat(0,0));
        pool.setReplenishPercentage(0.5f);
    }

    public PointFloat() {
    }

    public PointFloat(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static PointFloat getInstance(float x, float y) {
        PointFloat result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static PointFloat getInstance() {
        return pool.get();
    }

    public static PointFloat getInstance(PointFloat copy) {
        PointFloat result = pool.get();
        result.x = copy.x;
        result.y = copy.y;
        return result;
    }

    public static void recycleInstance(PointFloat instance){
        pool.recycle(instance);
    }

    public static void recycleInstances(List<PointFloat> instances){
        pool.recycle(instances);
    }

    public static final Parcelable.Creator<PointFloat> CREATOR = new Parcelable.Creator<PointFloat>() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        public PointFloat createFromParcel(Parcel in) {
            PointFloat r = new PointFloat(0,0);
            r.my_readFromParcel(in);
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        public PointFloat[] newArray(int size) {
            return new PointFloat[size];
        }
    };

    /**
     * Set the point's coordinates from the data stored in the specified
     * parcel. To write a point to a parcel, call writeToParcel().
     * Provided to support older Android devices.
     *
     * @param in The parcel to read the point's coordinates from
     */
    public void my_readFromParcel(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    @Override
    protected ObjectPool.Poolable instantiate() {
        return new PointFloat(0,0);
    }
}
