import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("application")
    id("com.github.johnrengelman.shadow") version "4.0.4"
}
group = "com.epam"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "com.epam.kotlinapp.ApplicationKt"
}
tasks {
    build {
        dependsOn(shadowJar)
    }
}

repositories {
    mavenCentral()
    jcenter()
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://dl.bintray.com/kotlin/ktor")
    }
    maven {
        url = uri("https://dl.bintray.com/kotlin/kotlinx")
    }
}
dependencies {
    val ktorVersion: String = "1.4.0"
    val h2Version: String = "1.4.200"
    val loggerVersion: String = "1.7.30"
    testImplementation(kotlin("test-junit"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    //implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2") //I don't think i need this
    implementation("com.h2database:h2:$h2Version")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("org.slf4j:slf4j-simple:$loggerVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    //implementation("com.github.papsign:Ktor-OpenAPI-Generator:-SNAPSHOT")
    implementation("com.github.nielsfalk:ktor-swagger:v0.7.0")
    implementation("junit:junit:4.4")
    implementation("io.ktor:ktor-server-test-host:$ktorVersion")
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}