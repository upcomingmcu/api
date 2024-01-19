plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
}

group = "app.umcu"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.serialization)
    implementation(libs.micrometer.registry.prometheus)
    implementation(libs.logback)
    implementation(libs.h2)
    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.kompendium)
    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test.junit)
}
