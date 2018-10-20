package com.github.reline.jisho.persistence.relations

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.github.reline.jisho.persistence.bridgetables.KanjiInfoList
import com.github.reline.jisho.persistence.bridgetables.KanjiPriorityList
import com.github.reline.jisho.persistence.entities.Kanji

data class KanjiWithMemberIds(
        @Embedded
        val kanji: Kanji
) {
    @Relation(
            parentColumn = "id",
            entityColumn = "kanji_id",
            entity = KanjiInfoList::class,
            projection = ["kanji_info_id"]
    )
    lateinit var infoIds: List<String>

    @Relation(
            parentColumn = "id",
            entityColumn = "kanji_id",
            entity = KanjiPriorityList::class,
            projection = ["kanji_priority_id"]
    )
    lateinit var priorityIds: List<String>
}