package com.epam


import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*

open class CustomPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        target.tasks.configureEach {
            doLast {
                println("This task $name edited by CustomPlugin")
            }
        }

        target.tasks.create("Task from plugin") {
            doLast {
                println("I am task from plugin")
            }
        }

        val task1 = target.tasks.register("TaskFromPlugin1", Task1::class) {
            outputFile.set(target.layout.buildDirectory.file("foo"))
        }
        target.tasks.register("TaskFromPlugin2", Task2::class) {
            inputFile.set(task1.flatMap { it.outputFile })
        }
    }
}


open class Task1 : DefaultTask() {

    @OutputFile
    val outputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        outputFile.get().asFile.writeText("Hello friend")
    }

}

open class Task2 : DefaultTask() {
    @InputFile
    val inputFile: RegularFileProperty = project.objects.fileProperty()

    @TaskAction
    fun run() {
        println(String(inputFile.get().asFile.readBytes()))
    }
}
