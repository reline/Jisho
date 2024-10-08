package com.github.reline.jisho.artifactrepository

import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

fun RepositoryHandler.edrdg() = ivy {
    name = "EDRDG"
    url = URI("http://ftp.edrdg.org/pub/Nihongo")
    isAllowInsecureProtocol = true
    patternLayout {
        artifact("[module]([revision])(.[classifier])(.[ext])")
    }
    metadataSources {
        artifact()
    }
    content {
        // only search for artifacts in this repository if the group is "org.edrdg"
        includeGroup("org.edrdg")
    }
}
