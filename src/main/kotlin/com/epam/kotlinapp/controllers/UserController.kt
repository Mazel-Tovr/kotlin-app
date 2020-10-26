package com.epam.kotlinapp.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.business.ICommonServices
import com.epam.kotlinapp.model.User
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Group
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


private val userExample = mapOf(
    "id" to 0,
    "name" to "sanya",
    "email" to "pro100sanya@gmail.com",
    "password" to "123"
)

private const val path: String = "/user"

@Group("User operations")
@Location(path.plus("/{id}"))
private class user(val id: Long)

@Group("User operations")
@Location(path)
private class users

@Group("Generic")
@Location(path.plus("/all"))
private class userGeneric



fun Route.userController(userService: ICommonServices<User>) {

    get<userGeneric>("all".responds(ok<Model<User>>())) {
        try {
            call.respond(userService.getAll())
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    get<user>(
        "find".responds(
            ok<User>(),
            notFound()
        )
    ) {
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            call.respond(userService.getEntity(id))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    post<users, User>(
        "create"
            .description("Add new user to date base")
            .examples(
                example("Sanya", userExample, summary = "Just Sashka")
            )
            .responds(
                created<User>(
                    example("Sanya", userExample)
                )
            )
    ) { _, entity: User ->
        try {
//            val user: User = call.receive<User>()
            call.respond(HttpStatusCode.Created, userService.create(entity))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    //TODO It should work , but it doesn't , idk why
    delete<users>(
        "delete"
            .description("Delete user from db")
            .examples(
                example("Sanya", userExample, summary = "Just Sashka")
            )
            .responds(
                ok<Unit>(),
                notFound()
            )
    ) {
        try {
            val user: User = call.receive<User>()
            userService.delete(user)
            call.respond("User successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    delete<user>(
        "delete".responds(
            ok<Unit>(),
            notFound()
        )
    ) {
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            userService.delete(id)
            call.respond("User successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    put<users, User>(
        "update"
            .description("Update user in db (by his id)")
            .examples(
                example("Sanya", userExample, summary = "Just Sashka")
            ).responds(
                ok<User>(),
                notFound()
            )
    ) { _, user: User ->
        try {
            //val user: User = call.receive<User>()
            userService.update(user)
            call.respond(user)
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
}
