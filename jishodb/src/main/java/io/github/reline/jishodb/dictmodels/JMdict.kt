package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "JMdict")
open class JMdict {

    @Element
    lateinit var entries: MutableList<Entry>
}