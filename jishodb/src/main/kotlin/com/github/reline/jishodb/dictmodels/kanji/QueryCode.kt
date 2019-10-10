package com.github.reline.jishodb.dictmodels.kanji

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

/**
 * These codes contain information relating to the glyph, and can be used
for finding a required kanji. The type of code is defined by the
qc_type attribute.
 */
@Xml
open class QueryCode {
    @Element
    lateinit var codes: Code

    /**
     * The q_code contains the actual query-code value, according to the
    qc_type attribute.
     */
    @Xml(name = "q_code")
    open class Code {

        /**
         * The qc_type attribute defines the type of query code. The current values
        are:
        skip -  Halpern's SKIP (System  of  Kanji  Indexing  by  Patterns)
        code. The  format is n-nn-nn.  See the KANJIDIC  documentation
        for  a description of the code and restrictions on  the
        commercial  use  of this data. [P]  There are also
        a number of misclassification codes, indicated by the
        "skip_misclass" attribute.
        sh_desc - the descriptor codes for The Kanji Dictionary (Tuttle
        1996) by Spahn and Hadamitzky. They are in the form nxnn.n,
        e.g.  3k11.2, where the  kanji has 3 strokes in the
        identifying radical, it is radical "k" in the SH
        classification system, there are 11 other strokes, and it is
        the 2nd kanji in the 3k11 sequence. (I am very grateful to
        Mark Spahn for providing the list of these descriptor codes
        for the kanji in this file.) [I]
        four_corner - the "Four Corner" code for the kanji. This is a code
        invented by Wang Chen in 1928. See the KANJIDIC documentation
        for  an overview of  the Four Corner System. [Q]

        deroo - the codes developed by the late Father Joseph De Roo, and
        published in  his book "2001 Kanji" (Bonjinsha). Fr De Roo
        gave his permission for these codes to be included. [DR]
        misclass - a possible misclassification of the kanji according
        to one of the code types. (See the "Z" codes in the KANJIDIC
        documentation for more details.)
         */
        @Attribute(name = "qc_type")
        lateinit var type: String

        /**
         * The values of this attribute indicate the type if
        misclassification:
        - posn - a mistake in the division of the kanji
        - stroke_count - a mistake in the number of strokes
        - stroke_and_posn - mistakes in both division and strokes
        - stroke_diff - ambiguous stroke counts depending on glyph
         */
        @Attribute(name = "skip_misclass")
        var skipMisclass: String? = null

        @TextContent
        lateinit var value: String
    }
}