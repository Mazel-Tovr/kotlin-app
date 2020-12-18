package com.epam.kotlinapp

import com.epam.kotlinapp.chat.server.*
import com.epam.kotlinapp.crud.business.*
import com.epam.kotlinapp.crud.dao.nosql.*
import com.epam.kotlinapp.crud.endpoints.*
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.*
import de.nielsfalk.ktor.swagger.version.v2.*
import de.nielsfalk.ktor.swagger.version.v3.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*


data class Model<T>(val elements: MutableList<T>)


@KtorExperimentalLocationsAPI
fun main() {
    val server = Server()
    embeddedServer(Netty, port = 8088, host = "127.0.0.1") {

        install(DefaultHeaders)
        install(CallLogging)
        install(ContentNegotiation) {
            json()
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

        install(WebSockets)

        routing {

            this.userController(UserService(UserOperationImpl()),server)
            this.productController(ProductService(ProductOperationImpl),server)
            this.productGroupController(ProductGroupService(ProductGroupOperationImp),server)
            this.webSocket(server)
        }
    }.start(wait = true)
}

