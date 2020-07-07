/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.modules

import com.github.reline.jisho.util.NoOpTree
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
class LoggingModule {
    @Provides
    fun provideLoggingTree(): Timber.Tree {
        return NoOpTree()
    }
}