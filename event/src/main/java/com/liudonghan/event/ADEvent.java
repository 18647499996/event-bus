package com.liudonghan.event;

import android.os.Bundle;


public class ADEvent {
    public static final int STATUS_FAILED = -1;
    public static final int STATUS_NO_MORE = 0;
    public static final int STATUS_NOT_COMPLETE = 1;
    public static final int STATUS_OK = 2;

    public int status = STATUS_OK;
    public int what;
    public int arg1;
    public int arg2;
    public Object obj;
    public Object extra;
    private Throwable error;

    private Bundle data;

    public ADEvent() {
    }

    public ADEvent(int what) {
        this.what = what;
    }
    public ADEvent(int what, Object obj) {
        this.what = what;
        this.obj = obj;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
