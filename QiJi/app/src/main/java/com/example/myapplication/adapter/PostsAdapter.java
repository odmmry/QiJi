package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.PostItem;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.CommunityHolder> {

    private List<PostItem> list;

    public PostsAdapter(List<PostItem> list) {
        this.list = list;
    }

    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public CommunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new CommunityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CommunityHolder extends RecyclerView.ViewHolder {

        private PostItem data;


        private ImageView ivAvatar;
        private TextView tvNickname;
        private TextView tvDatetime;
        private TextView tvContent;
        private GridView gridView;
        private GridViewImageAdapter gridViewImageAdapter;
        private List<String> imgs = new ArrayList<>();

        public CommunityHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvNickname = itemView.findViewById(R.id.tvNickname);
            tvDatetime = itemView.findViewById(R.id.tvDatetime);
            tvContent = itemView.findViewById(R.id.tvContent);
            gridView = itemView.findViewById(R.id.gridView);

            gridViewImageAdapter = new GridViewImageAdapter(itemView.getContext(), imgs);
            gridView.setAdapter(gridViewImageAdapter);
            gridView.setClickable(false);
            gridView.setPressed(false);
            gridView.setEnabled(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null && data != null) {
                        onClickListener.onItemClick(data.get_id());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null && data != null) {
                        onLongClickListener.onItemLongClick(data.get_id());
                    }
                    return true;
                }
            });
        }

        public void bindData(PostItem data) {
            this.data = data;

            tvNickname.setText(data.getNickname());

            if (data.getAvatarUrl() == null || data.getAvatarUrl().isEmpty()) {
                Glide.with(ivAvatar.getContext())
                        .load(ivAvatar.getContext().getDrawable(R.drawable.user))
                        .into(ivAvatar);
            } else {
                Glide.with(ivAvatar.getContext())
                        .load(data.getAvatarUrl())
                        .into(ivAvatar);
            }

            tvDatetime.setText(data.getCreateTimeAsString());
            tvContent.setText(data.getContent());

            this.imgs.clear();
            this.imgs.addAll(data.getImgList());
            this.gridViewImageAdapter.notifyDataSetChanged();
        }
    }

    public interface OnClickListener {
        void onItemClick(String id);
    }
    public interface OnLongClickListener {
        void onItemLongClick(String id);
    }
}
