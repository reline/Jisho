package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "re_inf")
open class ReadingInfo {

    @TextContent
    lateinit var value: String
}