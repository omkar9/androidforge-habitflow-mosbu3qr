import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Core plugins
    id("com.android.application") version "8.3.2" apply false
    id("com.android.library") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false

    // Hilt plugin
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    // KSP (though we use KAPT, KSP might be used in other projects or future integrations)
    id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false
}

// Common configuration for Android modules
subprojects {
    afterEvaluate {
        plugins.findPlugin("com.android.application")?.let { plugin ->
            (this as org.gradle.api.Project).extensions.configure<ApplicationExtension>("android") {
                compileSdk = 34
                defaultConfig {
                    minSdk = 24
                    targetSdk = 34
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                kotlinOptions {
                    jvmTarget = "17"
                }
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.11"
                }
            }
        }

        plugins.findPlugin("com.android.library")?.let { plugin ->
            (this as org.gradle.api.Project).extensions.configure<LibraryExtension>("android") {
                compileSdk = 34
                defaultConfig {
                    minSdk = 24
                    targetSdk = 34
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                kotlinOptions {
                    jvmTarget = "17"
                }
                buildFeatures {
                    compose = true
                }
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.5.11"
                }
            }
        }

        plugins.findPlugin("org.jetbrains.kotlin.android")?.let { plugin ->
            (this as org.gradle.api.Project).extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
                jvmToolchain(17)
            }
        }
    }
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}