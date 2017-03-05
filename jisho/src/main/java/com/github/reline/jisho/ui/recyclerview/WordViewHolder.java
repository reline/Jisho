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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.reline.jisho.R;
import com.github.reline.jisho.models.Japanese;
import com.github.reline.jisho.models.Sense;
import com.github.reline.jisho.models.Word;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WordViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.word)
    TextView readingTextView;

    @BindView(R.id.furigana)
    TextView furiganaTextView;

    @BindView(R.id.common)
    TextView commonTextView;

    @BindView(R.id.tags)
    LinearLayout tagsLayout;

    @BindView(R.id.senses)
    LinearLayout sensesLayout;

    public WordViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Word word) {
        Japanese japanese = word.getJapanese().get(0); // always use the first result
        if (japanese.getWord() != null) {
            furiganaTextView.setVisibility(View.VISIBLE);
            furiganaTextView.setText(japanese.getReading());
            readingTextView.setText(japanese.getWord());
        } else {
            furiganaTextView.setVisibility(View.GONE);
            readingTextView.setText(japanese.getReading());
        }

        commonTextView.setVisibility(word.isCommon() ? View.VISIBLE : View.GONE);

        bindTags(word.getTags());
        bindSenses(word.getSenses());
    }

    private void bindTags(List<String> tags) {
        tagsLayout.removeAllViews();
        for (String tag : tags) {
            TextView textView = (TextView) View.inflate(itemView.getContext(), R.layout.layout_tag, null);
            textView.setText(tag);
            tagsLayout.addView(textView);
        }
    }

    private void bindSenses(List<Sense> senses) {
        sensesLayout.removeAllViews();
        for (int i = 0; i < senses.size(); i++) {
            Sense sense = senses.get(i);

            bindPartsOfSpeech(sense.getPartsOfSpeech());

            TextView definitionTextView = new TextView(itemView.getContext());
            String definition = String.valueOf(i + 1) + ". ";
            List<String> englishDefinitions = sense.getEnglishDefinitions();
            for (int k = 0; k < englishDefinitions.size(); k++) {
                definition += englishDefinitions.get(k) + (k < englishDefinitions.size() - 1 ? "; " : "");
            }
            definitionTextView.setText(definition);
            definitionTextView.setTextIsSelectable(true);
            sensesLayout.addView(definitionTextView);
        }
    }

    private void bindPartsOfSpeech(List<String> pos) {
        if (pos.isEmpty()) return;
        TextView posTextView = new TextView(itemView.getContext());
        StringBuilder partsOfSpeech = new StringBuilder();
        for (int i = 0; i < pos.size(); i++) {
            partsOfSpeech.append(pos.get(i)).append(i < pos.size() - 1 ? ", " : "");
        }
        posTextView.setText(partsOfSpeech.toString());
        posTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
        posTextView.setTextIsSelectable(true);
        sensesLayout.addView(posTextView);
    }
}
