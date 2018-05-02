package ru.burtsev.imageviewer.paging;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.PhotoResponse;
import ru.burtsev.imageviewer.model.StatusLoad;
import ru.burtsev.imageviewer.rest.RestUtils;

public class PhotoDataSource extends PositionalDataSource<Photo> {

    private static final int DEFAULT_PAGE = 1;
    private String category;

    @Getter
    private MutableLiveData<StatusLoad> liveDataStatus;
    private MutableLiveData<StatusLoad> liveDataFirstLoadStatus;

    private Completable retryCompletable;

    private int pageNumber = 2;


    public PhotoDataSource(String category, MutableLiveData<StatusLoad> liveDataFirstLoadStatus, MutableLiveData<StatusLoad> liveDataStatus) {
        this.liveDataStatus = liveDataStatus;
        this.liveDataFirstLoadStatus = liveDataFirstLoadStatus;
        this.category = category;
        if (category == null) {
            this.category = "random";
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<Photo> callback) {
        liveDataFirstLoadStatus.postValue(StatusLoad.IN_PROGRESS);
        RestUtils.getApiService().getPhotos(DEFAULT_PAGE, category)
                .subscribeOn(Schedulers.io())
                .map(PhotoResponse::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    callback.onResult(photos, 0);
                    liveDataFirstLoadStatus.postValue(StatusLoad.SUCCESS);
                }, throwable -> {
                    liveDataFirstLoadStatus.postValue(StatusLoad.ERROR);
                    setRetry(() -> loadInitial(params, callback));

                });
    }


    @SuppressLint("CheckResult")
    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<Photo> callback) {
        liveDataStatus.postValue(StatusLoad.IN_PROGRESS);
        RestUtils.getApiService().getPhotos(pageNumber++, category)
                .subscribeOn(Schedulers.io())
                .map(PhotoResponse::getResults)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    callback.onResult(photos);
                    liveDataStatus.postValue(StatusLoad.SUCCESS);
                }, throwable -> {
                    liveDataStatus.postValue(StatusLoad.ERROR);
                    setRetry(() -> loadRange(params, callback));
                });
    }


    @SuppressLint("CheckResult")
    public void retry() {
        if (retryCompletable != null) {
            retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> {

                    });
        }
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }
}