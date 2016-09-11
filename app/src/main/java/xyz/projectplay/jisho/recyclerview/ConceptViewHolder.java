package xyz.projectplay.jisho.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;

public class ConceptViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.reading)
    TextView reading;

    @BindView(R.id.furigana)
    LinearLayout furigana;

    @BindView(R.id.tag)
    TextView tag;

    @BindView(R.id.meanings)
    LinearLayout meanings;

    public ConceptViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
