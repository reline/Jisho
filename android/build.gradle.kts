import com.github.reline.jisho.tasks.JishoPopulateTask
import com.google.devtools.ksp.gradle.KspAATask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.wire)
    id("com.github.reline.jisho.prepopulator")
}

android {
    namespace = "com.github.reline.jisho"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.github.reline.jisho"
        minSdk = 23
        targetSdk = 34
        versionCode = 4
        versionName = "1.2.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "DATABASE_FILE_NAME",
            "\"${jisho.database.fileName}\"",
        )
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

    flavorDimensions += "environment"
    productFlavors {
        create("mock") {
            dimension = "environment"
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
        }
        create("prod") {
            dimension = "environment"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    lint {
        abortOnError = false
    }
}

androidComponents {
    beforeVariants {
        if (it.buildType == "release" && it.productFlavors.contains("environment" to "mock")) {
            // Gradle ignores any variants that satisfy the conditions above.
            it.enable = false
        }
    }

    onVariants { variant ->
        if (!variant.productFlavors.contains("environment" to "mock")) {
            variant.sources.assets?.addGeneratedSourceDirectory(
                tasks.named<JishoPopulateTask>("populateJishoDatabase"),
                JishoPopulateTask::databaseOutputDirectory,
            )
        }

        // https://github.com/square/wire/issues/2335
        val buildType = variant.buildType.toString()
        val flavor = variant.flavorName.toString()
        tasks.withType<KspAATask> {
            if (name.contains(buildType, ignoreCase = true) && name.contains(flavor, ignoreCase = true)) {
                dependsOn("generate${flavor.capitalize()}${buildType.capitalize()}Protos")
            }
        }
    }
}

jisho {
    jmdict(libs.versions.jmdictfurigana)
    githubToken.set(providers.environmentVariable("GITHUB_TOKEN"))
}

configurations {
//    compile.exclude group: "stax"
//    compile.exclude group: "xpp3"
}

wire {
    kotlin {}
}

dependencies {
    testImplementation("junit:junit:4.12")
    testImplementation(libs.androidx.test.core)
    testImplementation("org.robolectric:robolectric:4.4")

    implementation(libs.jisho.database)
    implementation("com.github.reline:sqlitecopyopenhelper:0.1.0")
    implementation(libs.sqldelight.android.driver)
    implementation(libs.androidx.sqlite)
    implementation(libs.androidx.sqlite.framework)

    implementation(libs.androidx.datastore)
    implementation(libs.wire)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.appcompat)
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.okio)

    implementation(libs.timber)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
}
