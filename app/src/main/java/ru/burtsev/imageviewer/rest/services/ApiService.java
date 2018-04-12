package ru.burtsev.imageviewer.rest.services;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.burtsev.imageviewer.model.Photo;

public interface ApiService {

    @GET("/photos")
    Observable<Photo> getPhotos();

    @GET("/search/photos")
    Observable<Photo> getPhotos(@Query("query") String search);

}
