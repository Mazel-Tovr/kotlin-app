package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.UserNotFoundException
import com.epam.kotlinapp.crud.listener.Event.*
import com.epam.kotlinapp.crud.listener.IObserver
import com.epam.kotlinapp.crud.model.User
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Group
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*


private val userExample = mapOf(
    "id" to 0,
    "name" to "sanya",
    "email" to "pro100sanya@gmail.com",
    "password" to "123"
)

private const val path: String = "/user"

@KtorExperimentalLocationsAPI
@Group("User operations")
@Location(path.plus("/{id}"))
class user(val id: Long)

@KtorExperimentalLocationsAPI
@Group("User operations")
@Location(path)
class users

@KtorExperimentalLocationsAPI
@Group("User operations")
@Location(path.plus("/all"))
class userGeneric


@KtorExperimentalLocationsAPI
fun Route.userController(userService: ICommonServices<User>, observer: IObserver) {

    get<userGeneric>("all".responds(ok<Model<User>>())) {

        observer.onEvent(READ, "Getting all users")
        call.respond(HttpStatusCode.OK, userService.getAll())

    }
    get<user>(
        "find".responds(
            ok<User>(),
            notFound()
        )
    ) {
        try {
            val paramId = call.parameters["id"]
            if (paramId == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@get
            }
            val id: Long = paramId.toLong()
            val entity = userService.getEntity(id)

            observer.onEvent(READ, "Getting user with id = $id")
            call.respond(HttpStatusCode.OK, entity)

        } catch (ex: UserNotFoundException) {
            observer.onEvent(READ, "Trying to get user by id but ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
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
            val user = userService.create(entity)

            observer.onEvent(CREATE, "Creating new user $user")
            call.respond(HttpStatusCode.Created, user)

        } catch (ex: DataException) {
            observer.onEvent(CREATE, "User wasn't created ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }

    delete<user>(
        "delete".responds(
            ok<Unit>(),
            notFound()
        )
    ) {
        try {
            val paramId = call.parameters["id"]
            if (paramId == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@delete
            }
            val id: Long = paramId.toLong()
            userService.delete(id)
            observer.onEvent(DELETE, "User was deleted")
            call.respond(HttpStatusCode.OK, "User successfully removed")
        } catch (ex: DataException) {
            observer.onEvent(DELETE, "User was not deleted ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
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
            observer.onEvent(UPDATE, "User was edited")
            userService.update(user)
            call.respond(HttpStatusCode.OK, user)
        } catch (ex: DataException) {
            observer.onEvent(UPDATE, "User wasn't edited ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }
}
