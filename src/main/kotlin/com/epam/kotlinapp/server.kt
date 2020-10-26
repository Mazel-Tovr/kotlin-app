package com.epam.kotlinapp

import com.epam.kotlinapp.business.ProductGroupService
import com.epam.kotlinapp.business.ProductService
import com.epam.kotlinapp.business.UserService
import com.epam.kotlinapp.controllers.productController
import com.epam.kotlinapp.controllers.productGroupController
import com.epam.kotlinapp.controllers.userController
import de.nielsfalk.ktor.swagger.SwaggerSupport
import de.nielsfalk.ktor.swagger.version.shared.Contact
import de.nielsfalk.ktor.swagger.version.shared.Information
import de.nielsfalk.ktor.swagger.version.v2.Swagger
import de.nielsfalk.ktor.swagger.version.v3.OpenApi
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


data class Model<T>(val elements: MutableList<T>)

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(Locations)
        install(SwaggerSupport) {
            forwardRoot = true
            val information = Information(
                version = "0.1",
                title = "sample api implemented in ktor",
                description = "This is a sample which combines [ktor](https://github.com/Kotlin/ktor) with [swaggerUi](https://swagger.io/). You find the sources on [github](https://github.com/nielsfalk/ktor-swagger)",
                contact = Contact(
                    name = "Niels Falk",
                    url = "https://nielsfalk.de"
                )
            )
            swagger = Swagger().apply {
                info = information
            }
            openApi = OpenApi().apply {
                info = information
            }
        }
        routing {
            this.userController(UserService)
            this.productController(ProductService)
            this.productGroupController(ProductGroupService)
        }
    }.start(wait = true)
}