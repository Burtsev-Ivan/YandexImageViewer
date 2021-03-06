package ru.burtsev.imageviewer.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;

public class PhotoSourceFactory extends DataSource.Factory<Integer, Photo> {

    private final MutableLiveData<StatusLoad> liveDataStatus;
    private final MutableLiveData<StatusLoad> liveDataFirstLoadStatus;
    private final String category;

    private MutableLiveData<PhotoDataSource> photoDataSourceLiveData = new MutableLiveData<>();

    public PhotoSourceFactory(String category, MutableLiveData<StatusLoad> liveDataFirstLoadStatus, MutableLiveData<StatusLoad> liveDataStatus) {
        this.liveDataStatus = liveDataStatus;
        this.liveDataFirstLoadStatus = liveDataFirstLoadStatus;
        this.category = category;
    }


    @Override
    public DataSource<Integer, Photo> create() {
        PhotoDataSource photoDataSource = new PhotoDataSource(category, liveDataFirstLoadStatus, liveDataStatus);
        photoDataSourceLiveData.postValue(photoDataSource);
        return photoDataSource;
    }

    @NonNull
    public MutableLiveData<PhotoDataSource> getPhotoDataSourceLiveData() {
        return photoDataSourceLiveData;
    }

}