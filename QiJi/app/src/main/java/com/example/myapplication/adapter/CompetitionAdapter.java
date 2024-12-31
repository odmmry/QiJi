package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Competition;
import com.example.myapplication.util.DateUtil;

import java.util.List;

public class CompetitionAdapter extends RecyclerView.Adapter<CompetitionAdapter.CompetitionHolder> {

    private List<Competition> list;

    public CompetitionAdapter(List<Competition> list) {
        this.list = list;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
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

        private Competition data;

        private ImageView imageView;
        private TextView tvName;
        private TextView tvInfo;

        public CompetitionHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null && data != null) {
                        onClickListener.onItemClick(data.get_id());
                    }
                }
            });

            imageView = itemView.findViewById(R.id.imageView);
            tvName = itemView.findViewById(R.id.tvName);
            tvInfo = itemView.findViewById(R.id.tvInfo);
        }

        public void bindData(Competition data) {
            this.data = data;

            Glide.with(imageView.getContext())
                    .load(data.getImageUrl())
                    .into(imageView);

            tvName.setText(data.getName());
            tvInfo.setText(String.format("开始日期：%s｜地点：%s", DateUtil.convertTimestampToDateString(data.getStartDate()), data.getAddress()));
        }
    }

    public interface OnClickListener {
        void onItemClick(String id);
    }
}
