plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.22"
}

group = "com.thebrownfoxx"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.7")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}