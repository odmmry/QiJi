package com.example.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.amap.api.maps.MapsInitializer;
import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.model.response.LoginResponse;
import com.example.myapplication.util.SPUtil;
import com.example.myapplication.util.ToastUtil;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class LoginActivity extends BaseActivity {

    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyCompliance();
            }
        });

        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.class);
            }
        });
    }

    private void privacyCompliance() {
        MapsInitializer.updatePrivacyShow(LoginActivity.this,true,true);
        SpannableStringBuilder spannable = new SpannableStringBuilder("\"亲，感谢您对我们一直以来的信任！我们依据最新的监管要求更新了《隐私政策》，特向您说明如下\n1.为向您提供交易相关基本功能，我们会收集、使用必要的信息；\n2.基于您的明示授权，我们可能会获取您的位置（为您提供骑行定位相关功能等）等信息，您有权拒绝或取消授权；\n3.我们会采取业界先进的安全措施保护您的信息安全；\n4.未经您同意，我们不会从第三方处获取、共享或向提供您的信息；\n");
        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 31, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage(spannable)
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MapsInitializer.updatePrivacyAgree(LoginActivity.this, true);

                        handleLogin();
                    }
                })
                .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MapsInitializer.updatePrivacyAgree(LoginActivity.this, false);
                    }
                })
                .show();
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            ToastUtil.showToast("请输入用户名");
            return;
        }
        if (password.isEmpty()) {
            ToastUtil.showToast("请输入登录密码");
            return;
        }

        showLoading();

        DefaultRepository.getInstance().login(username, password)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<LoginResponse>>() {
                    @Override
                    public void onSucceeded(DetailResponse<LoginResponse> data) {
                        SPUtil.getInstance().putString("user_id", data.getData().getUserId());

                        startActivityAfterFinishThis(TabbarActivity.class);
                    }

                    @Override
                    public boolean onFailed(DetailResponse<LoginResponse> data, Throwable e) {
                        hideLoading();
                        return false;
                    }
                });
    }
}