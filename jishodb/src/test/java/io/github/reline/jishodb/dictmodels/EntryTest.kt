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

import io.github.reline.jishodb.dictmodels.TestData.ENTRY
import io.github.reline.jishodb.dictmodels.TestData.KANJI_ENTRY
import io.github.reline.jishodb.dictmodels.TestData.provide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(JUnitPlatform::class)
object EntryTest: Spek({
    describe("an entry with a kanji element") {
        val entry = provide(KANJI_ENTRY, Entry::class.java)

        it("should return the id that was given in the xml") {
            assertEquals(1289400, entry.id)
        }

        it("should have one kanji element") {
            assertEquals(1, entry.kanji?.size)
        }

        it("should have two reading elements") {
            assertEquals(2, entry.readings.size)
        }

        it("should have one sense element") {
            assertEquals(1, entry.senses.size)
        }

        it("should be a common word") {
            assertTrue(entry.isCommon())
        }

        it("should return the equivalent japanese objects") {
            val japanese = entry.getJapanese()
            assertEquals(2, japanese.size, "the expected and actual number of japanese objects was incorrect")

            assertEquals(japanese[0].word, "今日は", "the japanese word was not taken from the kanji element")
            assertEquals(japanese[0].reading, "こんにちは", "the japanese reading was not taken from the reading element")

            assertEquals(japanese[1].word, "今日は", "the japanese word was not taken from the kanji element")
            assertEquals(japanese[1].reading, "こんちは", "the japanese reading was not taken from the reading element")
        }
    }

    describe("an entry without a kanji element") {
        val entry = provide(ENTRY, Entry::class.java)

        it("should return the id that was given in the xml") {
            assertEquals(1000360, entry.id)
        }

        it("should have no kanji elements") {
            assertNull(entry.kanji)
        }

        it("should have one reading element") {
            assertEquals(1, entry.readings.size)
        }

        it("should have two sense elements") {
            assertEquals(2, entry.senses.size)
        }

        it("should be a common word") {
            assertTrue(entry.isCommon())
        }

        it("should return the equivalent japanese objects") {
            val japanese = entry.getJapanese()
            assertEquals(1, japanese.size, "the expected and actual number of japanese objects was incorrect")

            assertEquals(japanese[0].reading, "あっさり", "the japanese word was not taken from the reading element")
            assertNull(japanese[0].word, "there is no kanji element, so this field should be empty")
        }
    }
})