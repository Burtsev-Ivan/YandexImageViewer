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
    private MutableLiveData<StatusLoad> liveDataStatus = new MutableLiveData<>();

    public PhotosViewModel() {
    }


    public LiveData<PagedList<Photo>> getPhotos() {
        if (liveData == null) {
            PagedList.Config config = new PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(1)
                    .setPrefetchDistance(5)
                    .build();


            PhotoSourceFactory sourceFactory = new PhotoSourceFactory(liveDataStatus);
            return liveData = new LivePagedListBuilder(sourceFactory, config).build();
        }
        return liveData;
    }


}
