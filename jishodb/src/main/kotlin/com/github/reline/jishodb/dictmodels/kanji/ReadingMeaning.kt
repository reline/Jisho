package com.github.reline.jishodb.dictmodels.kanji

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * The readings for the kanji in several languages, and the meanings, also
in several languages. The readings and meanings are grouped to enable
the handling of the situation where the meaning is differentiated by
reading. [T1]
 */
@Xml
open class ReadingMeaning {
    @Element
    var groups: MutableList<Group>? = null

    @Xml(name = "rmgroup")
    open class Group {
        @Element
        var readings: MutableList<Reading>? = null

        /**
         * The reading element contains the reading or pronunciation
        of the kanji.
         */
        @Xml(name = "reading")
        open class Reading {
            @TextContent
            lateinit var value: String

            /**
             * The r_type attribute defines the type of reading in the reading
            element. The current values are:
            pinyin - the modern PinYin romanization of the Chinese reading
            of the kanji. The tones are represented by a concluding
            digit. [Y]
            korean_r - the romanized form of the Korean reading(s) of the
            kanji.  The readings are in the (Republic of Korea) Ministry
            of Education style of romanization. [W]
            korean_h - the Korean reading(s) of the kanji in hangul.
            vietnam - the Vietnamese readings supplied by Minh Chau Pham.
            ja_on - the "on" Japanese reading of the kanji, in katakana.
            Another attribute r_status, if present, will indicate with
            a value of "jy" whether the reading is approved for a
            "Jouyou kanji".
            A further attribute on_type, if present,  will indicate with
            a value of kan, go, tou or kan'you the type of on-reading.
            ja_kun - the "kun" Japanese reading of the kanji, usually in
            hiragana.
            Where relevant the okurigana is also included separated by a
            ".". Readings associated with prefixes and suffixes are
            marked with a "-". A second attribute r_status, if present,
            will indicate with a value of "jy" whether the reading is
            approved for a "Jouyou kanji".
             */
            @Attribute(name = "r_type")
            lateinit var type: String

            /**
             * See under ja_on above.
             */
            @Attribute(name = "on_type")
            var on: String? = null

            /**
             * See under ja_on and ja_kun above.
             */
            @Attribute(name = "r_status")
            var status: String? = null
        }

        @Element
        var meanings: MutableList<Meaning>? = null

        /**
         * The meaning associated with the kanji.
         */
        @Xml(name = "meaning")
        open class Meaning {
            @TextContent
            lateinit var value: String

            /**
             * The m_lang attribute defines the target language of the meaning. It
            will be coded using the two-letter language code from the ISO 639-1
            standard. When absent, the value "en" (i.e. English) is implied. [{}]
             */
            @Attribute(name = "m_lang")
            var language: String? = null
        }

    }

    @Element
    var nanori: MutableList<Nanori>? = null

    /**
     * Japanese readings that are now only associated with names.
     */
    @Xml(name = "nanori")
    open class Nanori { @TextContent lateinit var value: String }

}