package ru.burtsev.imageviewer.adapter;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import ru.burtsev.imageviewer.R;
import ru.burtsev.imageviewer.adapter.holder.NetworkStateItemHolder;
import ru.burtsev.imageviewer.adapter.holder.PhotoHolder;
import ru.burtsev.imageviewer.interfaces.OnItemClickListener;
import ru.burtsev.imageviewer.interfaces.RetryCallback;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;

public class PhotoAdapter extends PagedListAdapter<Photo, RecyclerView.ViewHolder> {

    private OnItemClickListener<Photo> onItemClickListener;
    private RetryCallback retryCallback;
    private StatusLoad networkState;

    public PhotoAdapter(@NonNull DiffUtil.ItemCallback<Photo> diffCallback, OnItemClickListener<Photo> onItemClickListener, RetryCallback retryCallback) {
        super(diffCallback);
        this.retryCallback = retryCallback;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == R.layout.list_item_photo) {
            view = layoutInflater.inflate(R.layout.list_item_photo, parent, false);
            PhotoHolder photoHolder = new PhotoHolder(view);

            photoHolder.itemView.setOnClickListener(v1 -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getItem(photoHolder.getAdapterPosition()));
                }
            });

            return photoHolder;

        } else if (viewType == R.layout.list_item_network_state) {
            view = layoutInflater.inflate(R.layout.list_item_network_state, parent, false);
            return new NetworkStateItemHolder(view, retryCallback);
        } else {
            throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.list_item_network_state;
        } else {
            return R.layout.list_item_photo;
        }
    }

    public void setNetworkState(StatusLoad newNetworkState) {
        StatusLoad previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        return networkState != StatusLoad.SUCCESS;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.list_item_photo:
                PhotoHolder photoHolder = (PhotoHolder) holder;

                Photo photo = getItem(position);
                if (photo != null && photo.getUrls() != null) {
                    Picasso.get()
                            .load(photo.getUrls().getSmall())
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(photoHolder.imageView);
                }

                break;
            case R.layout.list_item_network_state:
                NetworkStateItemHolder networkStateItemHolder = (NetworkStateItemHolder) holder;
                if (networkState == StatusLoad.IN_PROGRESS) {
                    networkStateItemHolder.progressBar.setVisibility(View.VISIBLE);
                    networkStateItemHolder.buttonRetryLoading.setVisibility(View.GONE);
                    networkStateItemHolder.errorMessage.setVisibility(View.GONE);

                } else if (networkState == StatusLoad.ERROR) {
                    networkStateItemHolder.progressBar.setVisibility(View.GONE);
                    networkStateItemHolder.buttonRetryLoading.setVisibility(View.VISIBLE);
                    networkStateItemHolder.errorMessage.setVisibility(View.VISIBLE);
                }
                break;
        }

    }
}