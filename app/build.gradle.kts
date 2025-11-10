import com.roemsoft.plugins.*

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.roemsoft.hengmao"
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = "com.roemsoft.hengmao"

        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
        versionCode = BuildConfig.versionCode
        versionName = BuildConfig.versionName

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("doc/roem.jks")
            storePassword = if (project.hasProperty("KEYSTORE_PASS")) {
                project.property("KEYSTORE_PASS") as String
            } else {
                System.getenv("KEYSTORE_PASS")
            }
            keyAlias = System.getenv("ALIAS_NAME")
            keyPassword = System.getenv("ALIAS_PASS")
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            multiDexEnabled = true
            signingConfig = signingConfigs.getByName("release")
        }

        release {
            isDebuggable = false
            isMinifyEnabled = false
            multiDexEnabled = true
            signingConfig = signingConfigs.getByName("release")
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
        dataBinding = true
        buildConfig = true
    }

    viewBinding {
        enable = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    android.applicationVariants.all {
        outputs.all {
            val version = defaultConfig.versionName
            val buildType = buildType.name
            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "HengMao_${buildType}_v${version}.apk"
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false

        disable += "CheckResult"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.activity:activity-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.slidingpanelayout:slidingpanelayout:1.2.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.window:window:1.3.0")
    implementation("androidx.window:window-core:1.3.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("com.github.michaellee123:LiveEventBus:1.8.14")
    implementation("com.guolindev.permissionx:permissionx:1.8.1")
}