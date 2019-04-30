package com.wyk.test_handler;

import java.lang.ref.WeakReference;

public class Demo {

    public static void main(String[] args){
        testGC1();
        testGC2("testGC2");
    }

    //这里会被GC回收
    public static void testGC1(){
        WeakReference<String> sr = new WeakReference(new String("testGC1"));
        System.out.println(sr.get()); //testGC1
        System.gc();                //通知JVM的gc进行垃圾回收
        System.out.println(sr.get()); //null  已经回收
    }
    //这里不会被GC回收，因为存入弱引用的实例是个强引用，不会回收
    public static void testGC2(String str){
        WeakReference<String> sr = new WeakReference<String>(str);
        System.out.println(sr.get());//testGC2
        System.gc();                 //通知JVM的gc进行垃圾回收
        System.out.println(sr.get());//testGC2
    }


}
