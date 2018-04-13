package ru.burtsev.imageviewer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.burtsev.imageviewer.adapter.PhotoAdapter;

public class MainActivity extends AppCompatActivity {

    public static int recyclerColumn = 2;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.view_error)
    View viewError;

    @BindView(R.id.recycler_photos)
    RecyclerView recyclerPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Display getOrient = getWindowManager().getDefaultDisplay();
        if (getOrient.getWidth() == getOrient.getHeight()) {
            recyclerColumn = 2;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                recyclerColumn = 2;
            } else {
                recyclerColumn = 4;
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        PhotoAdapter photoAdapter = new PhotoAdapter();
        recyclerPhotos.setLayoutManager(new GridLayoutManager(this, recyclerColumn));
        recyclerPhotos.setAdapter(photoAdapter);

        PhotosViewModel groupsViewModel = ViewModelProviders.of(this).get(PhotosViewModel.class);
        groupsViewModel.getLiveDataStatus().observe(this, statusLoad -> {
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
        groupsViewModel.getPhotos().observe(this, urls -> {
            photoAdapter.setData(urls);
        });
    }


    public int getScreenOrientation() {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
}
