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


private val productExample = mapOf(
    "productName" to "Itelephone",
    "price" to 1337,
    "description" to "Super duper product",
    "groupId" to 1,
    "userId" to 1
)


@KtorExperimentalLocationsAPI
fun Route.productController(productService: ICommonServices<Product>, observer: IObserver) {

    get<ApiRoot.Products>("all".responds(ok<Model<Product>>())) {

        observer.onEvent(READ, "Getting all products")
        call.respond(HttpStatusCode.OK, productService.getAll())

    }
    get<ApiRoot.Products.CurrentProduct>(
        "find".responds(
            ok<Product>(),
            notFound()
        )
    )
    {

        val id: Long = call.parameters["id"].let { param ->
            if (param == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@get
            } else {
                param.toLong()
            }
        }
        kotlin.runCatching { productService.getEntity(id) }
            .onSuccess { product ->
                observer.onEvent(READ, "Getting product with id = $id")
                call.respond(HttpStatusCode.OK, product)
            }
            .onFailure { exception ->
                when (exception) {
                    is ProductNotFoundException -> {
                        observer.onEvent(READ, "Trying to get product by id but ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "Trying to get product by id but ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }

    post<ApiRoot.Products, Product>(
        "create"
            .description("Add new product to date base\nId independence")
            .examples(
                example("SuperDuperProduct", productExample, summary = "SuperDuperProduct")
            )
            .responds(
                created<Product>(
                    example("SuperDuperProduct", productExample)
                )
            )
    ) { _, entity: Product ->
        kotlin.runCatching { productService.create(entity) }
            .onSuccess { product ->
                observer.onEvent(CREATE, "Creating new product $product")
                call.respond(HttpStatusCode.Created, product)
            }
            .onFailure { exception ->
                when (exception) {
                    is DataException -> {
                        observer.onEvent(CREATE, "Product wasn't created ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "Product wasn't created ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }
    delete<ApiRoot.Products.CurrentProduct>(
        "delete"
            .description("Delete product from db")
            .examples(
                example("SuperDuperProduct", productExample, summary = "SuperDuperProduct")
            )
            .responds(
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
        kotlin.runCatching { productService.delete(id) }
            .onSuccess {
                observer.onEvent(DELETE, "Product was deleted")
                call.respond(HttpStatusCode.OK, "Product successfully removed")
            }
            .onFailure { exception ->
                when (exception) {
                    is DataException -> {
                        observer.onEvent(DELETE, "Product wasn't deleted ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "Product wasn't deleted ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }
    put<ApiRoot.Products.CurrentProduct, Product>(
        "update".description("Update product in db (by his id)")
            .examples(
                example("SuperDuperProduct", productExample, summary = "SuperDuperProduct")
            )
            .responds(
                ok<Unit>(),
                notFound()
            )
    ) { _, product: Product ->
        product.id = call.parameters["id"].let { param ->
            if (param == null) {
                call.respond(HttpStatusCode.BadRequest, "Id isn't present")
                return@put
            } else {
                param.toLong()
            }
        }
        kotlin.runCatching { productService.update(product) }
            .onSuccess {
                observer.onEvent(UPDATE, "Product was edited")
                call.respond(HttpStatusCode.OK, product)
            }
            .onFailure { exception ->
                when (exception) {
                    is DataException -> {
                        observer.onEvent(UPDATE, "Product wasn't edited ${exception.message}")
                        call.respond(HttpStatusCode.BadRequest, "Product wasn't edited ${exception.message}")
                    }
                    else -> {
                        observer.onEvent(DELETE, "Server exception ${exception.message}")
                        call.respond(HttpStatusCode.InternalServerError, "Server exception ${exception.message}")
                    }
                }
            }
    }
}
