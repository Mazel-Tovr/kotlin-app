plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.epam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
   implementation(kotlin("stdlib","1.3.70"))
}

gradlePlugin {
    plugins {
        create("custom-plugin") {
            id = "com.epam.custom-plugin"
            implementationClass = "com.epam.CustomPlugin"
        }
    }
}
