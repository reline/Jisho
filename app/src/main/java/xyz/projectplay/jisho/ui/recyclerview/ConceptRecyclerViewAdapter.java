package xyz.projectplay.jisho.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;

public class ConceptRecyclerViewAdapter extends RecyclerView.Adapter<ConceptViewHolder> {

    private final PublishSubject<Concept> onClickSubject = PublishSubject.create();
    private List<Concept> conceptList = new ArrayList<>();

    @NonNull
    @Override
    public ConceptViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ConceptViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_concept, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConceptViewHolder holder, int position) {
        final Concept concept = conceptList.get(position);
        holder.bind(concept);
        holder.itemView.setOnClickListener(view -> onClickSubject.onNext(concept));
    }

    @Override
    public int getItemCount() {
        return conceptList.size();
    }

    @NonNull
    public Observable<Concept> itemClickObservable() {
        return onClickSubject.asObservable();
    }

    public void setConceptList(@NonNull List<Concept> conceptList) {
        int oldSize = this.conceptList.isEmpty() ? 0 : this.conceptList.size() - 1;
        int newSize = conceptList.isEmpty() ? 0 : conceptList.size() - 1;
        this.conceptList = conceptList;

        if (newSize > oldSize) {
            notifyItemRangeChanged(0, oldSize);
            notifyItemRangeInserted(oldSize, newSize);
        } else if (newSize < oldSize) {
            notifyItemRangeChanged(0, newSize);
            notifyItemRangeRemoved(newSize, oldSize);
        } else {
            notifyItemRangeChanged(0, newSize);
        }
    }

    @Override
    public void onViewRecycled(ConceptViewHolder holder) {
        holder.itemView.setOnClickListener(null);
    }
}
