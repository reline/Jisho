package com.github.reline.jisho.persistence

import com.github.reline.jisho.JishoDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SqlDelightDao @Inject constructor(private val database: JishoDatabase) : JapaneseMultilingualDao