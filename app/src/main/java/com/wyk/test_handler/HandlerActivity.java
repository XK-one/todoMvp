package com.wyk.test_handler;

import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;

public class HandlerActivity extends AppCompatActivity {
    /**
     *  自定义 Handler 时如何有效地避免内存泄漏问题？
     *  回答:
     *      问题1：为什么会引起内存泄漏？
     *      回答: 页面关闭时，由于消息队列还存在未处理/正在处理的message，message无法被回收，而message持有handler的引用，
     *            handler可能是通过匿名内部类声明(内部类持有外部类的引用)，持有activity的引用，导致该页面所属的activity无法被GC正常回收，
     *            即使handler不是通过匿名内部类方式声明，这样handler不持有activity的引用，message还是无法在页面关闭时被回收，同样内存泄漏
     *      总结：Handler内存泄漏的源头在于消息队列还存在未处理/正在处理的message
     *
     *
     *      问题2: message是如何持有handler的引用？Handler又是如何持有Activity的引用？
     *      回答：message类有个成员变量是Handler，当消息被发送的时候，message会持有handler的引用，而只有当loop轮询到messageQueue的该message时，
     *            才会去将message的handler成员变量置null，从而message不再持有handler的引用；一般在Activity中声明handler都通过匿名内部类方式，
     *            而内部类是持有外部类的引用，这样Handler就持有Activity的引用(具体看下面截图)
     *
     *      问题3：在Handler中声明弱引用存储Activity，有什么用
     *      回答：可能在Handler实例的handleMessage函数中，需要用到activity中的成员变量/函数等，所以这时候就需要activity这个实例.
     *
     *      问题4：GC时，弱引用会不会被回收
     *      回答：不一定会被回收，如果存入WeakReference的实例是一个强引用，那GC时~ 弱引用并不会被回收，具体看下面截图
     *
     *      问题5：既然源头是在于消息队列还存在未处理/正在处理的message，页面销毁时清理message不就可以解决了，为什么还要弱引用和静态内部类等方式?
     *      回答： 页面不一定是正常销毁，可能用户突然杀掉App或系统内存不足回收等，这时不走onDestroy函数(默认页面是Activity的情况)，
     *             所以还得弱引用和静态内部类等方式，来防止这一情况的发生
     *
     *      问题6：怎么防止内存泄漏？
     *      回答：两种方式
     *          - 1.页面关闭时，清空消息队列中待处理/正在处理的message
     *          - 2.将handler声明为静态内部类
     *          - 3.在Handler构造函数中传入activity的实例，接着将activity再存入弱引用中
     *
     *
     *          - 1.页面关闭时，清空消息队列中待处理/正在处理的message；
     *          - 2.写个类(例如MyHandler)继承Handler，在activity中初始化该MyHandler
     *          - 3.在Handler构造函数中传入activity的实例，接着将activity再存入弱引用中
     *
     *     问题7:内部线程类持有handler的引用，怎么避免(未完成)
     *
     *     8. 重要：页面销毁时，移除messageQueue的消息很重要(具体看下面截图)
     *
     *
     */

    private String TAG = "gc_TEST_HANDLER";
    private YHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new YHandler(this);
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = "haha";
        mHandler.sendMessageDelayed(msg,9000);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.clearReference();
    }

    /*private Handler mHandler;

    //试试在界面没销毁的时候调System.gc()，消除弱引用
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                    Log.i(TAG, "线程" + msg.what +"接收到消息:"+ msg.arg1);

            }
        };

        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = "haha";
        Message msg2 = Message.obtain();
        msg2.what = 2;
        msg2.obj = "hehe";
        mHandler.sendMessageDelayed(msg, 10000);
        mHandler.sendMessageDelayed(msg2, 10000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }*/
}
