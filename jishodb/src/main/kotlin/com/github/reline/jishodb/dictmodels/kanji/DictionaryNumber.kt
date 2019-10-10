package com.github.reline.jishodb.dictmodels.kanji

import com.tickaroo.tikxml.annotation.*

/**
 * This element contains the index numbers and similar unstructured
information such as page numbers in a number of published dictionaries,
and instructional books on kanji.
 */
@Xml
open class DictionaryNumber {

    @Element
    lateinit var dictionaryReferences: MutableList<DictionaryReference>

    /**
     * Each dic_ref contains an index number. The particular dictionary,
    etc. is defined by the dr_type attribute.
     */
    @Xml(name = "dic_ref")
    open class DictionaryReference {

        /**
         * The dr_type defines the dictionary or reference book, etc. to which
        dic_ref element applies. The initial allocation is:
        nelson_c - "Modern Reader's Japanese-English Character Dictionary",
        edited by Andrew Nelson (now published as the "Classic"
        Nelson).
        nelson_n - "The New Nelson Japanese-English Character Dictionary",
        edited by John Haig.
        halpern_njecd - "New Japanese-English Character Dictionary",
        edited by Jack Halpern.
        halpern_kkd - "Kodansha Kanji Dictionary", (2nd Ed. of the NJECD)
        edited by Jack Halpern.
        halpern_kkld - "Kanji Learners Dictionary" (Kodansha) edited by
        Jack Halpern.
        halpern_kkld_2ed - "Kanji Learners Dictionary" (Kodansha), 2nd edition
        (2013) edited by Jack Halpern.
        heisig - "Remembering The  Kanji"  by  James Heisig.
        heisig6 - "Remembering The  Kanji, Sixth Ed."  by  James Heisig.
        gakken - "A  New Dictionary of Kanji Usage" (Gakken)
        oneill_names - "Japanese Names", by P.G. O'Neill.
        oneill_kk - "Essential Kanji" by P.G. O'Neill.
        moro - "Daikanwajiten" compiled by Morohashi. For some kanji two
        additional attributes are used: m_vol:  the volume of the
        dictionary in which the kanji is found, and m_page: the page
        number in the volume.
        henshall - "A Guide To Remembering Japanese Characters" by
        Kenneth G.  Henshall.
        sh_kk - "Kanji and Kana" by Spahn and Hadamitzky.
        sh_kk2 - "Kanji and Kana" by Spahn and Hadamitzky (2011 edition).
        sakade - "A Guide To Reading and Writing Japanese" edited by
        Florence Sakade.
        jf_cards - Japanese Kanji Flashcards, by Max Hodges and
        Tomoko Okazaki. (Series 1)
        henshall3 - "A Guide To Reading and Writing Japanese" 3rd
        edition, edited by Henshall, Seeley and De Groot.
        tutt_cards - Tuttle Kanji Cards, compiled by Alexander Kask.
        crowley - "The Kanji Way to Japanese Language Power" by
        Dale Crowley.
        kanji_in_context - "Kanji in Context" by Nishiguchi and Kono.
        busy_people - "Japanese For Busy People" vols I-III, published
        by the AJLT. The codes are the volume.chapter.
        kodansha_compact - the "Kodansha Compact Kanji Guide".
        maniette - codes from Yves Maniette's "Les Kanjis dans la tete" French adaptation of Heisig.
         */
        @Attribute(name = "dr_type")
        lateinit var type: String

        /**
         * See [type] under "moro".
         */
        @Attribute(name = "m_vol")
        lateinit var vol: String

        /**
         * See [type] under "moro".
         */
        @Attribute(name = "m_page")
        lateinit var page: String

        @TextContent
        lateinit var value: String
    }
}