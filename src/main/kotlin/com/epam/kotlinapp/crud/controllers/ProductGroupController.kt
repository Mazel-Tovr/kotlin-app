package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.model.ProductGroup
import com.epam.kotlinapp.crud.model.User
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Group
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Exception

private const val path: String = "/productgroup"

private val productGroupExample = mapOf(
    "id" to 0,
    "groupName" to "some group",
)


@Group("Product group operations")
@Location(path.plus("/{id}"))
class productGroup(val id: Long)

@Group("Product group operations")
@Location(path)
class productGroups

@Group("Product group operations")
@Location(path.plus("/all"))
class productGroupGeneric


fun Route.productGroupController(productGroupService: ICommonServices<ProductGroup>) {


    get<productGroupGeneric>("all".responds(ok<Model<ProductGroup>>())) {
        try {
            call.respond(productGroupService.getAll())
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    get<productGroup>(
        "find".responds(
            ok<ProductGroup>(),
            notFound()
        )
    )
    {
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            call.respond(productGroupService.getEntity(id))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    post<productGroups, ProductGroup>(
        "create"
            .description("Add new user to date base")
            .examples(
                example("Group", productGroupExample, summary = "super group")
            )
            .responds(
                created<User>(
                    example("Group", productGroupExample)
                )
            )
    ) { _, entity: ProductGroup ->
        try {
            call.respond(HttpStatusCode.Created, productGroupService.create(entity))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    //TODO It should work , but it doesn't , idk why
    delete<productGroups>(
        "delete"
            .description("Delete product group from db")
            .examples(
                example("Group", productGroupExample, summary = "super group")
            )
            .responds(
                ok<Unit>(),
                notFound()
            )
    ) {
        try {
            val productGroup: ProductGroup = call.receive<ProductGroup>()
            productGroupService.delete(productGroup)
            call.respond("Product group successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    delete<productGroup>(
        "delete".responds(
            ok<Unit>(),
            notFound()
        )
    ) {
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            productGroupService.delete(id)
            call.respond("Product group successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    put<productGroups,ProductGroup>( "update"
        .description("Update produt group in db (by his id)")
        .examples(
            example("Group", productGroupExample, summary = "super group")
        ).responds(
            ok<ProductGroup>(),
            notFound()
        )) { _, productGroup: ProductGroup ->
        try {
            productGroupService.update(productGroup)
            call.respond(productGroup)
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }

}