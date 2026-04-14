plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.workmanager"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.workmanager"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- CÁC THƯ VIỆN CÓ SẴN (GIỮ NGUYÊN) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- CÁC THƯ VIỆN CẦN THÊM MỚI ---

    // 1. WorkManager (Bắt buộc phải có để chạy Worker)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // 2. ViewModel & Compose ViewModel (Để UI gọi được Repository)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // 3. Coil (Để hiển thị ảnh từ URI hoặc Internet một cách mượt mà)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // --- CÁC THƯ VIỆN TEST (GIỮ NGUYÊN) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}