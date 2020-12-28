pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}

rootProject.name = "kotlin-app"
include(":TestServer")
include(":gradlepractice")
include(":plugin")
include(":gradlepractice:subproject")
