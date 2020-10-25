package com.epam.kotlinapp.controllers

import com.epam.kotlinapp.business.ICommonServices
import com.epam.kotlinapp.model.User
import com.papsign.ktor.openapigen.annotations.Path
import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import io.ktor.application.*
import io.ktor.http.content.*

import io.ktor.request.*
import io.ktor.response.*
import com.papsign.ktor.openapigen.route.*
import io.ktor.routing.*
import java.lang.Exception
import kotlin.text.get

//Test input
//{
//    "id":"0",
//    "name":"sanya",
//    "email":"pro100sanya@gmail.com",
//    "password":"123"
// }

private const val path: String = "/user"

fun Route.userController(userService: ICommonServices<User>) {

    route(path) {

        get("/all") {
            try {
                call.respond(userService.getAll())
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        get("/{id}")
        {
            try {
                val id: Long = call.parameters["id"]!!.toLong()
                call.respond(userService.getEntity(id))
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        post {
            try {
                val user: User = call.receive<User>()
                userService.create(user)
                call.respond("User successfully created")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        delete {
            try {
                val user: User = call.receive<User>()
                userService.delete(user)
                call.respond("User successfully removed")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        delete("/{id}") {
            try {
                val id: Long = call.parameters["id"]!!.toLong()
                userService.delete(id)
                call.respond("User successfully removed")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        put {
            try {
                val user: User = call.receive<User>()
                userService.update(user)
                call.respond("User successfully edited")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
    }


}
