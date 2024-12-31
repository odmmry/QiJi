package com.example.myapplication.api;

import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.util.ExceptionHandlerUtil;

import okhttp3.Response;
import retrofit2.Retrofit;

public abstract class HttpObserver<T> extends ObserverAdapter<T> {

    public abstract void onSucceeded(T data);

    public boolean onFailed(T data, Throwable e) {
        return false;
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        if (isSucceeded(t)) {
            onSucceeded(t);
        } else {
            handleRequest(t,null);
        }
    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);

        handleRequest(null, e);
    }

    private void handleRequest(T data, Throwable error) {
        if (onFailed(data, error)) {

        } else {
            ExceptionHandlerUtil.handlerRequest(data, error);
        }
    }

    private boolean isSucceeded(T t) {
        if (t instanceof Retrofit) {
            Response response = (Response) t;
            if (response.code() >= 200 && response.code() <= 299) {
                return true;
            }
        } else if (t instanceof BaseResponse) {
            BaseResponse response = (BaseResponse) t;
            return response.isSucceeded();
        }
        return false;
    }
}
