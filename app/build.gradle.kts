fun kapt(s: String) {

}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.allomlar"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.allomlar"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //masshtab image
    implementation ("com.jsibbold:zoomage:1.3.1")

    //Lifecycle Components
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:lifecycle_version")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:lifecycle_version")

    //Logging
    implementation ("com.jakewharton.timber:timber:timber_version")

    //Picasso
    implementation ("com.squareup.picasso:picasso:2.71828")

    //Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.4.1")
    implementation ("androidx.navigation:navigation-ui-ktx:2.4.1")

    // bottom indicator for viewPager2
    implementation ("com.tbuonomo:dotsindicator:4.2")

    //ViewPager2
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

    //Collapsing AppBar
    implementation (" com.google.android.material:material:1.6.0-alpha02")
    implementation ("androidx.appcompat:appcompat:1.4.1")

    //Hilt
    implementation ("com.google.dagger:hilt-android:2.41")
    kapt ("com.google.dagger:hilt-compiler:2.41")

    //  Coroutines
    implementation( "org.jetbrains.kotlinx:kotlinx-coroutines-core:coroutines_version")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:coroutines_version")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:retrofit_version")
    implementation ("com.squareup.retrofit2:converter-gson:retrofit_version")
    implementation ("com.squareup.okhttp3:logging-interceptor:httplogging_version")
    implementation ( "com.google.code.gson:gson:json_version")

    //Room
    implementation("androidx.room:room-runtime:room_version")
    implementation("androidx.room:room-ktx:room_version")
    kapt("androidx.room:room-compiler:room_version")

    //Gsong
    implementation ("com.google.code.gson:gson:2.8.7")
}