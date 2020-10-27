package com.epam.kotlinapp

import com.epam.kotlinapp.chat.server.Server
import com.epam.kotlinapp.chat.server.User
import com.epam.kotlinapp.crud.business.ProductGroupService
import com.epam.kotlinapp.crud.business.ProductService
import com.epam.kotlinapp.crud.business.UserService
import com.epam.kotlinapp.crud.controllers.productController
import com.epam.kotlinapp.crud.controllers.productGroupController
import com.epam.kotlinapp.crud.controllers.userController
import de.nielsfalk.ktor.swagger.SwaggerSupport
import de.nielsfalk.ktor.swagger.version.shared.Contact
import de.nielsfalk.ktor.swagger.version.shared.Information
import de.nielsfalk.ktor.swagger.version.v2.Swagger
import de.nielsfalk.ktor.swagger.version.v3.OpenApi
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.cio.websocket.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.time.Duration


data class Model<T>(val elements: MutableList<T>)


fun main() {
    val server = Server()
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

        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
        }


        routing {
            this.userController(UserService)
            this.productController(ProductService)
            this.productGroupController(ProductGroupService)

            webSocket("/ws") {

                val input = incoming.receive()

                val userName = if (input is Frame.Text) input.readText() else ""

                if (userName != "" && server.isNickNameFree(userName)) {
                    val user = User(userName)
                    server.joinToServer(user, this)
                    try {
                        while (true) {
                            when (val frame = incoming.receive()) {
                                is Frame.Text -> {
                                    server.sendMessage(user, frame.readText())
                                }
                            }
                        }

                    } finally {
                        server.userLeftServer(user, this)
                    }
                } else {
                    this.send(Frame.Text("This nick name is already taken try again"))
                }
            }

        }
    }.start(wait = true)
}