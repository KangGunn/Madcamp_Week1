// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

//buildscript {
//    val version by extra("2.8.5")
//
//    dependencies {
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$version")
//    }
//}