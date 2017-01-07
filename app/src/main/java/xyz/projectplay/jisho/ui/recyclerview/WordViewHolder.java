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

package xyz.projectplay.jisho.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Japanese;
import xyz.projectplay.jisho.models.Sense;
import xyz.projectplay.jisho.models.Word;
import xyz.projectplay.jisho.ui.widgets.AutoResizeTextView;

public class WordViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.word)
    AutoResizeTextView readingTextView;

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

        tagsLayout.removeAllViews();
        for (String tag : word.getTags()) {
            TextView textView = (TextView) View.inflate(itemView.getContext(), R.layout.layout_tag, null);
            textView.setText(tag);
            tagsLayout.addView(textView);
        }

        bindSenses(word.getSenses());
    }

    private void bindSenses(List<Sense> senses) {
        sensesLayout.removeAllViews();
        for (int i = 0; i < senses.size(); i++) {
            Sense sense = senses.get(i);
            TextView textView = new TextView(itemView.getContext());
            String definition = String.valueOf(i + 1) + ". ";
            List<String> englishDefinitions = sense.getEnglishDefinitions();
            for (int k = 0; k < englishDefinitions.size(); k++) {
                definition += englishDefinitions.get(k) + (k < englishDefinitions.size() - 1 ? "; " : "");
            }
            textView.setText(definition);
            textView.setTextIsSelectable(true);
            sensesLayout.addView(textView);
        }
    }
}
