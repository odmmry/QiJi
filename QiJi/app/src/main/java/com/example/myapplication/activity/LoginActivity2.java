//package com.example.myapplication.activity;
//
//import android.content.DialogInterface;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.style.ForegroundColorSpan;
//import android.view.View;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AlertDialog;
//
//import com.amap.api.maps.MapsInitializer;
//import com.example.myapplication.R;
//import com.example.myapplication.util.SPUtil;
//import com.example.myapplication.util.ToastUtil;
//
//public class LoginActivity2 extends BaseActivity {
//
//    private EditText etUsername;
//    private EditText etPassword;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        etUsername = findViewById(R.id.etUsername);
//        etPassword = findViewById(R.id.etPassword);
//
//        findViewById(R.id.tvConfirm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String username = etUsername.getText().toString().trim();
//                String password = etPassword.getText().toString().trim();
//
//                if (username.isEmpty()) {
//                    ToastUtil.showToast("请输入用户名");
//                    return;
//                }
//                if (password.isEmpty()) {
//                    ToastUtil.showToast("请输入登录密码");
//                    return;
//                }
//
//                privacyCompliance();
//            }
//        });
//
//        findViewById(R.id.tvRegister).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(RegisterActivity.class);
//            }
//        });
//    }
//
//    private void privacyCompliance() {
//
//        if (SPUtil.getInstance().getBoolean("need_privacy_compliance", true)) {
//            MapsInitializer.updatePrivacyShow(LoginActivity2.this,true,true);
//            SpannableStringBuilder spannable = new SpannableStringBuilder("\"亲，感谢您对我们一直以来的信任！我们依据最新的监管要求更新了《隐私权政策》，特向您说明如下\n1.为向您提供交易相关基本功能，我们会收集、使用必要的信息；\n2.基于您的明示授权，我们可能会获取您的位置（为您提供骑行定位相关功能等）等信息，您有权拒绝或取消授权；\n3.我们会采取业界先进的安全措施保护您的信息安全；\n4.未经您同意，我们不会从第三方处获取、共享或向提供您的信息；\n");
//            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 31, 38, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            new AlertDialog.Builder(this)
//                    .setTitle("温馨提示")
//                    .setMessage(spannable)
//                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            MapsInitializer.updatePrivacyAgree(LoginActivity2.this, true);
//                            SPUtil.getInstance().putBoolean("need_privacy_compliance", false);
//
//                            handleLogin();
//                        }
//                    })
//                    .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            MapsInitializer.updatePrivacyAgree(LoginActivity2.this, false);
//
//                            SPUtil.getInstance().putBoolean("need_privacy_compliance", true);
//                        }
//                    })
//                    .show();
//        } else {
//            handleLogin();
//        }
//    }
//
//    private void handleLogin() {
//
//    }
//}