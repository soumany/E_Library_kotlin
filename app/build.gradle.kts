plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "kh.edu.ferupp.e_library_kotlin"
    compileSdk = 34

    defaultConfig {
        applicationId = "kh.edu.ferupp.e_library_kotlin"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures{
        viewBinding = true
    }
    buildFeatures {
        viewBinding = true
    }
    buildFeatures{
        viewBinding = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    implementation(libs.retrofit2.retrofit)
//    implementation(libs.squareup.converter.gson)

// Coroutines
//    implementation(libs.kotlinx.coroutines.core)
//    implementation(libs.kotlinx.coroutines.android)

// RecyclerView
//    implementation(libs.androidx.recyclerview)
    implementation ("androidx.core:core-ktx:1.9.0")        // Replace with latest if needed
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation ("com.google.android.material:material:1.12.0")
//    implementation(libs.androidx.core.ktx.v190)
//    implementation(libs.androidx.appcompat.v170)
//    implementation(libs.material.v1120)
//    implementation(libs.androidx.constraintlayout.v220)

    // Testing
//    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")


    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit.v121)
//    androidTestImplementation(libs.androidx.espresso.core.v361)
//    implementation(libs.picasso)
//    implementation (libs.retrofit2.retrofit)
//    implementation (libs.squareup.converter.gson)
//    implementation (libs.gson)
//    implementation(libs.glide)

    // Third-Party Libraries
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1") // Example version; check for updates
    implementation ("com.github.bumptech.glide:glide:4.15.0")



    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

// RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.1")

}