package com.aviral.batball.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.batball.Logic.ProductViewActivity;
import com.aviral.batball.R;
import com.aviral.batball.dataObjects.userProduct;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<userProduct> productList;


    public CartAdapter(List<userProduct> productList) {
        this.productList = productList;
    }


    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productlayout, parent, false); // your item XML
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        userProduct product = productList.get(position);
        //holder.itemImage.setImageResource(product.getImgsrc());
        Glide.with(holder.itemView.getContext())
                .load(product.img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.itemImage);
        holder.itemText.setText(product.title);


        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(v.getContext(), ProductViewActivity.class);
            intent.putExtra("type",holder.itemText.getText().toString());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemText;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_img);
            itemText = itemView.findViewById(R.id.item_text);
        }
    }
}
