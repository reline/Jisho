package com.github.reline.jisho

import java.io.File

fun File.forceCreate() {
    parentFile.mkdirs()
    delete()
    createNewFile()
}
