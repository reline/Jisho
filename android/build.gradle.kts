import com.github.reline.jisho.tasks.JishoPopulateTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt.android)
    id("com.github.reline.jisho.prepopulator")
}

android {
    namespace = "com.github.reline.jisho"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.github.reline.jisho"
        minSdk = 23
        targetSdk = 33
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
        viewBinding = true
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
            variant.sources.assets?.let { assets ->
                assets.addGeneratedSourceDirectory(
                    tasks.named<JishoPopulateTask>("populateJishoDatabase"),
                    JishoPopulateTask::databaseOutputDirectory,
                )
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

kapt {
    correctErrorTypes = true
}

//tasks.withType(org.jetbrains.kotlin.gradle.tasks.KaptGenerateStubs).configureEach {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}

dependencies {
    testImplementation("junit:junit:4.12")
    testImplementation("androidx.test:core:1.3.0")
    testImplementation("org.robolectric:robolectric:4.4")

    implementation(libs.jisho.database)
    implementation("com.github.reline:sqlitecopyopenhelper:0.1.0")
    implementation(libs.sqldelight.android.driver)
    implementation(libs.androidx.sqlite)
    implementation(libs.androidx.sqlite.framework)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    implementation(libs.retrofit)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.moshi)
    kapt(libs.moshi.codegen)

    implementation(libs.okio)

    implementation(libs.timber)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)
}
