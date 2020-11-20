package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.*
import com.epam.kotlinapp.crud.business.*
import com.epam.kotlinapp.crud.controllers.roots.*
import com.epam.kotlinapp.crud.exceptions.*
import com.epam.kotlinapp.crud.listener.*
import com.epam.kotlinapp.crud.listener.Event.*
import com.epam.kotlinapp.crud.model.*
import de.nielsfalk.ktor.swagger.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*


private val userExample = mapOf(
    "name" to "sanya",
    "email" to "pro100sanya@gmail.com",
    "password" to "123"
)


@KtorExperimentalLocationsAPI
fun Route.userController(userService: ICommonServices<User>, observer: IObserver) {

    get<ApiRoot.Users>("all".responds(ok<Model<User>>())) {

        observer.onEvent(READ, "Getting all users")
        call.respond(HttpStatusCode.OK, userService.getAll())

    }
    get<ApiRoot.Users.CurrentUser>(
        "find".responds(
            ok<User>(),
            notFound()
        )
    ) {

        val id: Long = call.parameters["id"].let { param ->
            if (param == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@get
            } else {
                param.toLong()
            }
        }

        kotlin.runCatching { userService.getEntity(id) }
            .onSuccess { user ->
                observer.onEvent(READ, "Getting user with id = $id")
                call.respond(HttpStatusCode.OK, user)
            }
            .onFailure { exception ->
                when (exception) {
                    is UserNotFoundException -> {
                        observer.onEvent(READ, "Trying to get user by id but ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "Trying to get user by id but ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(READ, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }

    post<ApiRoot.Users, User>(
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

        kotlin.runCatching { userService.create(entity) }
            .onSuccess { user ->
                observer.onEvent(CREATE, "Creating new user $user")
                call.respond(HttpStatusCode.Created, user)
            }
            .onFailure { exception ->
                when (exception) {
                    is DataException -> {
                        observer.onEvent(CREATE, "User wasn't created ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "User wasn't created ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }

    delete<ApiRoot.Users.CurrentUser>(
        "delete".responds(
            ok<Unit>(),
            notFound()
        )
    ) {

        val id: Long = call.parameters["id"].let { param ->
            if (param == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@delete
            } else {
                param.toLong()
            }
        }
        kotlin.runCatching { userService.delete(id) }
            .onSuccess {
                observer.onEvent(DELETE, "User was deleted")
                call.respond(HttpStatusCode.OK, "User successfully removed")
            }
            .onFailure { exception ->
                when (exception) {
                    is DataException -> {
                        observer.onEvent(DELETE, "User wasn't deleted ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "User wasn't deleted ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }

            }
    }

    put<ApiRoot.Users.CurrentUser, User>(
        "update"
            .description("Update user in db (by his id)")
            .examples(
                example("Sanya", userExample, summary = "Just Sashka")
            ).responds(
                ok<User>(),
                notFound()
            )
    ) { _, user: User ->

        user.id = call.parameters["id"].let { param ->
            if (param == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@put
            } else {
                param.toLong()
            }
        }
        kotlin.runCatching { userService.update(user) }
            .onSuccess {
                observer.onEvent(UPDATE, "User was edited")
                call.respond(HttpStatusCode.OK, user)
            }
            .onFailure { exception ->
                when (exception) {
                    is DataException -> {
                        observer.onEvent(UPDATE, "User wasn't edited ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "User wasn't edited ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }
}

