package com.example.myapplication.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.example.myapplication.adapter.GridViewImageAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.MediaPath;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.model.response.UploadImageResponse;
import com.example.myapplication.util.FileUtil;
import com.example.myapplication.util.SPUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateGoodsActivity extends BaseActivity {

    private EditText etName;
    private EditText etPrice;
    private EditText etDesc;
    private GridView gridView;
    private GridViewImageAdapter gridViewImageAdapter;
    private List<String> imgList = new ArrayList<>();

    public static final int REQUEST_CODE_CHOOSE_IMAGE_FROM_GALLERY = 2346;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goods);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etDesc = findViewById(R.id.etDesc);
        gridView = findViewById(R.id.gridView);
        imgList.add(FileUtil.getResourcesUri(this, R.drawable.icon_add_image));
        gridViewImageAdapter = new GridViewImageAdapter(this, imgList);
        gridView.setAdapter(gridViewImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imgList.size() >= 9) {
                    return;
                }
                if (position == imgList.size() - 1) {

                    Intent pickMultiImageIntent = new ActivityResultContracts.PickVisualMedia()
                            .createIntent(
                                    CreateGoodsActivity.this,
                                    new PickVisualMediaRequest.Builder()
                                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                                            .build());

                    startActivityForResult(pickMultiImageIntent, REQUEST_CODE_CHOOSE_IMAGE_FROM_GALLERY);
                }
            }
        });

        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请填写名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                String price = etPrice.getText().toString().trim();
                if (price.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请填写价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                String desc = etDesc.getText().toString().trim();
                if (desc.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请填写描述", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> imgs = imgList.stream().collect(Collectors.toList());
                imgs.remove(imgs.size() - 1);

                if (imgs.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                    return;
                }


                showLoading();

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");



                DefaultRepository.getInstance().createGoods(name, price, desc, imgs, userId)
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

                                imgList.add(imgList.size() - 1,data.getData().getImageUrl());

                                gridViewImageAdapter.notifyDataSetChanged();
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