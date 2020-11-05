package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.UserNotFoundException
import com.epam.kotlinapp.crud.model.User
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
class user(val id: Long)

@Group("User operations")
@Location(path)
class users

@Group("User operations")
@Location(path.plus("/all"))
class userGeneric


fun Route.userController(userService: ICommonServices<User>) {

    get<userGeneric>("all".responds(ok<Model<User>>())) {

        call.respond(HttpStatusCode.OK, userService.getAll())

    }
    get<user>(
        "find".responds(
            ok<User>(),
            notFound()
        )
    ) {
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            val entity = userService.getEntity(id)
            if (entity != null)
                call.respond(HttpStatusCode.OK, entity)
            else
                call.respond(HttpStatusCode.BadRequest,"")
        } catch (ex: UserNotFoundException) {
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
            val user = userService.create(entity);
            if (user!= null)
                call.respond(HttpStatusCode.Created, user)
            else
                call.respond(HttpStatusCode.BadRequest, "")
        } catch (ex: DataException) {
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
            val id: Long = call.parameters["id"]!!.toLong()
            userService.delete(id)
            call.respond(HttpStatusCode.OK, "User successfully removed")
        } catch (ex: DataException) {
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
            //val user: User = call.receive<User>()
            userService.update(user)
            call.respond(HttpStatusCode.OK, user)
        } catch (ex: DataException) {
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }
}
