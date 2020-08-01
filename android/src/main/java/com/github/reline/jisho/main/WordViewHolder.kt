/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Result
import com.github.reline.jisho.models.Sense
import kotlinx.android.synthetic.main.card_word.view.*

class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(result: Result) {
        with(itemView) {
            if (result.okurigana != null) {
                cardFuriganaTextView.visibility = View.VISIBLE
                cardFuriganaTextView.text = result.okurigana
            } else {
                cardFuriganaTextView.visibility = View.GONE
            }
            cardReadingTextView.text = result.japanese

            cardCommonTextView.visibility = if (result.isCommon) View.VISIBLE else View.GONE

            bindTags(result.tags)
            bindSenses(result.senses)
        }
    }

    private fun bindTags(tags: List<String>) {
        with(itemView) {
            cardTagsLayout.removeAllViews()
            for (tag in tags) {
                val textView = View.inflate(context, R.layout.layout_tag, null) as TextView
                textView.text = tag
                cardTagsLayout.addView(textView)
            }
        }
    }

    private fun bindSenses(senses: List<Sense>) {
        with(itemView) {
            cardSensesLayout.removeAllViews()
            for (i in senses.indices) {
                val sense = senses[i]

                bindPartsOfSpeech(sense.partsOfSpeech)

                val definitionTextView = TextView(context)
                val definition = StringBuilder().append(i + 1).append(" ")
                val englishDefinitions = sense.englishDefinitions
                for (k in englishDefinitions.indices) {
                    val separator = if (k < englishDefinitions.size - 1) "; " else ""
                    definition.append(englishDefinitions[k]).append(separator)
                }
                definitionTextView.text = definition
                definitionTextView.setTextIsSelectable(true)
                cardSensesLayout.addView(definitionTextView)
            }
        }
    }

    private fun bindPartsOfSpeech(pos: List<String>) {
        with(itemView) {
            if (pos.isEmpty()) return
            val posTextView = TextView(context)
            val partsOfSpeech = StringBuilder()
            for (i in pos.indices) {
                val separator = if (i < pos.size - 1) ", " else ""
                partsOfSpeech.append(pos[i]).append(separator)
            }
            posTextView.text = partsOfSpeech.toString()
            posTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            posTextView.setTextIsSelectable(true)
            cardSensesLayout.addView(posTextView)
        }
    }
}
