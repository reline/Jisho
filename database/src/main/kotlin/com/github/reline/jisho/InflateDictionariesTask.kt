package com.github.reline.jisho

import okio.buffer
import okio.sink
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.charset.Charset
import java.util.zip.ZipFile

abstract class InflateDictionariesTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Inflate monash dictionary files"

    @TaskAction
    fun inflate() = with (project) {
        outputDirectory.get().asFile.mkdirs()
        // fixme
        val dictResourcesDir = File(rootDir, "database/src/main/resources")
        dictResourcesDir.listFiles()?.forEach { file ->
            when (file.extension) {
                "zip" -> {
                    // EUC-JP [csEUCPkdFmtjapanese, x-euc-jp, eucjis, Extended_UNIX_Code_Packed_Format_for_Japanese, euc_jp, eucjp, x-eucjp]
                    val zip = ZipFile(file)
                    zip.entries().asSequence().forEach { entry ->
                        val source = zip.getInputStream(entry).source().buffer()
                        val outputFile = outputDirectory.file(entry.name).get().asFile
                        outputFile.sink().buffer().writeUtf8(source.readString(Charset.forName("EUC-JP")))
                    }
                }
                "gz" -> {
                    outputDirectory.file(file.nameWithoutExtension).get().asFile.sink().buffer()
                        .writeAll(resources.gzip(file.path).read().source().buffer())
                }
            }
        }

    }
}