package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "ke_inf")
open class KanjiInfo {

    @TextContent
    lateinit var value: String
}