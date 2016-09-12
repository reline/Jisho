package xyz.projectplay.jisho.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.autoresizetextview.AutoResizeTextView;

public class ConceptViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.reading)
    public AutoResizeTextView reading;

    @BindView(R.id.furigana)
    public LinearLayout furigana;

    @BindView(R.id.tag)
    public TextView tag;

    @BindView(R.id.meanings)
    public LinearLayout meanings;

    public ConceptViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
