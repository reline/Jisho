package com.github.reline.jishodb.dictmodels.kanji

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * This is the DTD of the XML-format kanji file combining information from
 * the KANJIDIC and KANJD212 files. It is intended to be largely self-
 * documenting, with each field being accompanied by an explanatory
 * comment.
 *
 * The file covers the following kanji:
 * (a) the 6,355 kanji from JIS X 0208;
 * (b) the 5,801 kanji from JIS X 0212;
 * (c) the 3,693 kanji from JIS X 0213 as follows:
 * (i) the 2,741 kanji which are also in JIS X 0212 have
 * JIS X 0213 code-points (kuten) added to the existing entry;
 * (ii) the 952 "new" kanji have new entries.
 *
 * At the end of the explanation for a number of fields there is a tag
 * with the format [N]. This indicates the leading letter(s) of the
 * equivalent field in the KANJIDIC and KANJD212 files.
 *
 * The KANJIDIC documentation should also be read for additional
 * information about the information in the file.
 */
@Xml(name = "kanjidic2")
open class KanjiDictionary {

    @Element(name = "header")
    lateinit var header: Header

    @Element
    var characters: MutableList<Character>? = null
}