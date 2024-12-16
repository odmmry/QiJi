package com.example.myapplication.activity;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.adapter.GridViewImageAdapter;

import java.util.List;

public class PostsDetailActivity extends BaseActivity {

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


        GridViewImageAdapter gridViewImageAdapter = new GridViewImageAdapter(this, List.of("https://cdn.uviewui.com/uview/album/1.jpg", "https://cdn.uviewui.com/uview/album/2.jpg", "https://cdn.uviewui.com/uview/album/3.jpg"));
        GridView gridView = findViewById(R.id.gridView);
        gridView.setClickable(false);
        gridView.setPressed(false);
        gridView.setEnabled(false);
        gridView.setAdapter(gridViewImageAdapter);


        ImageView ivThumb = findViewById(R.id.ivThumb);
        VectorDrawable drawable = (VectorDrawable) ivThumb.getBackground();
        drawable.setTint(getColor(R.color.primary));
    }
}