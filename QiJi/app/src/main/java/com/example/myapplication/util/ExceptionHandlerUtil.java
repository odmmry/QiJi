package com.example.myapplication.util;

import android.text.TextUtils;

import com.example.myapplication.MainApp;
import com.example.myapplication.model.response.BaseResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Response;
import retrofit2.HttpException;

public class ExceptionHandlerUtil {

    public static <T> void handlerRequest(T data, Throwable error) {
        if (error != null) {
            handleException(error);
        } else {
            if (data instanceof Response) {
                Response response = (Response) data;
                int code = response.code();
                if (code >= 200 && code <= 299) {
                } else {
                    ToastUtil.showToast(code + "");
                }
            } else {
                BaseResponse response = (BaseResponse) data;

                if (!response.isSucceeded()) {
                    ToastUtil.showToast(response.getMessage());
                }
            }
        }
    }

    public static void handleException(Throwable error) {
        if (error instanceof UnknownHostException) {
            ToastUtil.showToast("找不到服务器");
        } else if (error instanceof ConnectException) {
            ToastUtil.showToast("你的网络好像不给力");
        } else if (error instanceof SocketTimeoutException) {
            ToastUtil.showToast("连接服务器超时");
        } else if (error instanceof HttpException) {
            HttpException exception = (HttpException) error;

            ToastUtil.showToast(exception.getMessage());
        } else if (error instanceof IllegalArgumentException) {
            ToastUtil.showToast("本地参数错误");
        } else {

            System.out.println("异常了 " + error.getMessage().toString());

            ToastUtil.showToast("未知错误");
        }
    }
}
