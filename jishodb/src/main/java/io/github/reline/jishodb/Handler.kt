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

package io.github.reline.jishodb

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

class Handler : DefaultHandler() {

    companion object {
        val TAG = "Handler"
        val ENTRY = "entry"
        val ENTRY_SEQUENCE = "ent_seq"
        val KANJI_ELEMENT = "k_ele"
        val KEB = "keb"
        val KE_INF = "ke_inf"
        val KE_PRI = "ke_pri"
        val R_ELE = "r_ele"
        val REB = "reb"
        val RE_NOKANJI = "re_nokanji"
        val RE_RESTR = "re_restr"
        val RE_INF = "re_inf"
        val RE_PRI = "re_pri"
        val SENSE = "sense" // (stagk*, stagr*, pos*, xref*, ant*, field*, misc*, s_inf*, lsource*, dial*, gloss*)
        val STAGK = "stagk"
        val STAGR = "stagr"
        val CROSS_REFERENCE = "xref"
        val ANTONYM = "ant"
        val PART_OF_SPEECH = "pos"
        val FIELD = "field"
        val MISC = "misc"
        val LANGUAGE_SOURCE = "lsource"
        //lsource xml:lang CDATA "eng" - attribute list
        //lsource ls_type CDATA #IMPLIED - attribute list
        //lsource ls_wasei CDATA #IMPLIED - attribute list
        val DIAL = "dial"
        val GLOSS = "gloss"
        //gloss xml:lang CDATA "eng" - attribute list
        //gloss g_gend CDATA #IMPLIED - attribute list
        val PRI = "pri"
        val SENSE_INFORMATION = "s_inf"

        // the following entity codes are used for common elements
        // within the various information fields
        /*
        MA
        X
        abbr
        adj-i
        adj-ix
        adj-na
        adj-no
        adj-pn
        adj-t
        adj-f
        adv
        adv-to
        arch
        ateji
        aux
        aux-v
        aux-adj
        Buddh
        chem
        chn
        col
        comp
        conj
        cop-da
        ctr
        derog
        eK
        ek
        exp
        fam
        fem
        food
        geom
        gikun
        hon
        hum
        iK
        id
        ik
        int
        io
        iv
        ling
        m-sl
        male
        male-sl
        math
        mil
        n
        n-adv
        n-suf
        n-pref
        n-t
        num
        oK
        obs
        obsc
        ok
        oik
        on-mim
        pn
        poet
        pol
        pref
        proverb
        prt
        physics
        rare
        sense
        sl
        suf
        uK
        uk
        unc
        yoji
        v1
        v1-s
        v2a-s
        v4h
        v4r
        v5aru
        v5b
        v5g
        v5k
        v5k-s
        v5m
        v5n
        v5r
        v5r-i
        v5s
        v5t
        v5u
        v5u-s
        v5uru
        vz
        vi
        vk
        vn
        vr
        vs
        vs-c
        vs-s
        vs-i
        kyb
        osb
        ksb
        ktb
        tsb
        thb
        tsug
        kyu
        rkb
        nab
        hob
        vt
        vulg
        adj-kari
        adj-ku
        adj-shiku
        adj-nari
        n-pr
        v-unspec
        v4k
        v4g
        v4s
        v4t
        v4n
        v4b
        v4m
        v2k-k
        v2g-k
        v2t-k
        v2d-k
        v2h-k
        v2b-k
        v2m-k
        v2y-k
        v2r-k
        v2k-s
        v2g-s
        v2s-s
        v2z-s
        v2t-s
        v2d-s
        v2n-s
        v2h-s
        v2b-s
        v2m-s
        v2y-s
        v2r-s
        v2w-s
        archit
        astron
        baseb
        biol
        bot
        bus
        econ
        engr
        finc
        geol
        law
        mahj
        med
        music
        Shinto
        shogi
        sports
        usmo
        zool
        joc
        anat
         */
    }

    private var entry = false
    private var keb = false
    private var reb = false
    private var gloss = false

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
//        Log.d(TAG, qName)
        when (qName) {
            ENTRY -> {
                entry = true
                Log.d(TAG, "***************Entry****************")
            }
            GLOSS -> {
                gloss = true
            }
            KEB -> {
                keb = true
            }
            REB -> {
                reb = true
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
//        Log.d(TAG, qName)
        when (qName) {
            ENTRY -> {
                entry = false
            }
            GLOSS -> {
                gloss = false
            }
            KEB -> {
                keb = false
            }
            REB -> {
                reb = false
            }
        }
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        val s = java.lang.String(ch, start, length) as String
//        Log.d(TAG, s)
        if (gloss) {
            Log.d(TAG, "gloss: $s")
        }
        if (keb) {
            Log.d(TAG, "kanji: $s")
        }
        if (reb) {
            Log.d(TAG, "reading: $s")
        }
    }
}