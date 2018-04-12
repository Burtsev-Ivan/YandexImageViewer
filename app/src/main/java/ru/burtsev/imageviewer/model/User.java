
package ru.burtsev.imageviewer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("twitter_username")
    @Expose
    private String twitterUsername;
    @SerializedName("portfolio_url")
    @Expose
    private String portfolioUrl;
    @SerializedName("bio")
    @Expose
    private Object bio;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("profile_image")
    @Expose
    private ProfileImage profileImage;
    @SerializedName("total_collections")
    @Expose
    private Integer totalCollections;
    @SerializedName("instagram_username")
    @Expose
    private String instagramUsername;
    @SerializedName("total_likes")
    @Expose
    private Integer totalLikes;
    @SerializedName("total_photos")
    @Expose
    private Integer totalPhotos;


}
