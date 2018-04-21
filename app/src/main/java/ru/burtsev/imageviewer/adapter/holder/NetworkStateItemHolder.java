package ru.burtsev.imageviewer.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.burtsev.imageviewer.R;
import ru.burtsev.imageviewer.interfaces.RetryCallback;

public class NetworkStateItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progress_bar)
    public ProgressBar progressBar;
    @BindView(R.id.text_error)
    public TextView errorMessage;
    @BindView(R.id.button_retry_loading)
    public Button buttonRetryLoading;

    @BindView(R.id.view_error)
    public View viewError;

    public NetworkStateItemHolder(View itemView, RetryCallback retryCallback) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        buttonRetryLoading.setOnClickListener(v -> retryCallback.retry());
    }

}