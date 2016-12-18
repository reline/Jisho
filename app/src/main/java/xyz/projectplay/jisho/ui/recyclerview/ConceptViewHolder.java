package xyz.projectplay.jisho.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.ui.widgets.AutoResizeTextView;

public class ConceptViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.reading)
    AutoResizeTextView readingTextView;

    @BindView(R.id.furigana)
    LinearLayout furiganaLayout;

    @BindView(R.id.tag)
    TextView tagTextView;

    @BindView(R.id.meanings)
    LinearLayout meaningsLayout;

    public ConceptViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Concept concept) {
        readingTextView.setText(concept.getReading());

        furiganaLayout.removeAllViews();
        for (String furigana : concept.getFurigana()) {
            TextView textView = (TextView) View.inflate(itemView.getContext(), R.layout.layout_furigana, null);
            textView.setText(furigana);
            furiganaLayout.addView(textView);
        }

        String tag = concept.getTag();
        if (tag != null) {
            tagTextView.setText(tag.toLowerCase());
            tagTextView.setVisibility(View.VISIBLE);
        } else {
            tagTextView.setVisibility(View.GONE);
        }

        meaningsLayout.removeAllViews();
        for (String meaning : concept.getMeanings()) {
            if (meaning.isEmpty()) continue;
            TextView textView = new TextView(itemView.getContext());
            textView.setText(meaning);
            meaningsLayout.addView(textView);
        }
    }
}
