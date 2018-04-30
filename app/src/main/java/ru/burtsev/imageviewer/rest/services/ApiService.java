package ru.burtsev.imageviewer.rest.services;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.burtsev.imageviewer.model.Photo;

public interface ApiService {

    @GET("/photos")
    Observable<List<Photo>> getPhotos(@Query("page") int page);

}
