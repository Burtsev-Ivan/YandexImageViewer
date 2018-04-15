package ru.burtsev.imageviewer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;

public class PhotosViewModel extends ViewModel {

    @Getter
    private LiveData<PagedList<Photo>> liveData;
    @Getter
    private MutableLiveData<StatusLoad> liveDataStatus = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable;

    public PhotosViewModel() {
        compositeDisposable = new CompositeDisposable();
    }


    public void init() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(1)
                .setPrefetchDistance(5)
                .build();

        MySourceFactory sourceFactory = new MySourceFactory();
        liveData = new LivePagedListBuilder(sourceFactory, config).build();
//        liveDataStatus = sourceFactory.getStatusLoad();

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
