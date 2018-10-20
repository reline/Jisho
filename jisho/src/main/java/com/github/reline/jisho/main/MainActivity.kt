/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Word
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {

    @Inject
    internal lateinit var presenter: MainPresenter

    private lateinit var adapter: WordRecyclerViewAdapter
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        presenter.takeView(this)
        homeControllerRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = WordRecyclerViewAdapter()
        homeControllerRecyclerView.adapter = adapter
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState?.getString(QUERY)
        val words: List<Word> = savedInstanceState?.getParcelableArrayList(WORDS) ?: emptyList()
        updateResults(words)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(QUERY, query)
        outState.putParcelableArrayList(WORDS, ArrayList(adapter.wordList))
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        presenter.dropView(this)
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView
        searchView.setQuery(query, false) // restore the query
        searchView.queryHint = getString(R.string.search)
        searchView.maxWidth = Integer.MAX_VALUE // allow onSearchClicked view to match the width of the toolbar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                presenter.onSearchClicked(query)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun showNoMatchView() {
        homeControllerNoMatchTextView.visibility = View.VISIBLE
        homeControllerNoMatchTextView.text = getString(R.string.no_match).format(query)
    }

    override fun hideNoMatchView() {
        homeControllerNoMatchTextView.visibility = View.GONE
    }

    override fun hideLogo() {
        homeControllerLogoTextView.visibility = View.GONE
    }

    override fun showProgressBar() {
        homeControllerProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        homeControllerProgressBar.visibility = View.GONE
    }

    override fun updateResults(results: List<Word>) {
        adapter.updateData(results)
    }

    override fun hideKeyboard() {
        currentFocus?.windowToken?.let {
            val context = applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            context?.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    companion object {
        private const val QUERY = "QUERY"
        private const val WORDS = "WORDS"
    }
}
