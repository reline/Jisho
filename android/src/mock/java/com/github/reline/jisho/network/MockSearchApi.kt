package com.github.reline.jisho.network

import com.github.reline.jisho.models.Attribution
import com.github.reline.jisho.models.Japanese
import com.github.reline.jisho.models.Sense
import com.github.reline.jisho.models.Word
import com.github.reline.jisho.network.responses.Meta
import com.github.reline.jisho.network.responses.SearchResponse
import com.github.reline.jisho.network.services.SearchApi
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockSearchApi @Inject constructor() : SearchApi {
    override suspend fun searchQuery(query: String): SearchResponse {
        delay(2_000)
        if (query == "no match") {
            return SearchResponse(meta = Meta(0), emptyList())
        }
        return SearchResponse(meta = Meta(0), listOf(
                Word(
                        isCommon = true,
                        tags = listOf("wanikani3"),
                        jlpt = listOf("jlpt-n3", "jlpt-n1"),
                        japanese = listOf(Japanese(
                                word = "今日は",
                                reading = "こんにちは"
                        )),
                        senses = listOf(Sense(
                                englishDefinitions = listOf(
                                        "hello",
                                        "good day",
                                        "good afternoon"
                                ),
                                partsOfSpeech = emptyList(),
                                links = emptyList(),
                                tags = listOf("Usually written using kana alone"),
                                restrictions = emptyList(),
                                seeAlso = emptyList(),
                                antonyms = emptyList(),
                                source = emptyList(),
                                info = listOf("は is pronounced as わ; used during daytime")
                        )),
                        attribution = Attribution(isJmdict = true, isJmnedict = false, dbpedia = "false")
                )
        ))
    }
}