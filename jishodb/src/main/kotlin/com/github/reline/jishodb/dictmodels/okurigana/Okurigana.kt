package com.github.reline.jishodb.dictmodels.okurigana

class OkuriganaEntry(
        val text: String,
        val reading: String,
        val furigana: List<Okurigana>
)

class Okurigana(
        val ruby: String,
        val rt: String? = null
)