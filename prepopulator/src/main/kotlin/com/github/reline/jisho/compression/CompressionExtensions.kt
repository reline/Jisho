package com.github.reline.jisho.compression

import com.github.reline.jisho.text.EUC_JP
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.Sink
import okio.Source
import okio.buffer
import okio.gzip
import okio.openZip
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.charset.Charset

private val logger by lazy { LoggerFactory.getLogger("compress-logger") }

private val ROOT = "/".toPath()

typealias FileSystemPath = Pair<FileSystem, Path>

/**
 * Extract the compressed zip file contents at [zipPath] into a [destination] directory.
 *
 * Zip files are expected to be from Monash.
 * The Monash zip file contents are all in EUC-JP format AKA:
 * - EUC-JP
 * - csEUCPkdFmtjapanese
 * - x-euc-jp
 * - Extended_UNIX_Code_Packed_Format_for_Japanese
 * - eucjis
 * - euc_jp
 * - eucjp.kt
 * - eucjp.kt
 */
@Throws(IOException::class)
fun FileSystem.extractZip(
    zipPath: Path,
    destination: FileSystemPath,
    charset: Charset = Charsets.EUC_JP,
): List<Path> {
    val (fileSystem, base) = destination
    if (!fileSystem.exists(base)) {
        fileSystem.createDirectories(base, mustCreate = true)
    }
    // fixme:
    //  Caused by: java.lang.UnsupportedOperationException: not implemented yet!
    //	 at okio.ZipFileSystem.openReadOnly(ZipFileSystem.kt:79)
    //	 at okio.internal.ResourceFileSystem.openReadOnly(ResourceFileSystem.kt:106)
    //	 at okio.internal.ZipFilesKt.openZip(ZipFiles.kt:66)
    //	 at okio.internal.ZipFilesKt.openZip$default(ZipFiles.kt:61)
    //	 at okio.Okio__ZlibOkioKt.openZip(ZlibOkio.kt:26)
    //   at okio.Okio.openZip(Unknown Source)
    //	 at com.github.reline.jisho.compression.CompressionExtensionsKt.extractZip(CompressionExtensions.kt:46)
    //	 at com.github.reline.jisho.compression.CompressionExtensionsKt.extractZip$default(CompressionExtensions.kt:37)
    val zip = openZip(zipPath)
    logger.info("Extracting $zipPath to $base")
    return zip.list(ROOT).map { path ->
        val file = base/path.name
        // todo: inject logger
        logger.debug("Extracting $path to $base")
        // todo: use `copy(zip.source(path), fileSystem.sink(file))`
        zip.read(path) {
            fileSystem.write(file) {
                // todo: investigate performance
                // todo: inject charset
                writeUtf8(readString(charset))
            }
        }
        return@map file
    }
}

@Throws(IOException::class)
fun FileSystem.extractGzip(gzip: Path, destination: FileSystemPath): Path {
    val (fileSystem, file) = destination
    val parent = requireNotNull(file.parent)
    if (!fileSystem.exists(parent)) {
        fileSystem.createDirectories(parent, mustCreate = true)
    }
    logger.info("Extracting $gzip to $file")
    // todo: use `copy(source(gzip).gzip(), fileSystem.sink(file))`
    source(gzip).gzip().buffer().use { source ->
        fileSystem.write(file) {
            source.readAll(this)
        }
    }
    return file
}

@Throws(IOException::class)
internal fun copy(source: Source, target: Sink) {
    source.use { bytesIn ->
        target.buffer().use { bytesOut ->
            bytesOut.writeAll(bytesIn)
        }
    }
}
