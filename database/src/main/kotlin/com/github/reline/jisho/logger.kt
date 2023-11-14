package com.github.reline.jisho

import java.util.logging.Level
import java.util.logging.Logger

val logger: Logger by lazy {
    System.setProperty("java.util.logging.SimpleFormatter.format",
        "%1\$tF %1\$tT - %5\$s%n") // yyyy-MM-dd hh:mm:ss - %s\n
    Logger.getLogger("Jisho")
}

fun Logger.debug(s: String) {
    log(Level.ALL, s)
}

fun Logger.error(s: String) {
    log(Level.SEVERE, s)
}