package com.example.myapplication.activity;

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
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;

import com.example.myapplication.R;
import com.example.myapplication.adapter.GridViewImageAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateGoodsActivity extends BaseActivity {

    private EditText etName;
    private EditText etPrice;
    private GridView gridView;
    private GridViewImageAdapter gridViewImageAdapter;
    private List<String> imgList = new ArrayList<>();

    private ActivityResultLauncher<Intent> imgLauncher;

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
        imgLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result
                    .getData() != null) {

                Uri imageUri = result.getData().getData();


                long fileSize = 0;
                try {
                    fileSize = getFileSize(imageUri);
                } catch (Exception e) {
                    Toast.makeText(CreateGoodsActivity.this, "Error", Toast.LENGTH_SHORT);
                    return;
                }
                if (fileSize > 5 * 1024 * 1024) {
                    Toast.makeText(CreateGoodsActivity.this, "文件不能超过 5MB", Toast.LENGTH_SHORT);
                    return;
                }

                try {
                    Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    float scaleFactor;
                    int originalWidth = originalBitmap.getWidth();
                    int originalHeight = originalBitmap.getHeight();

                    if (originalWidth > originalHeight && originalWidth > 700) {
                        scaleFactor = (float) 700 / originalWidth;
                    } else if (originalHeight > originalWidth && originalHeight > 700) {
                        scaleFactor = (float) 700 / originalHeight;
                    } else {
                        scaleFactor = 1;
                    }

                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleFactor, scaleFactor);

                    Bitmap scaledBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, true);

                    if (imgList.size() + 1 >= 9) {
                        imgList.remove(imgList.size() - 1);
                    }
                    imgList.add(0, saveImg(scaledBitmap));
                    gridViewImageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(CreateGoodsActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                }
            }
        });


        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        gridView = findViewById(R.id.gridView);
        imgList.add(getResourcesUri(R.drawable.icon_add_image));
        gridViewImageAdapter = new GridViewImageAdapter(this, imgList);
        gridView.setAdapter(gridViewImageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imgList.size() >= 9) {
                    return;
                }
                if (position == imgList.size() - 1) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    imgLauncher.launch(intent);
                }
            }
        });

        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etName.getText().toString().trim();
                if (content.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请填写内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                String price = etPrice.getText().toString().trim();
                if (price.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请填写价格", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<String> imgs = imgList.stream().collect(Collectors.toList());
                if (imgs.size() < 9) {
                    imgs.remove(imgs.size() - 1);
                }
                if (imgs.isEmpty()) {
                    Toast.makeText(CreateGoodsActivity.this, "请上传图片", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    protected String saveImg(Bitmap bitmap) throws IOException {

        String filePath = "";

        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (directory!= null) {
            File imageFile = new File(directory, UUID.randomUUID().toString().replaceAll("-", "") + ".jpg");

            filePath = imageFile.getPath();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
            } catch (IOException e) {
                throw e;
            } finally {
                if (fos!= null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return filePath;
    }

    private long getFileSize(Uri uri) throws IOException {
        long fileSize = 0;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor!= null && cursor.moveToFirst()) {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            if (sizeIndex!= -1) {
                fileSize = cursor.getLong(sizeIndex);
            }
            cursor.close();
        }
        return fileSize;
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }
}