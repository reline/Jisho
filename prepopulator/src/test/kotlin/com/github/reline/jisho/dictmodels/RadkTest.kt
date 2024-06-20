package com.github.reline.jisho.dictmodels

import okio.buffer
import okio.source
import kotlin.test.Test
import kotlin.test.assertEquals

class RadkTest {

    @Test
    fun test() {
        val input = """
            # Documentation
            $ 日 4
            鬐鬙鬠鬺鮊鮋鯁鯟鯧鯷鯹鯺鯽鰋鰑鰚鰣鰨鰽鱄鱏鱓鱛鱣鱪鱜鱨鱰鴲鵪鵫鵾鶍鶕鶗鶡
            鶬鷃鷾鸇麘麞麯黤鼂鼙鼴鼹齃齄齰齵
            $ 入 2
            久込入兩兪叺圦懣杁柩滿疚瞞窩糴裲蹣輛陝魎鳰
        """.trimIndent()
        val expected = listOf(
            Radk(
                '日',
                4,
                "鬐鬙鬠鬺鮊鮋鯁鯟鯧鯷鯹鯺鯽鰋鰑鰚鰣鰨鰽鱄鱏鱓鱛鱣鱪鱜鱨鱰鴲鵪鵫鵾鶍鶕鶗鶡鶬鷃鷾鸇麘麞麯黤鼂鼙鼴鼹齃齄齰齵".toList(),
            ),
            Radk(
                '入',
                2,
                "久込入兩兪叺圦懣杁柩滿疚瞞窩糴裲蹣輛陝魎鳰".toList(),
            ),
        )
        input.byteInputStream().source().buffer().use {
            val actual = decodeRadicals(it)
            assertEquals(expected, actual)
        }
    }
}
