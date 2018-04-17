package ru.burtsev.imageviewer.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;

public class PhotoSourceFactory extends DataSource.Factory<Integer, Photo> {

    private final MutableLiveData<StatusLoad> liveDataStatus;

    public PhotoSourceFactory(MutableLiveData<StatusLoad> liveDataStatus) {
        this.liveDataStatus = liveDataStatus;
    }


    @Override
    public DataSource create() {
        return new PhotoDataSource(liveDataStatus);
    }

}