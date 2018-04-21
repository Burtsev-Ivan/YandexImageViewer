package ru.burtsev.imageviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoDetailActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "url";

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.image_photo)
    ImageView imageViewPhoto;

    @BindView(R.id.view_error)
    View viewError;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra(EXTRA_URL);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        loadPhoto();


    }

    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }


    @OnClick(R.id.button_retry_loading)
    void retryLoading() {
        loadPhoto();
    }

    private void loadPhoto() {
        progressBar.setVisibility(View.VISIBLE);
        viewError.setVisibility(View.GONE);

        Picasso.get()
                .load(url)
                .fit()
                .centerInside()
                .into(imageViewPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        viewError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        viewError.setVisibility(View.VISIBLE);
                    }
                });
    }

}
