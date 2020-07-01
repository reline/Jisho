/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidSchedulerProvider : SchedulerProvider {
    override fun newThread() = Schedulers.newThread()
    override fun single() = Schedulers.single()
    override fun trampoline() = Schedulers.trampoline()
    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()
    override fun computation() = Schedulers.computation()
    override fun io() = Schedulers.io()
}