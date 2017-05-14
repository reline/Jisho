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

import io.github.reline.jishodb.dictmodels.TestData.KANJI_ELEMENT
import io.github.reline.jishodb.dictmodels.TestData.UNCOMMON_KANJI_ELEMENT
import io.github.reline.jishodb.dictmodels.TestData.provide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(JUnitPlatform::class)
object KanjiTest: Spek({
    describe("a common kanji element") {
        val kanjiElement = provide(KANJI_ELEMENT, Kanji::class.java)

        it("should contain a japanese word") {
            assertEquals("比律賓", kanjiElement.value)
        }

        it("should be a common word") {
            assertTrue(kanjiElement.isCommon())
        }

        it("should have one information element") {
            assertEquals(1, kanjiElement.information?.size)
        }

        it("should have one priority element") {
            assertEquals(1, kanjiElement.priorities?.size)
        }
    }

    describe("an uncommon kanji element") {
        val kanjiElement = provide(UNCOMMON_KANJI_ELEMENT, Kanji::class.java)

        it("should not be a common word") {
            assertFalse(kanjiElement.isCommon())
        }

        it("should not have any priority elements") {
            assertNull(kanjiElement.priorities)
        }
    }
})