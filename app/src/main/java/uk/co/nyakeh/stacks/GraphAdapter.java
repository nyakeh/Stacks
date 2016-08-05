package uk.co.nyakeh.stacks;

import com.robinhood.spark.SparkAdapter;

public class GraphAdapter extends SparkAdapter {
    private float[] yData;
    private int yBaseLine = 2500;

    public GraphAdapter(float[] data, int baseLine) {
        this.yData = data;
        this.yBaseLine = baseLine;
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }

    @Override
    public boolean hasBaseLine() {
        return true;
    }

    @Override
    public float getBaseLine() {
        return yBaseLine;
    }
}