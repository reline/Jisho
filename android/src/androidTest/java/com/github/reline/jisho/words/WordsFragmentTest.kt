package com.github.reline.jisho.words

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.reline.jisho.launchFragmentInHiltContainer
import com.github.reline.jisho.words.WordsFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class WordsFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun smokeTest() {
        val scenario = launchFragmentInHiltContainer<WordsFragment>()
    }
}