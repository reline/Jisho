package com.github.reline.jishodb.dictmodels.kanji

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

/**
 * The single header element will contain identification information
 * about the version of the file
 */
@Xml
open class Header {

    /**
     * This field denotes the version of kanjidic2 structure, as more
     * than one version may exist.
     */
    @PropertyElement(name = "file_version")
    var fileVersion: Int = 0

    /**
     * The version of the file, in the format YYYY-NN, where NN will be
     * a number starting with 01 for the first version released in a
     * calendar year, then increasing for each version in that year.
     * todo: use a date with an adapter
     */
    @PropertyElement(name = "database_version")
    lateinit var databaseVersion: String

    /**
     * The date the file was created in international format (YYYY-MM-DD).
     */
    @PropertyElement(name = "date_of_creation")
    lateinit var createDate: String

}