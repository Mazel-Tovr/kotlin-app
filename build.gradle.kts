import org.jetbrains.kotlin.gradle.tasks.*

plugins {
//    kotlin("plugin.serialization")

    kotlin("plugin.serialization") version "1.3.70"
    kotlin("jvm") version "1.3.70"
    application


}
group = "com.epam"
version = "1.0-SNAPSHOT"


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
    gradlePluginPortal()
    maven(url = "https://oss.jfrog.org/artifactory/list/oss-release-local")
    maven(url = "https://dl.bintray.com/kotlin/kotlinx/")
    maven(url = "http://oss.jfrog.org/oss-release-local")
}



dependencies {
    val ktorVersion: String = "1.4.0"
    val h2Version: String = "1.4.200"
    val loggerVersion: String = "1.7.30"
    val immutableCollectionsVersion = "0.3.3"
    val serializationVersion = "0.20.0"
    val swaggerVersion = "0.5.0-drill.2"

    testImplementation(kotlin("test-junit"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("io.ktor:ktor-gson:$ktorVersion")
    implementation("org.slf4j:slf4j-simple:$loggerVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

    implementation("com.github.Drill4J:ktor-swagger:$swaggerVersion")
    implementation("junit:junit:4.4")
    implementation("io.ktor:ktor-server-test-host:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:$immutableCollectionsVersion")

    implementation("org.jetbrains.xodus:xodus-entity-store:1.3.232")
    implementation("org.jetbrains.xodus:dnq:1.4.480")

    testImplementation("io.mockk:mockk:1.10.3-jdk8")


}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "com.epam.kotlinapp.ApplicationKt"

}



tasks.withType<Test> {
   // dependsOn(":TestServer:startServer")


}



