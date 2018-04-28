/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui.controllers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.github.reline.jisho.Jisho
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Word
import com.github.reline.jisho.presenters.HomePresenter
import com.github.reline.jisho.ui.controllers.base.BaseController
import com.github.reline.jisho.ui.controllers.base.Layout
import com.github.reline.jisho.ui.recyclerview.WordRecyclerViewAdapter
import com.github.reline.jisho.ui.views.IHomeView
import kotlinx.android.synthetic.main.controller_home.view.*
import java.util.*
import javax.inject.Inject

@Layout(R.layout.controller_home)
class HomeController : BaseController(), IHomeView {

    @Inject
    internal lateinit var presenter: HomePresenter

    private lateinit var adapter: WordRecyclerViewAdapter
    private var query: String? = null

    init {
        Jisho.getInjectionComponent().inject(this)
    }

    override fun onViewBound(view: View) {
        presenter.takeView(this)
        setHasOptionsMenu(true)
        view.homeControllerRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = WordRecyclerViewAdapter()
        view.homeControllerRecyclerView.adapter = adapter
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        query = savedViewState.getString(QUERY)
        val words = savedViewState.getParcelableArrayList<Word>(WORDS)
        if (words != null) {
            updateView(words)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView
        searchView.setQuery(query, false) // restore the query
        searchView.queryHint = getString(R.string.search)
        searchView.maxWidth = Integer.MAX_VALUE // allow search view to match the width of the toolbar
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                view?.homeControllerProgressBar?.visibility = View.VISIBLE
                presenter.search(query)
                hideKeyboard()
                return true
            }
        })
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        outState.putString(QUERY, query)
        outState.putParcelableArrayList(WORDS, adapter.wordList)
    }

    override fun onDestroyView(view: View) {
        presenter.dropView(this)
        super.onDestroyView(view)
    }

    override fun updateView(results: ArrayList<Word>) {
        view?.apply {
            homeControllerLogoTextView.visibility = View.GONE
            adapter.updateData(results)
            homeControllerProgressBar.visibility = View.GONE
            if (results.isEmpty()) {
                homeControllerNoMatchTextView.visibility = View.VISIBLE
                homeControllerNoMatchTextView.text = getString(R.string.no_match)?.format(query)
            } else {
                homeControllerNoMatchTextView.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val QUERY = "HomeController.QUERY"
        private const val WORDS = "HomeController.WORDS"
    }
}
