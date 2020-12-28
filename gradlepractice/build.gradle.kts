plugins {
    kotlin("jvm")
    id("com.epam.custom-plugin") version "1.0-SNAPSHOT"
}
apply(from = "buildScript.gradle.kts") //path to srcipt

tasks.register("testTask") {
    doFirst {
        println("First") // перед с стартом таски
    }

    println("Configure") // на этапе конфигурации

    doLast {
        println("Last") // перед завершением
    }
}

val taskA = tasks.register("TaskA") {
    doLast {
        println("task a")
    }
}
val taskB = tasks.register("TaskB") {
    doLast {
        println("task b")
    }
}

val taskC = tasks.register("TaskC") {
    doLast {
        println("task c")
    }
}
taskA.get().dependsOn(taskB)
taskB.get().dependsOn(taskC)
//taskC.get().mustRunAfter(taskA) //Если мы запустим таску А то градл упадет с ошибкой связанной с циклической зависимостью
taskC.get().shouldRunAfter(taskA) // А если использовать should то ошибки не будет

repositories {
    mavenCentral()
}

//val someConfiguration by configurations.creating

dependencies {

//   Example of exclude dependecies from local module
//    implementation(project(":TestServer")) {
//        exclude("org.slf4j")
//        exclude("org.jetbrains.kotlin")
//    }
    implementation("org.slf4j:slf4j-api:1.7.16")
//   Example of exclude dependecies by transitive flag
//    implementation("org.slf4j:slf4j-log4j12:1.7.30") {
//        isTransitive = false
//    }

//  Example of exclude dependecies
    implementation("org.apache.hadoop:hadoop-common:3.1.3") {
        // exclude("org.slf4j","slf4j-log4j12")
    }


    testImplementation("junit:junit:4.12")
    testImplementation("org.slf4j:slf4j-simple:1.7.16") {

    }

    //google collection -> guava
    modules {
        module("org.slf4j:slf4j-log4j12") {
            replacedBy(
                "org.slf4j:slf4j-simple",
                "The reason why we want to use this module is BECAUSE"
            )
        }
    }

}


configurations.all {
    //  preferSl4jSimple()
}
configurations {
    // Зависимости по умолчанию
    // Основной пример использования этой функциональности - разработка плагинов, использующих версионные инструменты,
    // которые пользователь может переопределить.
    // Указав зависимости по умолчанию, плагин может использовать версию инструмента по умолчанию только в том случае,
    // если пользователь не указал конкретную версию для использования.
    create("pluginTool") {
        defaultDependencies {
            add(project.dependencies.create("org.gradle:my-util:1.0"))
        }
    }
}


open class OneMoreTask() : DefaultTask() {

    @org.gradle.api.tasks.Input
    var inputText: String = "Text"

    lateinit var worker: WorkerExecutor

    @javax.inject.Inject
    constructor(worker: WorkerExecutor) : this() {
        this.worker = worker
    }

    @org.gradle.api.tasks.TaskAction
    fun run() {
        println(inputText)
        val noIsolation = worker.noIsolation { }

    }
}



tasks.register("Test script", OneMoreTask::class.java) {
    inputText = "New text"
    doLast {
        println(project.extra.properties["Test"])
    }
}


fun Configuration.preferSl4jSimple() = resolutionStrategy.dependencySubstitution {

    // substitute(module("org.utils:util:2.5")).with(project(":util"))
    // or
    // substitute(project(":util")).with(module("org.utils:util:2.5"))

    substitute(module("org.slf4j:slf4j-log4j12"))
        .because("The reason why we want to use this module is BECAUSE")
        .with(module("org.slf4j:slf4j-simple:1.7.16"))// in 6.7.1 gradle version using "using" instead of "with"
}

subprojects {

    apply<org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin>()


    tasks.register("Subproject task check") {
        doLast {
            println("Sup bro")
        }
    }

    dependencies {
        implementation("org.webjars.npm:uuid:8.3.2") //test depencency
    }

}

allprojects {

    apply<org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin>()

    dependencies {
        implementation("org.webjars.npm:mocha:8.2.1")
    }
}

var a = "one"

beforeEvaluate {
    println("Before evaluate")
    a = "two"
}

afterEvaluate {
    println("After evaluate")
    println(a)
}

open class WP : WorkParameters

//TODO it's in progress
open class WorkerApi : WorkAction<WP> {

    override fun execute() {
        TODO("Not yet implemented")
    }

    override fun getParameters(): WP {
        return WP()
    }
}
