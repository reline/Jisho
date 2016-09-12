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

import rx.Observable;
import rx.subjects.PublishSubject;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;

public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<ConceptViewHolder> {

    private Context context;
    final PublishSubject<Concept> onClickSubject = PublishSubject.create();
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
        holder.reading.setText(concept.getReading());

        holder.furigana.removeAllViews();
        for (String furigana : concept.getFurigana()) {
            TextView textView = (TextView) View.inflate(context, R.layout.layout_furigana, null);
            textView.setText(furigana);
            holder.furigana.addView(textView);
        }

        String tag = concept.getTag();
        holder.tag.setText(tag != null ? tag.toLowerCase() : null);
        holder.tag.setVisibility(tag != null ? View.VISIBLE : View.GONE);

        holder.meanings.removeAllViews();
        for (String meaning : concept.getMeanings()) {
            if (meaning.isEmpty()) continue;
            TextView textView = (TextView) View.inflate(context, R.layout.layout_meaning, null);
            textView.setText(meaning);
            holder.meanings.addView(textView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubject.onNext(concept);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conceptList.size();
    }

    public Observable<Concept> itemClickObservable() {
        return onClickSubject.asObservable();
    }
}
