package com.example.myapplication.api;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.example.myapplication.MainApp;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {

    public static OkHttpClient providerOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);

        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder newRequestBuilder = originalRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8");

                return chain.proceed(newRequestBuilder.build());
            }
        });

        //创建okhttp日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //设置日志等级
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        //添加到网络框架中
        builder.addInterceptor(loggingInterceptor);
        //添加chucker实现应用内显示网络请求信息拦截器
        builder.addInterceptor(new ChuckerInterceptor.Builder(MainApp.getInstance()).build());

        return builder.build();
    }

    public static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://gi1oxe5s8w.hzh.sealos.run")
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .build();
    }
}
