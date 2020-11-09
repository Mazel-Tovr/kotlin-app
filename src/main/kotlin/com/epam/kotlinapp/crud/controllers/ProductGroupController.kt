package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.ProductNotFoundException
import com.epam.kotlinapp.crud.listener.Event.*
import com.epam.kotlinapp.crud.listener.IObserver
import com.epam.kotlinapp.crud.model.ProductGroup
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Group
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

private const val path: String = "/productgroup"

private val productGroupExample = mapOf(
    "id" to 0,
    "groupName" to "some group",
)


@KtorExperimentalLocationsAPI
@Group("Product group operations")
@Location(path.plus("/{id}"))
class productGroup(val id: Long)

@KtorExperimentalLocationsAPI
@Group("Product group operations")
@Location(path)
class productGroups

@KtorExperimentalLocationsAPI
@Group("Product group operations")
@Location(path.plus("/all"))
class productGroupGeneric


@KtorExperimentalLocationsAPI
fun Route.productGroupController(productGroupService: ICommonServices<ProductGroup>, observer: IObserver) {


    get<productGroupGeneric>("all".responds(ok<Model<ProductGroup>>())) {
        observer.onEvent(READ, "Getting all product groups")
        call.respond(HttpStatusCode.OK, productGroupService.getAll())
    }

    get<productGroup>(
        "find".responds(
            ok<ProductGroup>(),
            notFound()
        )
    )
    {
        try {
            val paramId = call.parameters["id"]
            if (paramId == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@get
            }
            val id: Long = paramId.toLong()

            val entity = productGroupService.getEntity(id)

            observer.onEvent(READ, "Getting product group with id = $id")
            call.respond(HttpStatusCode.OK, entity)

        } catch (ex: ProductNotFoundException) {
            observer.onEvent(READ, "Trying to get product group by id but ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }
    post<productGroups, ProductGroup>(
        "create"
            .description("Add new user to date base")
            .examples(
                example("Group", productGroupExample, summary = "super group")
            )
            .responds(
                created<ProductGroup>(
                    example("Group", productGroupExample)
                )
            )
    ) { _, entity: ProductGroup ->

        val productGroup = productGroupService.create(entity)
        try {

            observer.onEvent(CREATE, "Creating new product group $productGroup")
            call.respond(HttpStatusCode.Created, productGroup)

        } catch (ex: DataException) {
            observer.onEvent(CREATE, "Product was not created ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }

    delete<productGroup>(
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
            productGroupService.delete(id)
            observer.onEvent(DELETE, "Product was deleted")
            call.respond(HttpStatusCode.OK, "Product group successfully removed")
        } catch (ex: DataException) {
            observer.onEvent(DELETE, "Product was not deleted ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }
    put<productGroups, ProductGroup>(
        "update"
            .description("Update product group in db (by his id)")
            .examples(
                example("Group", productGroupExample, summary = "super group")
            ).responds(
                ok<ProductGroup>(),
                notFound()
            )
    ) { _, productGroup: ProductGroup ->
        try {
            productGroupService.update(productGroup)
            observer.onEvent(UPDATE, "Product group  was edited")
            call.respond(HttpStatusCode.OK, productGroup)
        } catch (ex: DataException) {
            observer.onEvent(UPDATE, "Product group  wasn't edited ${ex.message}")
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }

}