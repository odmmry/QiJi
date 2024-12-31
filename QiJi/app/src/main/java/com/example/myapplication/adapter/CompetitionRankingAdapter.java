package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.CompetitionRanking;
import com.example.myapplication.model.RideLog;
import com.example.myapplication.util.DateUtil;
import com.example.myapplication.util.DistanceUtil;

import java.util.List;

public class CompetitionRankingAdapter extends BaseAdapter {

    private Context context;
    private List<CompetitionRanking> list;

    public CompetitionRankingAdapter(Context context, List<CompetitionRanking> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CompetitionRankingAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_competition_ranking, parent, false);
            holder = new CompetitionRankingAdapter.ViewHolder();
            holder.ivAvatar = convertView.findViewById(R.id.ivAvatar);
            holder.tvNickname = convertView.findViewById(R.id.tvNickname);
            holder.tvDistance = convertView.findViewById(R.id.tvDistance);
            convertView.setTag(holder);
        } else {
            holder = (CompetitionRankingAdapter.ViewHolder) convertView.getTag();
        }

        CompetitionRanking competitionRanking = list.get(position);

        if (competitionRanking.getAvatarUrl() == null || competitionRanking.getAvatarUrl().isEmpty()) {
            Glide.with(context)
                    .load(holder.ivAvatar.getContext().getDrawable(R.drawable.user))
                    .into(holder.ivAvatar);
        } else {
            Glide.with(context)
                    .load(competitionRanking.getAvatarUrl())
                    .into(holder.ivAvatar);
        }

        holder.tvNickname.setText(competitionRanking.getNickname());
        holder.tvDistance.setText(String.format("%sKM", DistanceUtil.metersToKilometers(competitionRanking.getDistance())));

        return convertView;
    }

    static class ViewHolder {
        ImageView ivAvatar;
        TextView tvNickname;
        TextView tvDistance;
    }
}
