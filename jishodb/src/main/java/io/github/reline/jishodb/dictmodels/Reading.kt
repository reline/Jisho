package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "r_ele")
open class Reading {

    @PropertyElement(name = "reb")
    var value: String? = null

    @PropertyElement(name = "re_nokanji")
    var isNotTrueReading: Boolean? = null

    @Element
    lateinit var restrictions: MutableList<ReadingRestriction>

    @Element
    lateinit var information: MutableList<ReadingInfo>

    @Element
    lateinit var priorities: MutableList<ReadingPriority>
}