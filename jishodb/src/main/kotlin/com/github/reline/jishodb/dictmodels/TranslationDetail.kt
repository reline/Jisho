package com.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "trans_det")
class TranslationDetail {
    @TextContent
    lateinit var value: String

    val statement: String
        get() {
            // TODO
            return ""
        }
}
