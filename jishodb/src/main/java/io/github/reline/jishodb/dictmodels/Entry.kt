package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "entry")
open class Entry {

    @PropertyElement(name = "ent_seq")
    var id: Int = 0

    @Element
    lateinit var kanji: MutableList<Kanji>

    @Element
    lateinit var readings: MutableList<Reading>

//    @Element
//    lateinit var senses: MutableList<DictSense>
}