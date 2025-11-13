object Libs {

    const val BLANKJ_UTILS = "com.blankj:utilcodex:${Version.BLANKJ_UTILS}"

    object ANDROIDX {

        const val CORE = "androidx.core:core-ktx:${Version.ANDROIDX.CORE_KTX}"

        const val APPCOMPAT = "androidx.appcompat:appcompat:${Version.ANDROIDX.APPCOMPAT}"

        const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:${Version.ANDROIDX.CONSTRAINTLAYOUT}"

        const val DATASTORE = "androidx.datastore:datastore-preferences:${Version.ANDROIDX.DATASTORE}"

        object LIFECYCLE {

            const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.ANDROIDX.LIFECYCLE.LIVEDATA_KTX}"

            const val VIEWMODEL_KTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.ANDROIDX.LIFECYCLE.VIEWMODEL_KTX}"
        }

        object NAVIGATION {

            const val FRAGMENT_KTX = "androidx.navigation:navigation-fragment-ktx:${Version.ANDROIDX.NAVIGATION}"

            const val UI_KTX = "androidx.navigation:navigation-ui-ktx:${Version.ANDROIDX.NAVIGATION}"
        }
    }

    object GOOGLE {

        const val MATERIAL = "com.google.android.material:material:${Version.GOOGLE.MATERIAL}"

        const val MAP = "com.google.android.gms:play-services-maps:${Version.GOOGLE.MAP}"

        const val MAPS_UTILS = "com.google.maps.android:android-maps-utils:${Version.GOOGLE.MAPS_UTILS}"

        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Version.GOOGLE.FIREBASE_BOM}"

        const val FIREBASE_CRASHLYTICS_KTX = "com.google.firebase:firebase-crashlytics-ktx"

        const val FIREBASE_ANALYTICS_KTX = "com.google.firebase:firebase-analytics-ktx"
    }

    object HILT {

        const val ANDROID = "com.google.dagger:hilt-android:${Version.HILT}"

        const val COMPILER = "com.google.dagger:hilt-android-compiler:${Version.HILT}"
    }

    object SQUARE {

        const val RETROFIT2 = "com.squareup.retrofit2:retrofit:${Version.SQUARE.RETROFIT2}"

        const val RETROFIT2_GSON = "com.squareup.retrofit2:converter-gson:${Version.SQUARE.RETROFIT2}"

        const val OKHTTP3 = "com.squareup.okhttp3:okhttp:${Version.SQUARE.OKHTTP3}"
    }

    object FACEBOOK {

        const val FLIPPER = "com.facebook.flipper:flipper:${Version.FACEBOOK.FLIPPER}"

        const val SO_LOADER = "com.facebook.soloader:soloader:${Version.FACEBOOK.SO_LOADER}"

        const val NOOP = "com.facebook.flipper:flipper-noop:${Version.FACEBOOK.FLIPPER}"

        const val PLUGIN_NETWORK = "com.facebook.flipper:flipper-network-plugin:${Version.FACEBOOK.FLIPPER}"
    }

    object ROOM {

        const val RUNTIME = "androidx.room:room-runtime:${Version.ROOM}"
        const val COMPILER = "androidx.room:room-compiler:${Version.ROOM}"
        const val KTX = "androidx.room:room-ktx:${Version.ROOM}"
    }
}