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
import io.github.reline.jishodb.dictmodels.TestData.ENTRY
import okio.Buffer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals

@RunWith(JUnitPlatform::class)
object EntryTest: Spek({
    describe("an entry") {
        val source = Buffer().readFrom(ENTRY.byteInputStream(StandardCharsets.UTF_8))
        val entry = TikXml.Builder().build().read(source, Entry::class.java)

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
    }
})