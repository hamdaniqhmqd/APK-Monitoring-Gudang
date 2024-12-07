plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.tugas.aplikasimonitoringgudang"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tugas.aplikasimonitoringgudang"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunnerArguments["clearPackageData"] = "true"
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
    buildFeatures {
        viewBinding = true
    }
    dataBinding {
        enable = true
    }
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    implementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestUtil("androidx.test:orchestrator:1.5.1")

    // ViewModel and LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Room
    ksp(libs.androidx.room.compiler)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:compiler:4.16.0")
}
