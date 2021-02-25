/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.reline.jisho.R
import com.github.reline.jisho.databinding.ActivityMainBinding
import com.github.reline.jisho.models.Result
import com.github.reline.jisho.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setContent(viewModel.wordList, viewModel.showNoMatch, viewModel.showProgressBar, viewModel.showLogo)

        lifecycleScope.launch {
            viewModel.hideKeyboardCommand.asFlow().collect { hideKeyboard() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val offlineModeItem = menu.findItem(R.id.action_offline_mode)
        offlineModeItem?.isChecked = viewModel.isOfflineModeEnabled

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setQuery(viewModel.searchQuery, false) // restore the query
        searchView.queryHint = getString(R.string.search)
        searchView.maxWidth = Integer.MAX_VALUE // allow onSearchClicked view to match the width of the toolbar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onSearchQueryChanged(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearchClicked(query)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_offline_mode -> {
                item.isChecked = !item.isChecked
                viewModel.onOfflineModeToggled(item.isChecked)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

fun MainActivity.setContent(
    binding: ActivityMainBinding,
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