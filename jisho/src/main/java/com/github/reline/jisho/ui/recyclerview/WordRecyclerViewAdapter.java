/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.reline.jisho.R;
import com.github.reline.jisho.models.Word;

import java.util.ArrayList;


public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private ArrayList<Word> wordList;

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new WordViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_word, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        final Word word = wordList.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return wordList != null ? wordList.size() : 0;
    }

    public void updateData(@NonNull ArrayList<Word> wordList) {
        int oldSize = getItemCount();
        int newSize = wordList.size();
        this.wordList = wordList;

        if (newSize > oldSize) {
            notifyItemRangeInserted(oldSize, newSize);
            notifyItemRangeChanged(0, oldSize);
        } else if (newSize < oldSize) {
            notifyItemRangeRemoved(newSize, oldSize);
            notifyItemRangeChanged(0, newSize);
        } else {
            notifyItemRangeChanged(0, newSize);
        }
    }

    @Nullable
    public ArrayList<Word> getWordList() {
        return wordList;
    }
}
