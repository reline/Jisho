package com.github.reline.jisho.compression

import com.github.reline.jisho.EUC_JP
import com.github.reline.jisho.ensureDirsCreated
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.gzip
import okio.openZip
import java.io.IOException
import java.nio.charset.Charset

// todo: unit tests
class DictionaryFileDecompressor(
    private val resources: FileSystem = FileSystem.RESOURCES,
    private val fileSystem: FileSystem = FileSystem.SYSTEM,
) {
    /**
     * Extract all compressed files in the given [path].
     *
     * Zip files are expected to be from Monash.
     * The Monash zip file contents are all in EUC-JP format AKA:
     * - EUC-JP
     * - csEUCPkdFmtjapanese
     * - x-euc-jp
     * - Extended_UNIX_Code_Packed_Format_for_Japanese
     * - eucjis
     * - euc_jp
     * - eucjp
     * - eucjp
     */
    @Throws(IOException::class)
    fun extractAll(path: Path, destination: Path) {
        // todo: look into FileSystem#createDirectories
        destination.toFile().ensureDirsCreated()

        resources.list(path).forEach {
            when (it.toFile().extension) {
                "zip" -> extractZip(it, destination, EUC_JP)
                "gz" -> extractGzip(it, destination)
            }
        }
    }

    @Throws(IOException::class)
    fun extractZip(
        zipPath: Path,
        destinationDirectory: Path,
        charset: Charset = Charsets.UTF_8,
    ) {
        val zip = resources.openZip(zipPath)
        zip.list("/".toPath()).forEach { path ->
            zip.read(path) {
                fileSystem.write(destinationDirectory/path.name) {
                    // todo: investigate performance
                    writeUtf8(buffer.readString(charset))
                }
            }
        }
    }

    @Throws(IOException::class)
    fun extractGzip(gzip: Path, destination: Path) {
        resources.source(gzip).gzip().buffer().use { source ->
            fileSystem.write(destination/gzip.toFile().nameWithoutExtension) {
                source.readAll(this)
            }
        }
    }

}
