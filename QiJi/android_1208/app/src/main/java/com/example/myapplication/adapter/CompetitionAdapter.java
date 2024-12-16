package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.CompetitionBean;

import java.util.ArrayList;
import java.util.List;

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.CompetitionHolder> {

    private List<CompetitionBean> list;

    public CompetitionAdapter(List<CompetitionBean> list) {
        this.list = list;
    }

    private PostsAdapter.OnClickListener onClickListener;
    private PostsAdapter.OnLongClickListener onLongClickListener;

    public void setOnClickListener(PostsAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CompetitionAdapter.CompetitionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_competition, parent, false);
        return new CompetitionAdapter.CompetitionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompetitionAdapter.CompetitionHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CompetitionHolder extends RecyclerView.ViewHolder {

        private CompetitionBean data;


        public CompetitionHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null && data != null) {
                        onClickListener.onItemClick(data.getId());
                    }
                }
            });
        }

        public void bindData(CompetitionBean data) {
            this.data = data;

        }
    }

    public interface OnClickListener {
        void onItemClick(int id);
    }
}
