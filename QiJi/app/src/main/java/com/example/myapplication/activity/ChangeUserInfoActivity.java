package com.example.myapplication.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.MediaPath;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.model.response.UploadImageResponse;
import com.example.myapplication.util.FileUtil;
import com.example.myapplication.util.SPUtil;
import com.example.myapplication.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChangeUserInfoActivity extends BaseActivity {

    private ImageView ivAvatar;
    private EditText etNickname;

    private String imageUrl;


    public static final int REQUEST_CODE_CHOOSE_IMAGE_FROM_GALLERY = 2346;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivAvatar = findViewById(R.id.ivAvatar);
        etNickname = findViewById(R.id.etNickname);

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickMultiImageIntent = new ActivityResultContracts.PickVisualMedia()
                        .createIntent(
                                getHostActivity(),
                                new PickVisualMediaRequest.Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                        .build());

                startActivityForResult(pickMultiImageIntent, REQUEST_CODE_CHOOSE_IMAGE_FROM_GALLERY);
            }
        });
        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrl.isEmpty()) {
                    ToastUtil.showToast("请上传头像图片");
                    return;
                }
                String nickname = etNickname.getText().toString().trim();
                if (nickname.isEmpty()) {
                    ToastUtil.showToast("请填写活动描述");
                    return;
                }

                showLoading();

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");


                DefaultRepository.getInstance().changeUserInfo(userId, nickname, imageUrl)
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CHOOSE_IMAGE_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ArrayList<MediaPath> pathsFromIntent = getPathsFromIntent(data, false);

                showLoading();

                File file = new File(pathsFromIntent.get(0).getPath());

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part formData = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                DefaultRepository.getInstance().uploadFile(formData)
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                        .subscribe(new HttpObserver<DetailResponse<UploadImageResponse>>() {
                            @Override
                            public void onSucceeded(DetailResponse<UploadImageResponse> data) {

                                hideLoading();

                                imageUrl = data.getData().getImageUrl();

                                Glide.with(getHostActivity())
                                        .load(imageUrl)
                                        .into(ivAvatar);
                            }

                            @Override
                            public boolean onFailed(DetailResponse<UploadImageResponse> data, Throwable e) {

                                hideLoading();

                                return false;
                            }
                        });

            }
        }
    }

    @Nullable
    private ArrayList<MediaPath> getPathsFromIntent(@NonNull Intent data, boolean includeMimeType) {
        ArrayList<MediaPath> paths = new ArrayList<>();

        Uri uri = data.getData();

        if (uri == null) {
            ClipData clipData = data.getClipData();

            if (clipData == null) {
                return null;
            }

            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                uri = data.getClipData().getItemAt(i).getUri();
                if (uri == null) {
                    return null;
                }
                String path = FileUtil.getPathFromUri(this, uri);
                if (path == null) {
                    return null;
                }
                String mimeType = includeMimeType ? getContentResolver().getType(uri) : null;
                paths.add(new MediaPath(path, mimeType));
            }
        } else {
            String path = FileUtil.getPathFromUri(this, uri);
            if (path == null) {
                return null;
            }
            paths.add(new MediaPath(path, null));
        }
        return paths;
    }
}