/*
 * Copyright 2017 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.reline.jishodb.dictmodels

import com.tickaroo.tikxml.TikXml
import okio.Buffer
import java.nio.charset.StandardCharsets

object TestData {

    fun <T> provide(data: String, clazz: Class<T>): T {
        val source = Buffer().readFrom(data.byteInputStream(StandardCharsets.UTF_8))
        return TikXml.Builder().build().read(source, clazz)
    }

    val KANJI_ENTRY = """
    <entry>
        <ent_seq>1289400</ent_seq>
        <k_ele>
            <keb>今日は</keb>
            <ke_pri>ichi1</ke_pri>
        </k_ele>
        <r_ele>
            <reb>こんにちは</reb>
            <re_pri>ichi1</re_pri>
        </r_ele>
        <r_ele>
            <reb>こんちは</reb>
        </r_ele>
        <sense>
            <pos>&int;</pos>
            <misc>&uk;</misc>
            <s_inf>こんちは is col.</s_inf>
            <gloss>hello</gloss>
            <gloss>good day (daytime greeting)</gloss>
        </sense>
    </entry>
"""

    val ENTRY = """
    <entry>
        <ent_seq>1000360</ent_seq>
        <r_ele>
            <reb>あっさり</reb>
            <re_pri>ichi1</re_pri>
        </r_ele>
        <sense>
            <pos>&adv-to;</pos>
            <pos>&vs;</pos>
            <misc>&on-mim;</misc>
            <gloss>easily</gloss>
            <gloss>readily</gloss>
            <gloss>quickly</gloss>
        </sense>
        <sense>
            <misc>&on-mim;</misc>
            <gloss>lightly (flavored food, applied makeup)</gloss>
        </sense>
    </entry>
"""

    val KANJI_ELEMENT = """
    <k_ele>
        <keb>比律賓</keb>
        <ke_inf>&ateji;</ke_inf>
        <ke_pri>spec1</ke_pri>
    </k_ele>
"""

    val UNCOMMON_KANJI_ELEMENT = """
    <k_ele>
        <keb>明白</keb>
        <ke_inf>&ateji;</ke_inf>
    </k_ele>
"""

    val KANJI_INFO = """<ke_inf>&ateji;</ke_inf>"""

    val KANJI_PRIORITY = """<ke_pri>ichi1</ke_pri>"""

    val SENSE = """
    <sense>
        <pos>&int;</pos>
        <misc>&uk;</misc>
        <s_inf>こんちは is col.</s_inf>
        <gloss>hello</gloss>
        <gloss>good day (daytime greeting)</gloss>
    </sense>
"""
}