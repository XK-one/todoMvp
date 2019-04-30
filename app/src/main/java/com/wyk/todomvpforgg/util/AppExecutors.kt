package com.wyk.todomvpforgg.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Executors类提供了一系列静态工厂方法用于创建各种线程池
 */
const val THREAD_COUNT = 3
class AppExecutors constructor(
            val mDiskIO: Executor = DiskIOThreadExecutor(),
            val mNetworkIO: Executor = Executors.newFixedThreadPool(THREAD_COUNT),
            val mMainThread: Executor = MainThreadExecutor()) {

    class MainThreadExecutor: Executor{
        private val mHandleThread: Handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable?) {
            mHandleThread.post(command)
        }

    }


}