package com.github.reline.jisho.dictmodels.jmdict

import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "trans")
class Translation {
    var nameTypes: MutableList<NameType>? = null
    var crossReferences: MutableList<XReference>? = null
    var translationDetails: MutableList<TranslationDetail>? = null
}
