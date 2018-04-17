package ru.burtsev.imageviewer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.burtsev.imageviewer.adapter.PhotoAdapter;
import ru.burtsev.imageviewer.adapter.PhotoDiffUtilCallback;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.view_error)
    View viewError;

    @BindView(R.id.recycler_photos)
    RecyclerView recyclerPhotos;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.main_activity_title);
        setSupportActionBar(toolbar);

        int recyclerColumn = getRecyclerColumn();

        PhotoDiffUtilCallback photoDiffUtilCallback = new PhotoDiffUtilCallback();
        PhotoAdapter photoAdapter = new PhotoAdapter(photoDiffUtilCallback);

        recyclerPhotos.setLayoutManager(new GridLayoutManager(this, recyclerColumn));
        recyclerPhotos.setAdapter(photoAdapter);

        PhotosViewModel photosViewModel = ViewModelProviders.of(this).get(PhotosViewModel.class);
        photosViewModel.init();
        photosViewModel.getLiveDataStatus().observe(this, photoAdapter::setNetworkState);
        photosViewModel.getLiveData().observe(this, photoAdapter::submitList);

        photoAdapter.setOnClickListener(photo -> {
            Intent intent = PhotoDetailActivity.getStartIntent(this, photo.getUrls().getRegular());
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int getRecyclerColumn() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int column;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            column = 2;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                column = 2;
            } else {
                column = 4;
            }
        }
        return column;
    }

}
