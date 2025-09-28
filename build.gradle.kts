// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Version.HILT}")
        classpath("com.android.tools.build:gradle:${Version.AGP}")
        classpath("com.google.gms:google-services:${Version.GOOGLE.GOOGLE_SERVICE}")
        classpath("com.google.firebase:firebase-crashlytics-gradle:${Version.GOOGLE.CRASHLYTICS}")

    }
}
plugins {
    id("com.android.application") version Version.AGP apply false
    id("com.android.library") version Version.AGP apply false
    id("org.jetbrains.kotlin.android") version Version.ANDROID.KOTLIN apply false
    id("com.google.devtools.ksp") version "1.9.25-1.0.20" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}