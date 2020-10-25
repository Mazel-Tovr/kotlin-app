package com.epam.kotlinapp

import com.epam.kotlinapp.business.ProductGroupService
import com.epam.kotlinapp.business.ProductService
import com.epam.kotlinapp.business.UserService
import com.epam.kotlinapp.controllers.productController
import com.epam.kotlinapp.controllers.productGroupController
import com.epam.kotlinapp.controllers.userController
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*



fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {



        routing {

            get("/") {
                call.respondRedirect("/swagger-ui/index.html?url=/openapi.json", true)
            }
            this.userController(UserService)
            this.productController(ProductService)
            this.productGroupController(ProductGroupService)

        }
    }.start(wait = true)
}