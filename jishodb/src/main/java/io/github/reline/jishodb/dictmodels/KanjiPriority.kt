package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "ke_pri")
open class KanjiPriority {

    @TextContent
    lateinit var value: String
}