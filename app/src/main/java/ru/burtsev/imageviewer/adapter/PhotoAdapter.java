package ru.burtsev.imageviewer.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.burtsev.imageviewer.R;
import ru.burtsev.imageviewer.adapter.holder.PhotoHolder;
import ru.burtsev.imageviewer.interfaces.OnItemClickListener;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

    private List<String> urls = new ArrayList<>();
    private OnItemClickListener<String> onItemClickListener;

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_photo, parent, false);
        PhotoHolder groupHolder = new PhotoHolder(v);
        groupHolder.itemView.setOnClickListener(v1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(urls.get(groupHolder.getAdapterPosition()));
            }
        });

        return groupHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        String url = urls.get(position);
        int v = (int) convertDpToPixel(200, holder.imageView.getContext());
        Picasso.get()
                .load(url)
                .resize(v, v)
                .centerCrop()
                .into(holder.imageView);


//        DisplayMetrics displaymetrics = holder.itemView.getContext().getResources().getDisplayMetrics();
//        int devicewidth = displaymetrics.widthPixels / 2;
//        holder.imageView.getLayoutParams().width = devicewidth;
//        holder.imageView.getLayoutParams().height = devicewidth;

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }


    public void setData(List<String> groups) {
        this.urls = groups;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnItemClickListener<String> onClickListener) {
        this.onItemClickListener = onClickListener;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}
