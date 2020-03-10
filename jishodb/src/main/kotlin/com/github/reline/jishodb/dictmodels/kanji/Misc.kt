package com.github.reline.jishodb.dictmodels.kanji

import com.tickaroo.tikxml.annotation.*

@Xml
open class Misc {
    /**
     * The kanji grade level. 1 through 6 indicates a Kyouiku kanji
    and the grade in which the kanji is taught in Japanese schools.
    8 indicates it is one of the remaining Jouyou Kanji to be learned
    in junior high school. 9 indicates it is a Jinmeiyou (for use
    in names) kanji which in addition  to the Jouyou kanji are approved
    for use in family name registers and other official documents. 10
    also indicates a Jinmeiyou kanji which is a variant of a
    Jouyou kanji. [G]
     */
    @PropertyElement(name = "grade")
    var grade: Int? = null

    /**
     * The stroke count of the kanji, including the radical. If more than
    one, the first is considered the accepted count, while subsequent ones
    are common miscounts. (See Appendix E. of the KANJIDIC documentation
    for some of the rules applied when counting strokes in some of the
    radicals.) [S]
     */
    @Element
    lateinit var strokeCounts: MutableList<StrokeCount>
    @Xml(name = "stroke_count") open class StrokeCount { @TextContent lateinit var strokeCount: String }

    @Element
    var variants: MutableList<Variant>? = null

    /**
     * Either a cross-reference code to another kanji, usually regarded as a
    variant, or an alternative indexing code for the current kanji.
    The type of variant is given in the var_type attribute.
     */
    @Xml(name = "variant")
    open class Variant {
        @TextContent
        lateinit var value: String

        /**
         * The var_type attribute indicates the type of variant code. The current
        values are:
        jis208 - in JIS X 0208 - kuten coding
        jis212 - in JIS X 0212 - kuten coding
        jis213 - in JIS X 0213 - kuten coding
        (most of the above relate to "shinjitai/kyuujitai"
        alternative character glyphs)
        deroo - De Roo number - numeric
        njecd - Halpern NJECD index number - numeric
        s_h - The Kanji Dictionary (Spahn & Hadamitzky) - descriptor
        nelson_c - "Classic" Nelson - numeric
        oneill - Japanese Names (O'Neill) - numeric
        ucs - Unicode codepoint- hex
         */
        @Attribute(name = "var_type")
        lateinit var type: String
    }

    /**
     * A frequency-of-use ranking. The 2,500 most-used characters have a
    ranking; those characters that lack this field are not ranked. The
    frequency is a number from 1 to 2,500 that expresses the relative
    frequency of occurrence of a character in modern Japanese. This is
    based on a survey in newspapers, so it is biassed towards kanji
    used in newspaper articles. The discrimination between the less
    frequently used kanji is not strong. (Actually there are 2,501
    kanji ranked as there was a tie.)
     */
    @PropertyElement(name = "freq")
    var frequency: Int? = null

    @Element
    var radicalNames: MutableList<RadicalName>? = null
    /**
     * When the kanji is itself a radical and has a name, this element
    contains the name (in hiragana.) [T2]
     */
    @Xml(name = "rad_name") open class RadicalName { @TextContent lateinit var value: String }

    /**
     * The (former) Japanese Language Proficiency test level for this kanji.
    Values range from 1 (most advanced) to 4 (most elementary). This field
    does not appear for kanji that were not required for any JLPT level.
    Note that the JLPT test levels changed in 2010, with a new 5-level
    system (N1 to N5) being introduced. No official kanji lists are
    available for the new levels. The new levels are regarded as
    being similar to the old levels except that the old level 2 is
    now divided between N2 and N3.
     */
    @PropertyElement(name = "jlpt")
    var jlpt: String? = null
}