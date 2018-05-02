package ru.burtsev.imageviewer.rest.services;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.burtsev.imageviewer.model.Photo;
import ru.burtsev.imageviewer.model.PhotoResponse;

public interface ApiService {

    @GET("/photos")
    Observable<List<Photo>> getPhotos();

    @GET("/search/photos")
    Observable<PhotoResponse> getPhotos(@Query("page") int page, @Query("query") String query);

    @GET("/search/photos")
    Observable<Photo> getPhotos(@Query("query") String search);

}
