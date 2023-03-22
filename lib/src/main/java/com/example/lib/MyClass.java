package com.example.lib;

import java.awt.Point;

import jdk.internal.net.http.common.Log;

public class MyClass {


    public static void main(String[] args) {
        Point p = new Point(0,0);
        MyClass myClass = new MyClass();
        myClass.isNeedTranslateMap(p);
    }

    public float leftBorder =0f;
    public float rightBorder = 13f;
    public float topBorder = 0f;
    public float bottomBorder = 8f;

    private boolean isNeedTranslateMap(Point v)
    {

        if (v.x < leftBorder)
        {
            leftBorder -= 13;
            rightBorder -= 13;
            printLog(v, 1);
            return true;
        }

        if (v.x > rightBorder)
        {
            leftBorder += 13;
            rightBorder += 13;
            printLog(v, 2);
            return true;
        }
        if (v.y > topBorder)
        {
            topBorder += 8;
            bottomBorder += 8;
            printLog(v, 3);
            return true;
        }

        if (v.y < bottomBorder)
        {
            topBorder -= 8;
            bottomBorder -= 8;
            printLog(v, 4);
            return true;
        }
        return false;
    }

    void printLog(Point v, int index) {
        System.out.println(index + "translate position:" + v.toString());
       System.out.println(index + "leftborder :" + leftBorder);
       System.out.println(index + "rightborder :" + rightBorder);
       System.out.println(index + "topborder :" + topBorder);
       System.out.println(index + "bottomborder :" + bottomBorder);

    }
}