package com.github.reline.jisho

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import okio.buffer
import okio.gzip
import okio.openZip
import okio.sink
import okio.source
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class InflateDictionariesTask : DefaultTask() {
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    override fun getDescription() = "Inflate monash dictionary files"

    private val fileSystem get() = FileSystem.SYSTEM

    @TaskAction
    fun inflate() = with (project) {
        outputDirectory.get().asFile.mkdirs()
        File(rootDir, "database/src/main/resources").listFiles()?.forEach { file ->
            when (file.extension) {
                "zip" -> {
                    // EUC-JP [csEUCPkdFmtjapanese, x-euc-jp, eucjis, Extended_UNIX_Code_Packed_Format_for_Japanese, euc_jp, eucjp, x-eucjp]
                    val zip = fileSystem.openZip(file.toOkioPath())
                    zip.list("/".toPath()).forEach { path ->
                        zip.source(path).buffer().use { source ->
                            val sink = outputDirectory.file(path.name).get().asFile.sink().buffer()
                            sink.writeUtf8(source.readEucJp())
                        }
                    }
                }
                "gz" -> {
                    file.source().gzip().buffer().use { gzip ->
                        val outputFile = outputDirectory.file(file.nameWithoutExtension).get().asFile
                        fileSystem.write(outputFile.toOkioPath()) {
                            // fixme: investigate why writeAll does not work,
                            //  but reading fully to string or line by line does
                            gzip.readAll(this)
//                            writeAll(gzip) // essentially gzip.read"All"(this@sink)
//                            write(gzip.readByteArray())
                        }
                    }
                }
            }
        }

    }
}