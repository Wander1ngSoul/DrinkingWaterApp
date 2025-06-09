buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")  // Обязательно для Firebase
    }
}

plugins {
    alias(libs.plugins.android.application) apply false  // Плагин приложения (версия берется из libs)
    id("com.android.library") version "7.4.0" apply false  // Плагин библиотеки
}