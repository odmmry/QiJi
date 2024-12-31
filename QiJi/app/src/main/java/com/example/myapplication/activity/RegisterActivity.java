package com.example.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.ToastUtil;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etNickname = findViewById(R.id.etNickname);

        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String nickname = etNickname.getText().toString().trim();

                if (username.isEmpty()) {
                    ToastUtil.showToast("请输入用户名");
                    return;
                }
                if (password.isEmpty()) {
                    ToastUtil.showToast("请输入登录密码");
                    return;
                }
                if (nickname.isEmpty()) {
                    ToastUtil.showToast("请输入昵称");
                    return;
                }


                showLoading();

                DefaultRepository.getInstance().register(username, password, nickname)
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                        .subscribe(new HttpObserver<BaseResponse>() {
                            @Override
                            public void onSucceeded(BaseResponse data) {
                                finish();
                            }

                            @Override
                            public boolean onFailed(BaseResponse data, Throwable e) {
                                hideLoading();
                                return false;
                            }
                        });
            }
        });
    }
}