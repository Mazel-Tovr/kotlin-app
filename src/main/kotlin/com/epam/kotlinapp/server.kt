package com.epam.kotlinapp

import com.epam.kotlinapp.business.ProductGroupService
import com.epam.kotlinapp.business.ProductService
import com.epam.kotlinapp.business.UserService
import com.epam.kotlinapp.controllers.productController
import com.epam.kotlinapp.controllers.productGroupController
import com.epam.kotlinapp.controllers.userController
import com.papsign.ktor.openapigen.OpenAPIGen
import com.papsign.ktor.openapigen.openAPIGen
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*



fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(OpenAPIGen) {
            // basic info
            info {
                version = "0.0.1"
                title = "Test API"
                description = "The Test API"
                contact {
                    name = "Support"
                    email = "support@test.com"
                }
            }
            // describe the server, add as many as you want
            server("http://localhost:8080/") {
                description = "Test server"
            }
        }

//        install(ContentNegotiation) {
//            jackson()
//        }
        routing {

            get("/openapi.json") {
                call.respond(application.openAPIGen.api.serialize())
            }
            get("/") {
                call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
            }
            this.userController(UserService)
            this.productController(ProductService)
            this.productGroupController(ProductGroupService)

        }
    }.start(wait = true)
}