package ru.burtsev.imageviewer;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;
import ru.burtsev.imageviewer.rest.RestUtils;

class PhotoDataSource extends PositionalDataSource<Photo> {

    @Getter
    private final MutableLiveData<StatusLoad> liveDataStatus = new MutableLiveData<>();


    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Photo> callback) {
        liveDataStatus.postValue(StatusLoad.IN_PROGRESS);
        RestUtils.getApiService().getPhotos(params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    callback.onResult(photos, 0);
                    liveDataStatus.postValue(StatusLoad.SUCCESS);
                }, throwable -> {
                    liveDataStatus.postValue(StatusLoad.ERROR);
                });
    }


    @SuppressLint("CheckResult")
    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Photo> callback) {
        liveDataStatus.postValue(StatusLoad.IN_PROGRESS);
        RestUtils.getApiService().getPhotos(params.startPosition / 10)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    callback.onResult(photos);
                    liveDataStatus.postValue(StatusLoad.SUCCESS);
                }, throwable -> {
                    liveDataStatus.postValue(StatusLoad.ERROR);
                });
    }

}