package com.wyk.test_handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * 可以单独抽出一个类或者静态内部类
 */
public class YHandler extends Handler {
    private String TAG = "gc_TEST_HANDLER";

    WeakReference<Activity> mWeakReference;
    public YHandler(Context context){
        mWeakReference = new WeakReference(context);
    }
    @Override
    public void handleMessage(Message msg) {

        Log.i(TAG, "mWeakReference==" + mWeakReference);
        Activity activity = mWeakReference.get();
        Log.i(TAG, "activityCompat== " + activity);
        if(activity != null){ //todo

        }
    }
    //
    public void clearReference(){
        if(mWeakReference != null && mWeakReference.get() != null){
            Activity activityCompat = mWeakReference.get();
            activityCompat = null;
            mWeakReference.clear();
        }
    }
}