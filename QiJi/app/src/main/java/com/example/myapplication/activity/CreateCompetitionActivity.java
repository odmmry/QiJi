package com.example.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateCompetitionActivity extends BaseActivity {


    private RadioGroup radioGroup;
    private ImageView imageView;
    private EditText etStartDate;
    private EditText etEndDate;
    private EditText etName;
    private EditText etAddress;
    private EditText etDesc;


    private String imageUrl;
    private long startDate = 0l;
    private long endDate = 0;


    public static final int REQUEST_CODE_CHOOSE_IMAGE_FROM_GALLERY = 2346;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        imageView = findViewById(R.id.imageView);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etDesc = findViewById(R.id.etDesc);

        imageView.setOnClickListener(new View.OnClickListener() {
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

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateCompetitionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(year, month, dayOfMonth);
                                Date selectedDate = selectedCalendar.getTime();
//                                long timestamp = selectedDate.getTime() / 1000;

                                startDate = getDateOnlyTimestamp(selectedDate.getTime());


                                etStartDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateCompetitionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(year, month, dayOfMonth);
                                Date selectedDate = selectedCalendar.getTime();
//                                long timestamp = selectedDate.getTime() / 1000;

                                endDate = getDateOnlyTimestamp(selectedDate.getTime());

                                System.out.println(endDate);


                                etEndDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

                datePickerDialog.show();
            }
        });


        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUrl.isEmpty()) {
                    ToastUtil.showToast("请上传活动封面图片");
                    return;
                }
                if (startDate == 0 || endDate == 0) {
                    ToastUtil.showToast("请选择活动日期");
                    return;
                }
                String name = etName.getText().toString().trim();
                if (name.isEmpty()) {
                    ToastUtil.showToast("请填写活动名称");
                    return;
                }
                String address = etAddress.getText().toString().trim();
                if (address.isEmpty()) {
                    ToastUtil.showToast("请填写活动地点");
                    return;
                }
                String desc = etDesc.getText().toString().trim();
                if (desc.isEmpty()) {
                    ToastUtil.showToast("请填写活动描述");
                    return;
                }

                showLoading();

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");


                String type = "";
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.radio1) {
                    type = "1";
                } else if (checkedRadioButtonId == R.id.radio2) {
                    type = "2";
                }


                DefaultRepository.getInstance().createCompetition(type, imageUrl, startDate, endDate, name, address, desc, userId)
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
                                        .into(imageView);
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

    private long getDateOnlyTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long dateOnlyTimestampInMillis = calendar.getTimeInMillis();
        return (dateOnlyTimestampInMillis / 1000);
    }
}