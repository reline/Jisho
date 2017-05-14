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

import io.github.reline.jishodb.dictmodels.TestData.KANJI_PRIORITY
import io.github.reline.jishodb.dictmodels.TestData.provide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(JUnitPlatform::class)
object KanjiPriorityTest: Spek({
    describe("a kanji priority element") {
        val kanjiPriority = provide(KANJI_PRIORITY, KanjiPriority::class.java)

        it("should have a priority value") {
            assertEquals("ichi1", kanjiPriority.value)
        }

        it("should be common") {
            assertTrue(kanjiPriority.isCommon())
        }
    }
})