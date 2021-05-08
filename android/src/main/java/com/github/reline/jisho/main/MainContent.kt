package com.github.reline.jisho.main

import android.view.View
import androidx.lifecycle.LiveData
import com.github.reline.jisho.R
import com.github.reline.jisho.databinding.FragmentMainBinding
import com.github.reline.jisho.models.Result

fun MainFragment.setContent(
    binding: FragmentMainBinding,
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