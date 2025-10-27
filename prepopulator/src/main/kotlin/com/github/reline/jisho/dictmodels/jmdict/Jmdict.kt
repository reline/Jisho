package com.github.reline.jisho.dictmodels.jmdict

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.newReader
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.xmlStreaming
import okio.BufferedSource
import okio.IOException

@Serializable
class JMdict private constructor(
    val entries: List<Entry>,
) {
    companion object {
        @Throws(IOException::class)
        fun decodeFrom(source: BufferedSource): JMdict {
            val reader = xmlStreaming.newReader(source.inputStream())
            val xml = XML {
                defaultPolicy {
                    ignoreUnknownChildren()
                }
            }
            return xml.decodeFromReader(serializer(), reader)
        }
    }
}

@Serializable
@SerialName("entry")
class Entry private constructor(
    @SerialName("ent_seq")
    @XmlElement
    val id: Long,
    val kanji: List<Kanji>? = null,
    val readings: List<Reading>,
    val senses: List<DictSense>,
) {
    fun isCommon(): Boolean =
        readings.any { it.isCommon() } || kanji?.any { it.isCommon() } == true
}

@Serializable
@SerialName("k_ele")
class Kanji private constructor(
    @SerialName("keb")
    @XmlElement
    val value: String,
//    val information: List<KanjiInfo>? = null,
    @SerialName("ke_pri")
    val priorities: List<String>? = null,
) {
    fun isCommon() = priorities?.any { Priority.isCommon(it) } == true
}

@Serializable
@SerialName("r_ele")
class Reading private constructor(
    @SerialName("reb")
    @XmlElement
    val value: String,
    @SerialName("re_pri")
    val priorities: List<String>? = null,
    @SerialName("re_nokanji")
    @XmlElement
    val isNotTrueReading: Boolean? = null,
) {
    fun isCommon() = priorities?.any { Priority.isCommon(it) } == true
}

@Serializable
@SerialName("sense")
class DictSense private constructor(
    @SerialName("pos")
    val partsOfSpeech: List<String>? = null,
    @SerialName("misc")
    val miscellaneous: List<String>? = null,
    @SerialName("gloss")
    val glosses: List<String>? = null,
)
