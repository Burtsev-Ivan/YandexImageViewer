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
import butterknife.OnClick;
import ru.burtsev.imageviewer.adapter.PhotoAdapter;
import ru.burtsev.imageviewer.adapter.PhotoDiffUtilCallback;
import ru.burtsev.imageviewer.interfaces.OnItemClickListener;
import ru.burtsev.imageviewer.interfaces.RetryCallback;
import ru.burtsev.imageviewer.model.Photo;

public class MainActivity extends AppCompatActivity implements OnItemClickListener<Photo>, RetryCallback {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.view_error)
    View viewError;

    @BindView(R.id.recycler_photos)
    RecyclerView recyclerPhotos;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private PhotosViewModel photosViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.main_activity_title);
        setSupportActionBar(toolbar);

        int recyclerColumn = getRecyclerColumn();

        PhotoDiffUtilCallback photoDiffUtilCallback = new PhotoDiffUtilCallback();
        PhotoAdapter photoAdapter = new PhotoAdapter(photoDiffUtilCallback, this, this);

        recyclerPhotos.setLayoutManager(new GridLayoutManager(this, recyclerColumn));
        recyclerPhotos.setAdapter(photoAdapter);

        photosViewModel = ViewModelProviders.of(this).get(PhotosViewModel.class);
        photosViewModel.getLiveDataNextLoadStatus().observe(this, photoAdapter::setNetworkState);
        photosViewModel.getLiveDataFirstLoadStatus().observe(this, statusLoad -> {
            if (statusLoad == null) {
                return;
            }
            switch (statusLoad) {
                case ERROR:
                    recyclerPhotos.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    viewError.setVisibility(View.VISIBLE);
                    break;

                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    viewError.setVisibility(View.GONE);
                    recyclerPhotos.setVisibility(View.VISIBLE);
                    break;

                case IN_PROGRESS:
                    recyclerPhotos.setVisibility(View.GONE);
                    viewError.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    break;
            }
        });
        photosViewModel.getPhotos().observe(this, photoAdapter::submitList);

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

    @OnClick(R.id.button_retry_loading)
    void retryLoading() {
        photosViewModel.retryLoad();
    }

    @Override
    public void onItemClick(Photo photo) {
        Intent intent = PhotoDetailActivity.getStartIntent(this, photo.getUrls().getRegular());
        startActivity(intent);
    }

    @Override
    public void retry() {
        photosViewModel.retryLoad();
    }
}
