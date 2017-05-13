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

object TestData {
    val ENTRY = """
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
}