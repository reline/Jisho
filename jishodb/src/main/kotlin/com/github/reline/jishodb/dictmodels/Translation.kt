package com.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Xml
import java.lang.StringBuilder

@Xml(name = "trans")
class Translation {
    var nameTypes: MutableList<NameType>? = null
    var crossReferences: MutableList<XReference>? = null
    var translationDetails: MutableList<TranslationDetail>? = null
}
