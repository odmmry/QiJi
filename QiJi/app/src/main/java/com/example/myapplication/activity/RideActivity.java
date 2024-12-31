package com.example.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import com.example.myapplication.MainApp;
import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.util.Constant;
import com.example.myapplication.util.DateUtil;
import com.example.myapplication.util.DistanceUtil;
import com.example.myapplication.util.SPUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class RideActivity extends BaseActivity implements AMap.OnMyLocationChangeListener {

    protected String[] needPermissions = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            BACK_LOCATION_PERMISSION
    };
    private static final int PERMISSON_REQUESTCODE = 0;
    // 是否需要检测后台定位权限，设置为true时，如果用户没有给予后台定位权限会弹窗提示
    private boolean needCheckBackLocation = false;
    // 如果设置了target > 28，需要增加这个权限，否则不会弹出"始终允许"这个选择框
    private static String BACK_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * 判断是否需要检测权限，防止不停的弹框
     */
    private boolean isNeedCheck = true;


    private MapView mapView;
    private AMap aMap;


    private String mode; // 模式
    private int target; // 目标（模式2 为分钟，模式3为距离）
    private int totalSecond;
    private int curSecond; // 当前计时秒数

    private TextView tvDistance;
    private TextView tvTime;

    private Long startTime = 0l;


    private Float currentDistance = 0f; // 当前骑行距离，单位米
    private LatLng lastLatLng; // 上次定位坐标


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[] {
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_PHONE_STATE,
                    BACK_LOCATION_PERMISSION
            };
            needCheckBackLocation = true;
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);


        tvDistance = findViewById(R.id.tvDistance);
        tvTime = findViewById(R.id.tvTime);

        mode = getIntent().getStringExtra("mode");
        target = getIntent().getIntExtra("selectedTarget", 0);
        if (mode.equals(Constant.RIDE_MODE_2)) {
            totalSecond = target * 60;
        }
        startTime = DateUtil.getTenDigitTimestamp();

        findViewById(R.id.llStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok();
            }
        });

        MainApp.runOnMainThread(timeTask, 1000);
    }

    @Override
    public void onMyLocationChange(Location location) {
        if (location != null) {
//            System.out.println("onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());


            if (lastLatLng == null) {
                lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }

            float distance = AMapUtils.calculateLineDistance(lastLatLng, new LatLng(location.getLatitude(), location.getLongitude()));

            currentDistance += distance;
            tvDistance.setText(DistanceUtil.metersToKilometers(currentDistance) + "KM");


            if (mode.equals(Constant.RIDE_MODE_3) && currentDistance >= target) {
                MainApp.removeCallbackOnMainThread(timeTask);

                AlertDialog.Builder builder = new AlertDialog.Builder(RideActivity.this);
                builder.setTitle("提示");
                builder.setMessage("骑行目标已达成");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ok();
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }
    }


    Runnable timeTask = new Runnable() {
        @Override
        public void run() {
            curSecond++;

            long minutes = curSecond / (60);
            long seconds = curSecond % 60;
            String timeFormat = String.format("%02d:%02d", minutes, seconds);
            tvTime.setText(timeFormat);

            if (mode.equals(Constant.RIDE_MODE_2) && curSecond >= totalSecond) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RideActivity.this);
                builder.setTitle("提示");
                builder.setMessage("骑行目标已达成");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ok();
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                MainApp.runOnMainThread(timeTask, 1000);
            }
        }
    };

    private void ok() {
        MainApp.removeCallbackOnMainThread(timeTask);

        showLoading();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");

        Long endTime = DateUtil.getTenDigitTimestamp();

        DefaultRepository.getInstance().submitRideLog(mode, startTime, endTime, currentDistance, userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<BaseResponse>() {
                    @Override
                    public void onSucceeded(BaseResponse data) {
                        hideLoading();

                        finish();
                    }

                    @Override
                    public boolean onFailed(BaseResponse data, Throwable e) {
                        hideLoading();

                        return false;
                    }
                });
    }


    private void checkPermissions(String... permissions) {
        try{
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    try {
                        String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                        Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});
                        method.invoke(this, array, 0);
                    } catch (Throwable e) {

                    }
                }
            }

        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    // 获取权限集中需要申请权限的列表
    private List<String> findDeniedPermissions(String[] permissions) {
        try{
            List<String> needRequestPermissonList = new ArrayList<String>();
            if (Build.VERSION.SDK_INT >= 23 && getApplicationInfo().targetSdkVersion >= 23) {
                for (String perm : permissions) {
                    if (checkMySelfPermission(perm) != PackageManager.PERMISSION_GRANTED
                            || shouldShowMyRequestPermissionRationale(perm)) {
                        if(!needCheckBackLocation
                                && BACK_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            }
            return needRequestPermissonList;
        } catch(Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private int checkMySelfPermission(String perm) {
        try {
            Method method = getClass().getMethod("checkSelfPermission", new Class[]{String.class});
            Integer permissionInt = (Integer) method.invoke(this, perm);
            return permissionInt;
        } catch (Throwable e) {
        }
        return -1;
    }

    private boolean shouldShowMyRequestPermissionRationale(String perm) {
        try {
            Method method = getClass().getMethod("shouldShowRequestPermissionRationale", new Class[]{String.class});
            Boolean permissionInt = (Boolean) method.invoke(this, perm);
            return permissionInt;
        } catch (Throwable e) {
        }
        return false;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        try{
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try{
            if (Build.VERSION.SDK_INT >= 23) {
                if (requestCode == PERMISSON_REQUESTCODE) {
                    if (!verifyPermissions(grantResults)) {
                        showMissingPermissionDialog();
                        isNeedCheck = false;
                    }
                }
            }
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    private void showMissingPermissionDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限");

            // 拒绝, 退出应用
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                finish();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });

            builder.setPositiveButton("设置",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startAppSettings();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    });

            builder.setCancelable(false);

            builder.show();
        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    private void startAppSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMap.setOnMyLocationChangeListener(null);
        mapView.onDestroy();

        MainApp.removeCallbackOnMainThread(timeTask);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}