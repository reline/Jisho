package com.github.reline.jisho.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "trans_det")
class TranslationDetail {
    @TextContent
    lateinit var value: String
}
