package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.model.Product
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
class product(val id: Long)

@Group("Product operations")
@Location(path)
class products

@Group("Product operations")
@Location(path.plus("/all"))
class productGeneric


fun Route.productController(productService: ICommonServices<Product>) {

    get<productGeneric>("all".responds(ok<Model<Product>>())) {
        try {
            call.respond(HttpStatusCode.OK,productService.getAll())
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(HttpStatusCode.NotFound,it1) }
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
            call.respond(HttpStatusCode.OK,productService.getEntity(id))
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(HttpStatusCode.NotFound,it1) }
        }
    }
    post<products, Product>(
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
        try {
            productService.create(entity)?.let { call.respond(HttpStatusCode.Created, it) }
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(HttpStatusCode.ExpectationFailed,it1) }
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
            call.respond(HttpStatusCode.OK,"Product successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(HttpStatusCode.ExpectationFailed,it1) }
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
            call.respond(HttpStatusCode.OK,"Product successfully removed")
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(HttpStatusCode.ExpectationFailed,it1) }
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
            call.respond(HttpStatusCode.OK,product)
        } catch (ex: Exception) {
            ex.message?.let { it1 -> call.respond(HttpStatusCode.ExpectationFailed,it1) }
        }
    }
}