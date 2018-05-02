package ru.burtsev.imageviewer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import lombok.Getter;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.StatusLoad;
import ru.burtsev.imageviewer.paging.PhotoSourceFactory;

public class PhotosViewModel extends ViewModel {

    private LiveData<PagedList<Photo>> liveData;
    @Getter
    private MutableLiveData<StatusLoad> liveDataNextLoadStatus = new MutableLiveData<>();

    @Getter
    private MutableLiveData<StatusLoad> liveDataFirstLoadStatus = new MutableLiveData<>();
    private PhotoSourceFactory sourceFactory;

    public PhotosViewModel() {

    }


    public LiveData<PagedList<Photo>> getPhotos(String category) {
        if (liveData == null) {
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(1)
                    .setPrefetchDistance(5)
                    .build();

            sourceFactory = new PhotoSourceFactory(category, liveDataFirstLoadStatus, liveDataNextLoadStatus);

            return liveData = new LivePagedListBuilder<>(sourceFactory, config).build();
        }
        return liveData;
    }


    public void retryLoad() {
        sourceFactory.getPhotoDataSourceLiveData().getValue().retry();
    }

    public LiveData<PagedList<Photo>> setCategory(String text) {
        liveData = null;
        return getPhotos(text);
    }
}
