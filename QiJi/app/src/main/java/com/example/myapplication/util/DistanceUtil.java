package com.example.myapplication.util;

public class DistanceUtil {

    // 米转换为千米
    public static float metersToKilometers(float meters) {
        return meters / 1000.0f;
    }

    // 米转换为千米（传字符串）
    public static double metersToKilometers(String metersStr) {
        try {
            float meters = Float.parseFloat(metersStr);
            return metersToKilometers(meters);
        } catch (Exception e) {
            return -1;
        }
    }
}
