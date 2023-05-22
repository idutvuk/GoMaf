// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.8.0-1.0.8" apply false
//    id("com.google.devtools.ksp") version "1.8.20-1.0.11" apply false
}