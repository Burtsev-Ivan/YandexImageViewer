package ru.burtsev.imageviewer;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.burtsev.imageviewer.adapter.PhotoAdapter;
import ru.burtsev.imageviewer.adapter.PhotoDiffUtilCallback;
import ru.burtsev.imageviewer.interfaces.OnItemClickListener;
import ru.burtsev.imageviewer.interfaces.RetryCallback;
import ru.burtsev.imageviewer.model.Photo;

public class PhotosFragment extends Fragment implements OnItemClickListener<Photo>, RetryCallback {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.view_error)
    View viewError;

    @BindView(R.id.recycler_photos)
    RecyclerView recyclerPhotos;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private PhotosViewModel photosViewModel;
    private PhotoAdapter photoAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PhotoDiffUtilCallback photoDiffUtilCallback = new PhotoDiffUtilCallback();
        photoAdapter = new PhotoAdapter(photoDiffUtilCallback, this, this);


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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, view);
        toolbar.setTitle(R.string.main_activity_title);

        int recyclerColumn = getRecyclerColumn();
        recyclerPhotos.setLayoutManager(new GridLayoutManager(getActivity(), recyclerColumn));
        recyclerPhotos.setAdapter(photoAdapter);
        return view;
    }

    private int getRecyclerColumn() {
        Display getOrient = getActivity().getWindowManager().getDefaultDisplay();
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
        photosViewModel.retryLoading();
    }

    @Override
    public void onItemClick(Photo photo) {
        Intent intent = PhotoDetailActivity.getStartIntent(getActivity(), photo.getUrls().getRegular());
        startActivity(intent);
    }

    @Override
    public void retry() {
        retryLoading();
    }
}
