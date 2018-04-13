package ru.burtsev.imageviewer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;
import ru.burtsev.imageviewer.rest.RestUtils;

public class PhotosViewModel extends ViewModel {

    private MutableLiveData<List<String>> liveData;
    @Getter
    private final MutableLiveData<StatusLoad> liveDataStatus = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable;

    public PhotosViewModel() {
        compositeDisposable = new CompositeDisposable();
    }


    public LiveData<List<String>> getPhotos() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            loadData();
        }
        return liveData;
    }

    public void loadData() {
        liveDataStatus.postValue(StatusLoad.IN_PROGRESS);
        Disposable subscribe = RestUtils.getApiService().getPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(photos -> {
                    List<String> photoUrls = new ArrayList<>();
                    for (Photo photo : photos) {
                        photoUrls.add(photo.getUrls().getRegular());
                    }
                    return photoUrls;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    liveData.postValue(photos);
                    liveDataStatus.postValue(StatusLoad.SUCCESS);
                }, throwable -> {
                    liveDataStatus.postValue(StatusLoad.ERROR);
                });
        compositeDisposable.add(subscribe);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
