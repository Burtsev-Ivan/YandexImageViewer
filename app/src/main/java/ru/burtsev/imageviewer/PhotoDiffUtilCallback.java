package ru.burtsev.imageviewer;

import android.support.v7.util.DiffUtil;

import ru.burtsev.imageviewer.model.Photo;

public class PhotoDiffUtilCallback extends DiffUtil.ItemCallback<Photo> {

    @Override
    public boolean areItemsTheSame(Photo oldItem, Photo newItem) {
        return oldItem.getUrls().getRegular().equals(newItem.getUrls().getRegular())
                && oldItem.getUrls().getSmall().equals(newItem.getUrls().getSmall());
    }

    @Override
    public boolean areContentsTheSame(Photo oldItem, Photo newItem) {
        return oldItem.getUrls().getRegular().equals(newItem.getUrls().getRegular())
                && oldItem.getUrls().getSmall().equals(newItem.getUrls().getSmall());
    }
}