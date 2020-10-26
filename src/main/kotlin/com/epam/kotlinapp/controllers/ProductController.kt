package com.epam.kotlinapp.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.business.ICommonServices
import com.epam.kotlinapp.model.Product
import com.epam.kotlinapp.model.User
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Group
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.post
import java.lang.Exception

private const val path: String = "/product"

private val productExample = mapOf(
    "id" to 0,
    "productName" to "Itelephone",
    "price" to 1337,
    "description" to "Super duper product",
    "groupID" to 1,
    "userId" to 1
)

@Group("Product operations")
@Location(path.plus("/{id}"))
private class product(val id: Long)

@Group("Product operations")
@Location(path)
private class products

@Group("Generic")
@Location(path.plus("/all"))
private class productGeneric


fun Route.productController(productService: ICommonServices<Product>) {

    get<productGeneric>("all".responds(ok<Model<Product>>())) {
        try {
            call.respond(productService.getAll())
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    get<product>(
        "find".responds(
            ok<Product>(),
            notFound()
        )
    )
    {
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            call.respond(productService.getEntity(id))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    post<products, Product>(
        "create"
            .description("Add new product to date base\nId independence")
            .examples(
                example("SuperDuperProduct", productExample, summary = "SuperDuperProduct")
            )
            .responds(
                created<User>(
                    example("SuperDuperProduct", productExample)
                )
            )
    ) { _, entity: Product ->
        try {
            call.respond(HttpStatusCode.Created, productService.create(entity))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    //TODO It should work , but it doesn't , idk why
    delete<products>(
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
        try {
            val product: Product = call.receive<Product>()
            productService.delete(product)
            call.respond("Product successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    delete<product>(
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
        try {
            val id: Long = call.parameters["id"]!!.toLong()
            productService.delete(id)
            call.respond("Product successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
    put<products, Product>(
        "update".description("Update product in db (by his id)")
            .examples(
                example("SuperDuperProduct", productExample, summary = "SuperDuperProduct")
            )
            .responds(
                ok<Unit>(),
                notFound()
            )
    ) { _, product: Product ->
        try {
            productService.update(product)
            call.respond(product)
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(it1) }
        }
    }
}