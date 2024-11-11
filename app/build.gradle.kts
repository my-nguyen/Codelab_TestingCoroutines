plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.nguyen.codelab_testingcoroutines"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nguyen.codelab_testingcoroutines"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }
    testOptions.unitTests {
        isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.bundles.androidx.room)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.timber)
    implementation(libs.coroutines.android)
    testImplementation(libs.hamcrest)
    testImplementation(libs.core.ktx)
    testImplementation(libs.junit.ktx)
    testImplementation(libs.robolectric)
    testImplementation(libs.core.testing)

    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.coroutines.test)
    implementation(libs.fragment.testing)
    implementation(libs.core)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.dexmaker.mockito)
    androidTestImplementation(libs.espresso.contrib)
}