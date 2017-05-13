package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "k_ele")
open class Kanji {

    @PropertyElement(name = "keb")
    lateinit var value: String

    @Element
    lateinit var information: MutableList<KanjiInfo>

    @Element
    lateinit var priorities: MutableList<KanjiPriority>
}