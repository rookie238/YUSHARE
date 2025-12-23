plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.yushare"

    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.yushare"
        minSdk = 24
        targetSdk = 34 // Burayı da 36 yerine 34 yapalım.
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileSdk = 34 // "release(36)" yerine standart 34 veya 35 kullanmak daha güvenlidir, ama hata verirse 34 yap.



    defaultConfig {
        applicationId = "com.example.yushare"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {


    implementation("androidx.compose.material:material-icons-extended:1.7.6")
    implementation("androidx.navigation:navigation-compose:2.8.5")



    // --- STANDART ANDROID & COMPOSE KÜTÜPHANELERİ ---

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)


    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.navigation.compose)


    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.foundation)


    // --- TESTLER ---

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- FIREBASE (BOM YÖNETİMİ) ---
    // BOM sayesinde versiyon numarası yazmamıza gerek kalmaz
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Auth (Giriş işlemleri)
    implementation("com.google.firebase:firebase-auth")

    // Firestore (Veritabanı)
    implementation("com.google.firebase:firebase-firestore")

    // Storage (Resim Yükleme) - HATA VEREN ESKİ SATIR SİLİNDİ, DOĞRUSU BU:
    implementation("com.google.firebase:firebase-storage")

    // --- DİĞER KÜTÜPHANELER ---
    // Navigasyon
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Coil (Resim Gösterme)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // BU SATIRI EKLE (Genişletilmiş İkon Paketi):
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    implementation("com.cloudinary:cloudinary-android:2.5.0")
}