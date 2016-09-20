package xyz.projectplay.jisho.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.models.Japanese;
import xyz.projectplay.jisho.models.Sense;

public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<ConceptViewHolder> {

    private Context context;
    private List<Concept> conceptList = new ArrayList<>();

    public ConceptRecyclerViewAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setConceptList(List<Concept> conceptList) {
        this.conceptList = conceptList;
        notifyDataSetChanged();
    }

    @Override
    public ConceptViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_concept, viewGroup, false);

        return new ConceptViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConceptViewHolder holder, int position) {
        final Concept concept = conceptList.get(position);

        Japanese j = concept.getJapanese().get(0);
        String word = j.getWord();

        // TODO: 9/19/16 API BLOCKER: align furigana
        holder.furigana.removeAllViews();
        if (word != null) {
            holder.reading.setText(word);
            TextView furiganaTextView = (TextView) View.inflate(context, R.layout.layout_furigana, null);
            furiganaTextView.setText(j.getReading());
            holder.furigana.addView(furiganaTextView);
        } else {
            holder.reading.setText(j.getReading());
        }

        holder.common.setVisibility(concept.isCommon() ? View.VISIBLE : View.GONE);

        holder.meanings.removeAllViews();
        List<Sense> senses = concept.getSenses();
        int s = senses.size();
        for (int i = 0; i < s; i++) {
            String englishDefinition = String.valueOf(i + 1) + ". ";
            List<String> englishDefinitions = senses.get(i).getEnglishDefinitions();
            int size = englishDefinitions.size();
            for (int k = 0; k < size; k++) {
                englishDefinition = englishDefinition.concat(englishDefinitions.get(k));
                if (k != size - 1) {
                    englishDefinition = englishDefinition.concat("; ");
                }
            }
            TextView meaningTextView = (TextView) View.inflate(context, R.layout.layout_meaning, null);
            meaningTextView.setText(englishDefinition);
            holder.meanings.addView(meaningTextView);
        }
    }

    @Override
    public int getItemCount() {
        return conceptList.size();
    }
}
