package com.github.reline.jisho.compression

import com.github.reline.jisho.EUC_JP
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.gzip
import okio.openZip
import java.io.IOException
import java.nio.charset.Charset

class DictionaryFileDecompressor(
    private val resources: FileSystem = FileSystem.RESOURCES,
    private val system: FileSystem = FileSystem.SYSTEM,
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
        if (!system.exists(destination)) {
            system.createDirectories(destination, mustCreate = true)
        }

        resources.list(path).forEach {
            when (it.toFile().extension) {
                "zip" -> resources.extractZip(it, system to destination, EUC_JP)
                "gz" -> resources.extractGzip(it, system to destination)
            }
        }
    }
}

private val ROOT = "/".toPath()

@Throws(IOException::class)
private fun FileSystem.extractZip(
    zipPath: Path,
    destination: Pair<FileSystem, Path>,
    charset: Charset = Charsets.UTF_8,
) {
    val (fileSystem, base) = destination
    val zip = openZip(zipPath)
    zip.list(ROOT).forEach { path ->
        // todo: inject logger
        println("Extracting ${zipPath.name} from ${zipPath.name} to ${base.name}")
        zip.read(path) {
            fileSystem.write(base/path.name) {
                // todo: investigate performance
                writeUtf8(readString(charset))
            }
        }
    }
}

@Throws(IOException::class)
private fun FileSystem.extractGzip(gzip: Path, destination: Pair<FileSystem, Path>) {
    val (fileSystem, base) = destination
    println("Extracting ${gzip.name} to ${base.name}")
    source(gzip).gzip().buffer().use { source ->
        fileSystem.write(base/gzip.toFile().nameWithoutExtension) {
            source.readAll(this)
        }
    }
}
