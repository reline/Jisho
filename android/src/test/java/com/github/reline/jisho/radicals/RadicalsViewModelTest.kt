package com.github.reline.jisho.radicals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.reline.jisho.models.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.*
import org.junit.Rule
import org.mockito.kotlin.*
import timber.log.Timber
import kotlin.test.*

private val kanji = hashMapOf(
    Pair("籤", listOf(
        Radical(1L, "人", 2),
        Radical(2L, "戈", 4),
        Radical(3L, "竹", 6),
        Radical(4L, "韭", 9),
    )),
    Pair("讖", listOf(
        Radical(1L, "人", 2),
        Radical(2L, "戈", 4),
        Radical(4L, "韭", 9),
        Radical(5L, "言", 7),
    ))
)

private val radicals = kanji.values.flatten().distinct()

class RadicalsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var repository: Repository
    lateinit var viewModel: RadicalsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        Timber.plant(object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                println(message)
            }
        })
        repository = mock {
            onBlocking { getRadicals() } doReturn radicals
            onBlocking { getRelatedRadicals(any()) } doAnswer { inv ->
                val radicals = inv.getArgument<List<String>>(0)
                kanji.values.filter {
                    it.any { radical ->
                        radicals.contains(radical.value)
                    }
                }.flatten()
            }
        }
        viewModel = RadicalsViewModel(repository)
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @Test
    fun testDefaultNoneSelected() = runTest {
        val entries = viewModel.radicals.value.entries.map { it.value }
        entries.none { it.isSelected }
    }

    @Test
    fun selectedRadicalTest() = runTest {
        val expected = Radical(1L, "人", 2)
        viewModel.onRadicalToggled(expected)
        val entries = viewModel.radicals.value.entries.map { it.value }
        val actual = entries.single { it.isSelected }
        assertEquals(expected.value, actual.value)
    }

    @Test
    fun unselectRadicalTest() = runTest {
        val rad = Radical(1L, "人", 2)
        viewModel.onRadicalToggled(rad)
        viewModel.onRadicalToggled(Radical(5L, "言", 7))
        viewModel.onRadicalToggled(Radical(5L, "言", 7))
        val selected = viewModel.radicals.value.entries.map { it.value }.single { it.isSelected }
        assertEquals(rad.value, selected.value)
        assertTrue(selected.isEnabled)
    }

    @Test
    fun unselectLastRadicalTest() = runTest {
        val rad = Radical(1L, "人", 2)
        viewModel.onRadicalToggled(rad)
        viewModel.onRadicalToggled(rad)
        val disabled = viewModel.radicals.value.entries.map { it.value }.filter { !it.isEnabled }
        assertTrue(disabled.isEmpty())
    }

    @Test
    fun disabledRadicalTest() = runTest {
        viewModel.onRadicalToggled(Radical(5L, "言", 7))
        val results = viewModel.radicals.value.entries.map { it.value }
        val enabled = results.filter { it.isEnabled }
        assertEquals(4, enabled.size)
        val disabled = results.single { !it.isEnabled }
        assertEquals("竹", disabled.value)
    }
}