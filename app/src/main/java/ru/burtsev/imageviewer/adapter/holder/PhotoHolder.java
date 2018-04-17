package ru.burtsev.imageviewer.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import ru.burtsev.imageviewer.R;


public class PhotoHolder extends RecyclerView.ViewHolder {

    public final ImageView imageView;


    public PhotoHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_photo);
    }
}
