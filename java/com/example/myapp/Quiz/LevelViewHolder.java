package com.example.myapp.Quiz;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;

public class LevelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView level_name;
    public ImageView level_image;

    private ItemClickListener itemClickListener;

    public LevelViewHolder(View itemView) {
        super(itemView);

        level_image = (ImageView)itemView.findViewById(R.id.level_image);
        level_name = (TextView)itemView.findViewById(R.id.level_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
