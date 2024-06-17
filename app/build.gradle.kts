
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
//    id("com.android.library") version "7.4.1" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" // Depends on your kotlin version
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}


android {
    namespace = "com.example.contentsquare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.contentsquare"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packagingOptions {
        resources {
            excludes += setOf("META-INF/AL2.0", "META-INF/LGPL2.1", "META-INF/licenses/ASM")
        }
    }

    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

object ComposeDestination {
    // https://composedestinations.rafaelcosta.xyz/setup#:~:text=1.3%20(1.3.x)-,Compose%201.4%20(1.4.x),-CAUTION
    private val core_version = "1.8.41-beta"
    val destination = "io.github.raamcosta.compose-destinations:core:${core_version}"
    val destinations_animation_core = "io.github.raamcosta.compose-destinations:animations-core:${core_version}"
    val destination_ksp = "io.github.raamcosta.compose-destinations:ksp:${core_version}"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
//    implementation(libs.androidx.lifecycle.runtime.compose.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    "implementation"("androidx.compose.ui:ui") {
        version {
            strictly("1.4.3")
        }
    }
    "implementation"("androidx.compose.runtime:runtime") {
        version {
            strictly("1.4.3")
        }
    }
    "implementation"("com.contentsquare.android:library:4.27.1")
    "implementation"("com.contentsquare.android:compose:4.27.1")
    "api"(ComposeDestination.destination)
    "api"(ComposeDestination.destinations_animation_core)
    "ksp"(ComposeDestination.destination_ksp)
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    annotationProcessor("com.google.dagger:hilt-android:2.45")
////
////
    "implementation"("androidx.hilt:hilt-navigation-compose:1.0.0")
//     ViewModel
    "implementation"("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

//     ViewModel utilities for Compose
    "implementation"("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    "api"("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.hilt:hilt-work:1.2.0")
//    ksp("androidx.hilt:hilt-compiler:1.2.0")
    "implementation"("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-preferences-core:1.1.1")
    implementation("com.google.code.gson:gson:2.11.0")

}