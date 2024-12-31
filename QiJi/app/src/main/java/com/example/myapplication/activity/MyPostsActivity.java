package com.example.myapplication.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.R;
import com.example.myapplication.adapter.PostsAdapter;
import com.example.myapplication.api.DefaultRepository;
import com.example.myapplication.api.HttpObserver;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.util.SPUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import autodispose2.AutoDispose;
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;

public class MyPostsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;
    private List<PostItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        postsAdapter = new PostsAdapter(list);
        recyclerView.setAdapter(postsAdapter);

        postsAdapter.setOnLongClickListener(new PostsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClick(String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPostsActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确认删除帖子吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoading();

                        DefaultRepository.getInstance().delPost(id)
                                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                                .subscribe(new HttpObserver<BaseResponse>() {
                                    @Override
                                    public void onSucceeded(BaseResponse data) {
                                        hideLoading();

                                        int index = -1;
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).get_id().equals(id)) {
                                                index = i;
                                                break;
                                            }
                                        }
                                        if (index > -1) {
                                            list.remove(index);
                                            postsAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public boolean onFailed(BaseResponse data, Throwable e) {
                                        hideLoading();

                                        return false;
                                    }
                                });
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void loadList() {
        list.clear();

        showLoading();

        // 获取当前登录的用户id
        String userId = SPUtil.getInstance().getString("user_id", "");


        DefaultRepository.getInstance().getPostList(userId)
                .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(getHostActivity())))
                .subscribe(new HttpObserver<DetailResponse<List<PostItem>>>() {
                    @Override
                    public void onSucceeded(DetailResponse<List<PostItem>> data) {
                        hideLoading();

                        list.addAll(data.getData());
                        postsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFailed(DetailResponse<List<PostItem>> data, Throwable e) {

                        hideLoading();

                        return false;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadList();
    }
}