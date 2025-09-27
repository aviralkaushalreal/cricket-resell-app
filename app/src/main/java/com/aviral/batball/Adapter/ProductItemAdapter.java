package com.aviral.batball.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.batball.R;
import com.aviral.batball.Logic.SellSuccessActivity;
import com.aviral.batball.dataObjects.userProduct;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

public class ProductItemAdapter extends RecyclerView.Adapter<ProductItemAdapter.MyTicketViewHolder> {

    private List<userProduct> productList;

    public ProductItemAdapter(List<userProduct> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new MyTicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTicketViewHolder holder, int position) {
        userProduct product = productList.get(position);

        holder.title.setText(product.title);
        holder.cost.setText("₹"+product.cost);
        holder.desc.setText(product.desc);

        Glide.with(holder.itemView.getContext())
                .load(product.img)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.poster);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), SellSuccessActivity.class);
            intent.putStringArrayListExtra("urls", new ArrayList<>(product.urls));
            intent.putExtra("title", product.title);
            intent.putExtra("cost", product.cost);
            intent.putExtra("type", product.cat);
            intent.putExtra("desc", product.desc);
            intent.putExtra("form", "non_preview");
            intent.putExtra("itemKey", product.itemKey);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }




    static class MyTicketViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, cost, desc;

        public MyTicketViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.product_img);
            title = itemView.findViewById(R.id.product_title);
            cost = itemView.findViewById(R.id.product_cost);
            desc = itemView.findViewById(R.id.product_desc);
        }
    }
}
