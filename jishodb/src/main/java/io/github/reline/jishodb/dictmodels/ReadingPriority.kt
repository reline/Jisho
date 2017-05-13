package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "re_pri")
open class ReadingPriority {

    @TextContent
    lateinit var value: String
}