package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.ShopGoods;

import java.util.List;

public class ShopGoodsAdapter extends BaseAdapter {

    private Context context;
    private List<ShopGoods> list;

    public ShopGoodsAdapter(Context context, List<ShopGoods> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_goods, parent, false);
            holder = new ViewHolder();
            holder.ivGoods = convertView.findViewById(R.id.ivGoods);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.tvPrice = convertView.findViewById(R.id.tvPrice);
            holder.ivAvatar = convertView.findViewById(R.id.ivAvatar);
            holder.tvNickname = convertView.findViewById(R.id.tvNickname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShopGoods shopGoods = list.get(position);

        holder.tvName.setText(shopGoods.getName());
        holder.tvPrice.setText("¥" + shopGoods.getPrice());


        if (shopGoods.getImgList() != null && shopGoods.getImgList().size() > 0) {
            Glide.with(holder.ivGoods.getContext())
                    .load(shopGoods.getImgList().get(0))
                    .into(holder.ivGoods);
        } else {
            holder.ivGoods.setImageDrawable(new ColorDrawable(Color.parseColor("#aaaaaa")));
        }

        if (shopGoods.getAvatarUrl() == null || shopGoods.getAvatarUrl().isEmpty()) {
            Glide.with(holder.ivAvatar.getContext())
                    .load(holder.ivAvatar.getContext().getDrawable(R.drawable.user))
                    .into(holder.ivAvatar);
        } else {
            Glide.with(holder.ivAvatar.getContext())
                    .load(shopGoods.getAvatarUrl())
                    .into(holder.ivAvatar);
        }
        holder.tvNickname.setText(shopGoods.getNickname());

        return convertView;
    }

    static class ViewHolder {
        ImageView ivGoods;
        TextView tvName;
        TextView tvPrice;
        ImageView ivAvatar;
        TextView tvNickname;
    }
}
