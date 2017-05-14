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

import io.github.reline.jishodb.dictmodels.TestData.SENSE
import io.github.reline.jishodb.dictmodels.TestData.provide
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(JUnitPlatform::class)
object DictSenseTest: Spek({
    describe("a dictionary sense element") {
        val dictSense = provide(SENSE, DictSense::class.java)

        it("should have the defined glosses") {
            assertEquals("hello", dictSense.glosses?.get(0)?.value)
            assertEquals("good day (daytime greeting)", dictSense.glosses?.get(1)?.value)
        }

        it("should have a coded part of speech") {
            assertEquals("&int;", dictSense.partsOfSpeech?.get(0)?.value)
        }

        it("should have a miscellaneous element") {
            assertEquals("&uk;", dictSense.miscellaneous?.get(0)?.value)
        }

        it("should have an information element") {
            assertEquals("こんちは is col.", dictSense.information?.get(0)?.value)
        }

        val sense = dictSense.getSense()

        it("should map glosses to definitions") {
            assertEquals("hello", sense.getDefinitions()[0])
            assertEquals("good day (daytime greeting)", sense.getDefinitions()[1])
        }

        it("should map miscellaneous elements to tags") {
            assertEquals("Usually written using kana alone", sense.getTags()[0])
        }

        it("should map information") {
            assertEquals("こんちは is col.", sense.getInfo()[0])
        }
    }
})