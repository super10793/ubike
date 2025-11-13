import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties = Properties().apply {
    load(FileInputStream(secretsPropertiesFile))
}

android {
    namespace = "com.demo.ubike"
    compileSdk = Version.ANDROID.COMPILE_SDK

    defaultConfig {
        applicationId = "com.demo.ubike"
        minSdk = Version.ANDROID.MIN_SDK
        targetSdk = Version.ANDROID.TARGET_SDK
        versionCode = 1003
        versionName = "1.0.03"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_CLIENT_ID_1", "\"${secretsProperties["API_CLIENT_ID_1"]}\"")
        buildConfigField(
            "String",
            "API_CLIENT_SECRET_1",
            "\"${secretsProperties["API_CLIENT_SECRET_1"]}\""
        )
        buildConfigField("String", "API_CLIENT_ID_2", "\"${secretsProperties["API_CLIENT_ID_2"]}\"")
        buildConfigField(
            "String",
            "API_CLIENT_SECRET_2",
            "\"${secretsProperties["API_CLIENT_SECRET_2"]}\""
        )
        buildConfigField("String", "API_CLIENT_ID_3", "\"${secretsProperties["API_CLIENT_ID_3"]}\"")
        buildConfigField(
            "String",
            "API_CLIENT_SECRET_3",
            "\"${secretsProperties["API_CLIENT_SECRET_3"]}\""
        )

        manifestPlaceholders["mapsApiKey"] = secretsProperties.getProperty("MAPS_API_KEY")
    }

    signingConfigs {
        create("release") {
            storeFile = file(secretsProperties["KEYSTORE_FILE_PATH"] as String)
            storePassword = secretsProperties["KEYSTORE_PASSWORD"] as String
            keyAlias = secretsProperties["KEYSTORE_ALIAS"] as String
            keyPassword = secretsProperties["KEYSTORE_ALIAS_PASSWORD"] as String
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            manifestPlaceholders["appName"] = "@string/app_name_debug"
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            applicationIdSuffix = ""
            manifestPlaceholders["appName"] = "@string/app_name"
        }
    }

    sourceSets {
        getByName("debug") {
            java.srcDirs("src/buildTypes/debug")
        }
        getByName("release") {
            java.srcDirs("src/buildTypes/release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // dataBinding 設定
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

}

dependencies {
    implementation(Libs.ANDROIDX.CORE)
    implementation(Libs.ANDROIDX.APPCOMPAT)
    implementation(Libs.ANDROIDX.CONSTRAINTLAYOUT)
    implementation(Libs.ANDROIDX.LIFECYCLE.LIVEDATA_KTX)
    implementation(Libs.ANDROIDX.LIFECYCLE.VIEWMODEL_KTX)
    implementation(Libs.ANDROIDX.NAVIGATION.FRAGMENT_KTX)
    implementation(Libs.ANDROIDX.NAVIGATION.UI_KTX)

    implementation(Libs.GOOGLE.MATERIAL)
    implementation(Libs.GOOGLE.MAP)
    implementation(Libs.GOOGLE.MAPS_UTILS)

    implementation(Libs.HILT.ANDROID)
    ksp(Libs.HILT.COMPILER)

    implementation(Libs.SQUARE.RETROFIT2)
    implementation(Libs.SQUARE.RETROFIT2_GSON)
    implementation(Libs.SQUARE.OKHTTP3)

    debugImplementation(Libs.FACEBOOK.FLIPPER)
    debugImplementation(Libs.FACEBOOK.SO_LOADER)
    debugImplementation(Libs.FACEBOOK.PLUGIN_NETWORK)
    releaseImplementation(Libs.FACEBOOK.NOOP)

    implementation(Libs.ROOM.RUNTIME)
    ksp(Libs.ROOM.COMPILER)
    implementation(Libs.ROOM.KTX)

    implementation(Libs.ANDROIDX.DATASTORE)

    implementation(Libs.BLANKJ_UTILS)

    // Import the BoM for the Firebase platform
    implementation(platform(Libs.GOOGLE.FIREBASE_BOM))

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(Libs.GOOGLE.FIREBASE_CRASHLYTICS_KTX)
    implementation(Libs.GOOGLE.FIREBASE_ANALYTICS_KTX)
}
