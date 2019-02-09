/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.github.reline.jisho.R
import com.github.reline.jisho.util.hideKeyboard
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val adapter = WordRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

        mainActivityRecyclerView.adapter = adapter

        viewModel.hideKeyboardCommand.observe(this, Observer { hideKeyboard() })

        viewModel.wordList.observe(this, Observer {
            adapter.updateData(it)
        })

        viewModel.showNoMatchViewCommand.observe(this, Observer {
            mainActivityNoMatchTextView.visibility = View.VISIBLE
            mainActivityNoMatchTextView.text = getString(R.string.no_match).format(it)
        })

        viewModel.hideNoMatchViewCommand.observe(this, Observer {
            mainActivityNoMatchTextView.visibility = View.GONE
        })

        viewModel.showProgressBarCommand.observe(this, Observer {
            mainActivityProgressBar.visibility = View.VISIBLE
        })

        viewModel.hideProgressBarCommand.observe(this, Observer {
            mainActivityProgressBar.visibility = View.GONE
        })

        viewModel.hideLogoCommand.observe(this, Observer {
            mainActivityLogoTextView.visibility = View.GONE
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
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

}
