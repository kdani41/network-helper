plugins {
    alias(libs.plugins.kotlinx.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.maven.publish)
}
group = "com.kdani.ktor.helper"

kotlin {
    jvm() // For Android
    ios() // For iOS

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.ktor.client.core)
                api(libs.ktor.client.json)
                api(libs.ktor.client.serialization)
                api(libs.ktor.content.negotiation)
                api(libs.ktor.client.logging)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.kotlinx.serialization)
                implementation(libs.ktor.networkjvm)
                implementation(libs.coroutines.core)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
    }
}
