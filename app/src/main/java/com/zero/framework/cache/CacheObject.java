package com.zero.framework.cache;

/**
 * Created by Zero on 2017/5/30.
 */

public class CacheObject {

    private long timestamp;
    private int period = -1;
    private Object data;

    /**
     * @param data
     * @param period -1 表示永不过期，大于0表示过期的时间，单位分钟
     */
    public CacheObject(Object data, int period) {
        timestamp = System.currentTimeMillis();
        this.data = data;
        this.period = period;
    }

    public Object getObject() {
        return data;
    }

    public boolean isValid() {
        if (period == -1 || System.currentTimeMillis() < (timestamp + period * 60000)) {
            return true;
        }
        return false;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return period;
    }
}
