package ru.burtsev.imageviewer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class PhotoResponse {

    @SerializedName("results")
    private List<Photo> results;


}