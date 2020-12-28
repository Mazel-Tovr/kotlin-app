import kotlinx.coroutines.*

plugins {
    kotlin("jvm")
}

group = "com.epam"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-api:1.7.30")

}


val register = tasks.register("startServer", CustomTask::class) {
    dependsOn("jar")
    classpath = files("build/libs/TestServer-1.0.0.jar")
}


tasks.register("stopServer") {
    //TODO And implementation
}

open class CustomTask : JavaExec() {
    override fun exec() {
        GlobalScope.launch {
            super.exec()
        }
    }
}

tasks.withType(Jar::class) {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "com.epam.Main"
    }
}


