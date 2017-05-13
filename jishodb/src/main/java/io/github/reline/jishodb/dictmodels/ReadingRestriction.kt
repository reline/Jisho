package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "re_restr")
open class ReadingRestriction {

    @TextContent
    lateinit var kanji: String
}