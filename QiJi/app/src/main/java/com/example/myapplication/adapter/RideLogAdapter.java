package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.RideLog;
import com.example.myapplication.util.DateUtil;
import com.example.myapplication.util.DistanceUtil;

import java.util.List;

public class RideLogAdapter extends BaseAdapter {

    private Context context;
    private List<RideLog> list;

    public RideLogAdapter(Context context, List<RideLog> list) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ride_log, parent, false);
            holder = new ViewHolder();
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvDistance = convertView.findViewById(R.id.tvDistance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RideLog rideLog = list.get(position);
        holder.tvTime.setText(String.format("骑行时间：%s 至 %s", DateUtil.convertTimeToString(rideLog.getStartTime()), DateUtil.convertTimeToString(rideLog.getEndTime())));
        holder.tvDistance.setText(String.format("骑行距离：%s（KM）", DistanceUtil.metersToKilometers(rideLog.getDistance())));

        return convertView;
    }

    static class ViewHolder {
        TextView tvTime;
        TextView tvDistance;
    }
}
