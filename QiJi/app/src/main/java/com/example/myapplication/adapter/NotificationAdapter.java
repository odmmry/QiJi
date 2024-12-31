package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Notification;
import com.example.myapplication.util.DateUtil;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private Context context;
    private List<Notification> list;

    public NotificationAdapter(Context context, List<Notification> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
            holder = new ViewHolder();
            holder.tvContent = convertView.findViewById(R.id.tvContent);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Notification notification = list.get(position);
        holder.tvContent.setText(String.format("您已成功报名比赛：%s，地点在：%s", notification.getName(), notification.getAddress()));
        holder.tvTime.setText(DateUtil.convertTimeToString(notification.getCreateTime()));

        return convertView;
    }

    static class ViewHolder {
        TextView tvContent;
        TextView tvTime;
    }
}
