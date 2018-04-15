package ru.burtsev.imageviewer.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import ru.burtsev.imageviewer.R;
import ru.burtsev.imageviewer.adapter.holder.PhotoHolder;
import ru.burtsev.imageviewer.interfaces.OnItemClickListener;
import ru.burtsev.imageviewer.model.Photo;

public class PhotoAdapter2 extends PagedListAdapter<Photo, PhotoHolder> {

    private OnItemClickListener<Photo> onItemClickListener;

    public PhotoAdapter2(@NonNull DiffUtil.ItemCallback<Photo> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photo, parent, false);
        PhotoHolder photoHolder = new PhotoHolder(v);
        photoHolder.itemView.setOnClickListener(v1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(getItem(photoHolder.getAdapterPosition()));
            }
        });

        return photoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Photo photo = getItem(position);
        if (photo != null && photo.getUrls() != null) {
            int photoSize = (int) convertDpToPixel(200, holder.imageView.getContext());
            Picasso.get()
                    .load(photo.getUrls().getSmall())
                    .resize(photoSize, photoSize)
                    .centerCrop()
                    .into(holder.imageView);
        }
    }


    public void setOnClickListener(OnItemClickListener<Photo> onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}