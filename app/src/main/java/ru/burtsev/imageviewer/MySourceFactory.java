package ru.burtsev.imageviewer;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;

class MySourceFactory extends DataSource.Factory<Integer, Photo> {

    private PhotoDataSource photoDataSource;

    @Override
    public DataSource create() {
        photoDataSource = new PhotoDataSource();
        return photoDataSource;
    }

    public LiveData<StatusLoad> getStatusLoad() {
        return photoDataSource.getLiveDataStatus();
    }
}