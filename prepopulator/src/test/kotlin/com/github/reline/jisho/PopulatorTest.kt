package com.github.reline.jisho

import com.github.reline.jisho.populators.JishoInput
import com.github.reline.jisho.populators.JishoPopulator
import com.github.reline.jisho.tasks.Dictionary
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

class PopulatorTest {
    private val fileSystem = FileSystem.SYSTEM
    private val tmpDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    private val resources = FileSystem.RESOURCES

    private lateinit var databaseDir: Path
    private lateinit var databasePath: Path

    private val jmdictEntries = "JMdict_e.xml".toPath()
    private val jmdictFurigana = "JmdictFurigana.json".toPath()
    private val kanjidic = "kanjidic2.xml".toPath()

    private val definitions get() = databaseDir/jmdictEntries
    private val okurigana get() = databaseDir/jmdictFurigana
    private val kanji get() = databaseDir/kanjidic

    private lateinit var testScope: TestScope
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var jishoPopulator: JishoPopulator

    @BeforeTest
    fun setup() {
        databaseDir = tmpDir/"populatorTest_${UUID.randomUUID()}"
        fileSystem.createDirectories(databaseDir, mustCreate = true)
        databasePath = databaseDir/"jisho.sqlite"

        resources.read(jmdictEntries) {
            fileSystem.write(definitions, mustCreate = true) {
                writeAll(this@read)
            }
        }

        resources.read(jmdictFurigana) {
            fileSystem.write(okurigana, mustCreate = true) {
                writeAll(this@read)
            }
        }

        resources.read(kanjidic) {
            fileSystem.write(kanji, mustCreate = true) {
                writeAll(this@read)
            }
        }

        testScope = TestScope()
        testDispatcher = StandardTestDispatcher(testScope.testScheduler)
        jishoPopulator = JishoPopulator(testDispatcher)
    }

    @Test
    fun testDatabaseFileIsClosed() = testScope.runTest {
        jishoPopulator.populate(
            databasePath.toFile(),
            JishoInput(listOf(
                Dictionary(definitions.toFile(), okurigana = okurigana.toFile())),
                listOf(kanji.toFile()),
                emptyList(),
            ),
        )
        // if the db file can be deleted, it is closed
        fileSystem.deleteRecursively(databaseDir, mustExist = true)
    }
}
