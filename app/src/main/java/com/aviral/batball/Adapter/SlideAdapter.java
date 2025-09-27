package com.aviral.batball.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.batball.R;
import com.aviral.batball.Logic.Slide;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<Slide> slideList;

    public SlideAdapter(List<Slide> slideList) {
        this.slideList = slideList;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Slide slide = slideList.get(position);
        holder.image.setImageResource(slide.getImageResId());
        holder.text.setText(slide.getText());
    }

    @Override
    public int getItemCount() {
        return slideList.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageSlide);
            text = itemView.findViewById(R.id.textSlide);
        }
    }
}
