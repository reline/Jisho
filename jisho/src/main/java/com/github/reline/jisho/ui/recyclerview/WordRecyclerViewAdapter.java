/*
 * Copyright 2016 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
