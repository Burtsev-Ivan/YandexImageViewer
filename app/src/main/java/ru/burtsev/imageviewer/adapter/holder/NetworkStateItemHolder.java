package ru.burtsev.imageviewer.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.burtsev.imageviewer.R;

public class NetworkStateItemHolder extends RecyclerView.ViewHolder {

    public final ProgressBar progressBar;
    public final TextView errorMsg;

    public NetworkStateItemHolder(View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progress_bar);
        errorMsg = itemView.findViewById(R.id.text_error);
    }

}