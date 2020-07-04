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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.reline.jisho.Jisho
import com.github.reline.jisho.R
import com.github.reline.jisho.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val adapter = WordRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as Jisho).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        mainActivityRecyclerView.adapter = adapter

        viewModel.wordList.observe(this, Observer {
            adapter.updateData(it)
        })

        lifecycleScope.launch {
            viewModel.hideKeyboardCommand.asFlow().collect { hideKeyboard() }
        }

        lifecycleScope.launch {
            viewModel.showNoMatchViewCommand.asFlow().collect {
                mainActivityNoMatchTextView.visibility = View.VISIBLE
                mainActivityNoMatchTextView.text = getString(R.string.no_match).format(it)
            }
        }

        lifecycleScope.launch {
            viewModel.hideNoMatchViewCommand.asFlow().collect {
                mainActivityNoMatchTextView.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.showProgressBarCommand.asFlow().collect {
                mainActivityProgressBar.visibility = View.VISIBLE
            }
        }

        lifecycleScope.launch {
            viewModel.hideProgressBarCommand.asFlow().collect {
                mainActivityProgressBar.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.hideLogoCommand.asFlow().collect {
                mainActivityLogoTextView.visibility = View.GONE
            }
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
