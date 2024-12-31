package com.example.myapplication.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private List<Cart> list;

    public CartAdapter(List<Cart> list) {
        this.list = list;
    }

    private OnClickListener onClickListener;


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CartHolder extends RecyclerView.ViewHolder {

        private Cart data;

        private ImageView ivGoods;
        private TextView tvName;
        private TextView tvPrice;
        private ImageView ivDelete;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            ivGoods = itemView.findViewById(R.id.ivGoods);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null && data != null) {
                        onClickListener.onDeleteClick(data.get_id());
                    }
                }
            });
        }

        public void bindData(Cart data) {
            this.data = data;

            if (data.getImgList() != null && data.getImgList().size() > 0) {
                Glide.with(ivGoods.getContext())
                        .load(data.getImgList().get(0))
                        .into(ivGoods);
            } else {
                ivGoods.setImageDrawable(new ColorDrawable(Color.parseColor("#aaaaaa")));
            }

            tvName.setText(data.getName());
            tvPrice.setText("¥" + data.getPrice());
        }
    }

    public interface OnClickListener {
        void onDeleteClick(String id);
    }
}
