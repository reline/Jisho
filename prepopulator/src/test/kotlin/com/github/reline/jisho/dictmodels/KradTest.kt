package com.github.reline.jisho.dictmodels

import okio.buffer
import okio.source
import kotlin.test.Test
import kotlin.test.assertEquals

class KradTest {
    @Test
    fun test() {
        val input = """
            # Documentation
            今 : 个 一
            旭 : 日 九
        """.trimIndent()
        val expected = listOf(
            Krad(
                '今',
                listOf('个', '一'),
            ),
            Krad(
                '旭',
                listOf('日', '九'),
            ),
        )
        input.byteInputStream().source().buffer().use {
            val actual = decodeKrad(it)
            assertEquals(expected, actual)
        }

    }
}