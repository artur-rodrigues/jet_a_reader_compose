import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    alias(libs.plugins.hilt)
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.goggle.service)
}

val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
val firebaseFireStore = "com.google.firebase:firebase-firestore-ktx"

val apiKeyPropertiesFile = rootProject.file("apikey.properties")
val apiKeyProperties = Properties()
apiKeyProperties.load(FileInputStream(apiKeyPropertiesFile))

android {
    namespace = "com.example.jetaeader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jetaeader"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField ("String", "API_KEY", apiKeyProperties["API_KEY"].toString())
            buildConfigField ("String", "BASE_URL", apiKeyProperties["BASE_URL"].toString())
            buildConfigField ("String", "IMAGE_URL", apiKeyProperties["IMAGE_URL"].toString())
        }

        release {
            buildConfigField ("String", "API_KEY", apiKeyProperties["API_KEY"].toString())
            buildConfigField ("String", "BASE_URL", apiKeyProperties["BASE_URL"].toString())
            buildConfigField ("String", "IMAGE_URL", apiKeyProperties["IMAGE_URL"].toString())

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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
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

    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.hilt.navigation)

    ksp(libs.google.hilt.compiler)
    ksp(libs.google.dagger.compiler)
    implementation(libs.google.hilt)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.gson)

    implementation(libs.square.okhttp)
    implementation(libs.square.logging.interceptor)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.kotlin)

    implementation(libs.io.coil.kt)

    implementation(libs.live.data)

    implementation(platform(libs.fire.base))
    implementation(firebaseAuth)
    implementation(firebaseFireStore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}