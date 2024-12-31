package com.example.myapplication.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.OrderGoods;
import com.example.myapplication.model.ShopGoods;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private List<Order> list;

    public OrderAdapter(List<Order> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll1;
        private TextView tvAddressName;
        private TextView tvAddressPhone;
        private TextView tvAddressDetail;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);

            ll1 = itemView.findViewById(R.id.ll1);
            tvAddressName = itemView.findViewById(R.id.tvAddressName);
            tvAddressPhone = itemView.findViewById(R.id.tvAddressPhone);
            tvAddressDetail = itemView.findViewById(R.id.tvAddressDetail);
        }

        public void bindData(Order data) {
            tvAddressName.setText(String.format("收件人：%s", data.getAddressName()));
            tvAddressPhone.setText(String.format("联系电话：%s", data.getAddressPhone()));
            tvAddressDetail.setText(String.format("收货地址：%s", data.getAddressDetail()));

            int childCount = ll1.getChildCount();
            for (int i = childCount - 1; i > 1; i--) {
                ll1.removeViewAt(i);
            }
            LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
            for (OrderGoods orderGoods : data.getOrderGoods()) {
                View listItemView = inflater.inflate(R.layout.item_submit_order_goods, ll1, false);
                ImageView imageView = listItemView.findViewById(R.id.imageView);
                TextView tvName = listItemView.findViewById(R.id.tvName);
                TextView tvPrice = listItemView.findViewById(R.id.tvPrice);

                Glide.with(imageView)
                        .load(orderGoods.getImageUrl())
                        .into(imageView);
                tvName.setText(orderGoods.getName());
                tvPrice.setText("¥" + orderGoods.getPrice());

                ll1.addView(listItemView);
            }
        }
    }
}
