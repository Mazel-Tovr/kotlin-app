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
}


val register = tasks.register("startServer", CustomTask::class) {
    dependsOn("jar")
    classpath = files("build/libs/TestServer-1.0.0.jar")
}


tasks.register("stopServer"){
    println("I should stop server but i'm int implemented what a disaster, isn't it ?")
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


