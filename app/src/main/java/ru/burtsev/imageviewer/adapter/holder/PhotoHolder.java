package ru.burtsev.imageviewer.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.burtsev.imageviewer.R;


public class PhotoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_photo)
    public ImageView imageView;


    public PhotoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
