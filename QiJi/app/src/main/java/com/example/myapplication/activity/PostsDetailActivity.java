package com.example.myapplication.activity;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.adapter.GridViewImageAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.CommentItem;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class PostsDetailActivity extends BaseActivity {

    private PostItem detail;

    private LinearLayout llCommentContainer;
    private ImageView ivThumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_detail);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView ivAvatar = findViewById(R.id.ivAvatar);
        TextView tvNickname = findViewById(R.id.tvNickname);
        TextView tvDatetime = findViewById(R.id.tvDatetime);
        TextView tvContent = findViewById(R.id.tvContent);


        List<String> imgList = new ArrayList<>();
        GridViewImageAdapter gridViewImageAdapter = new GridViewImageAdapter(this, imgList);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setClickable(false);
        gridView.setPressed(false);
        gridView.setEnabled(false);
        gridView.setAdapter(gridViewImageAdapter);



        llCommentContainer = findViewById(R.id.llCommentContainer);

        ivThumb = findViewById(R.id.ivThumb);

        updateThumbColor();

        showLoading();

        DefaultRepository.getInstance().getPostDetail(getIntent().getStringExtra("id"))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<PostItem>>() {
                    @Override
                    public void onSucceeded(DetailResponse<PostItem> data) {
                        hideLoading();

                        detail = data.getData();

                        imgList.addAll(detail.getImgList());
                        gridViewImageAdapter.notifyDataSetChanged();

                        if (detail.getAvatarUrl().isEmpty()) {
                            Glide.with(ivAvatar.getContext())
                                    .load(ivAvatar.getContext().getDrawable(R.drawable.user))
                                    .into(ivAvatar);
                        } else {
                            Glide.with(ivAvatar.getContext())
                                    .load(detail.getAvatarUrl())
                                    .into(ivAvatar);
                        }

                        tvNickname.setText(detail.getNickname());
                        tvDatetime.setText(detail.getCreateTimeAsString());
                        tvContent.setText(detail.getContent());

                        updateThumbColor();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<PostItem> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });


        EditText etCommentContent = findViewById(R.id.etCommentContent);
        etCommentContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String text = v.getText().toString();

                if (text.isEmpty()) return true;


                showLoading();

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");

                DefaultRepository.getInstance().addPostComment(detail.get_id(), text, userId)
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                        .subscribe(new HttpObserver<BaseResponse>() {
                            @Override
                            public void onSucceeded(BaseResponse data) {
                                hideLoading();

                                etCommentContent.setText("");
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etCommentContent.getWindowToken(), 0);

                                loadCommentList();
                            }

                            @Override
                            public boolean onFailed(BaseResponse data, Throwable e) {

                                hideLoading();

                                return false;
                            }
                        });

                return true;
            }
            return false;
        });


        ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();

                // 获取当前登录的用户id
                String userId = SPUtil.getInstance().getString("user_id", "");

                DefaultRepository.getInstance().thumbPost(detail.get_id(), userId)
                        .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                        .subscribe(new HttpObserver<BaseResponse>() {
                            @Override
                            public void onSucceeded(BaseResponse data) {
                                hideLoading();

                                detail.setThumb(!detail.isThumb());
//
                                updateThumbColor();
                            }

                            @Override
                            public boolean onFailed(BaseResponse data, Throwable e) {

                                hideLoading();

                                return false;
                            }
                        });
            }
        });


        loadCommentList();

    }

    private void loadCommentList() {
        DefaultRepository.getInstance().getPostCommentList(getIntent().getStringExtra("id"))
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<CommentItem>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<CommentItem>> data) {
                        int childCount = llCommentContainer.getChildCount();
                        for (int i = childCount - 1; i > 2; i--) {
                            llCommentContainer.removeViewAt(i);
                        }

                        LayoutInflater inflater = LayoutInflater.from(PostsDetailActivity.this);
                        for (CommentItem commentBean : data.getData()) {
                            View listItemView = inflater.inflate(R.layout.item_comment, llCommentContainer, false);
                            ImageView ivAvatar = listItemView.findViewById(R.id.ivAvatar);
                            TextView tvNickname = listItemView.findViewById(R.id.tvNickname);
                            TextView tvDatetime = listItemView.findViewById(R.id.tvDatetime);
                            TextView tvContent = listItemView.findViewById(R.id.tvContent);

//                            tvNickname.setText(commentBean.ge());
                            tvDatetime.setText(commentBean.getCreateTimeAsString());
                            tvContent.setText(commentBean.getContent());
                            llCommentContainer.addView(listItemView);
                        }
                    }
                });
    }

    private void updateThumbColor() {
        VectorDrawable drawable = (VectorDrawable) ivThumb.getBackground();

        if (detail != null && detail.isThumb()) {
            drawable.setTint(getColor(R.color.primary));
        } else {
            drawable.setTint(getColor(R.color.gray));
        }
    }
}