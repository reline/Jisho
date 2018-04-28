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