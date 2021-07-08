/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.words

import android.view.View
import androidx.lifecycle.LiveData
import com.github.reline.jisho.R
import com.github.reline.jisho.databinding.FragmentWordsBinding
import com.github.reline.jisho.models.Result

fun WordsFragment.setContent(
    binding: FragmentWordsBinding,
    wordList: LiveData<List<Result>>,
    showNoMatch: LiveData<String?>,
    showProgressBar: LiveData<Boolean>,
    showLogo: LiveData<Boolean>
) {
    val adapter = WordRecyclerViewAdapter()

    wordList.observe(this, {
        adapter.updateData(it)
    })

    binding.recycler.adapter = adapter

    showNoMatch.observe(this) {
        if (it == null) {
            binding.noMatch.visibility = View.GONE
        } else {
            binding.noMatch.text = getString(R.string.no_match).format(it)
            binding.noMatch.visibility = View.VISIBLE
        }
    }

    showProgressBar.observe(this) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }

    showLogo.observe(this) {
        binding.logo.visibility = if (it) View.VISIBLE else View.GONE
    }
}