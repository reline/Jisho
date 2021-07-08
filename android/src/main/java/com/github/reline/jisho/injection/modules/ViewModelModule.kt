/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.reline.jisho.injection.ViewModelKey
import com.github.reline.jisho.main.MainViewModel
import com.github.reline.jisho.radicals.RadicalsViewModel
import com.github.reline.jisho.util.JishoViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RadicalsViewModel::class)
    abstract fun bindRadicalsViewModel(viewModel: RadicalsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: JishoViewModelFactory): ViewModelProvider.Factory
}