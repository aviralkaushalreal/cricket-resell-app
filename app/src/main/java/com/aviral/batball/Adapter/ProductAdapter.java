package com.aviral.batball.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.batball.dataObjects.Product;
import com.aviral.batball.Logic.ProductViewActivity;
import com.aviral.batball.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;


    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productlayout, parent, false); // your item XML
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.itemImage.setImageResource(product.getImgsrc());
        holder.itemText.setText(product.getName());


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

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemText;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_img);
            itemText = itemView.findViewById(R.id.item_text);
        }
    }
}
