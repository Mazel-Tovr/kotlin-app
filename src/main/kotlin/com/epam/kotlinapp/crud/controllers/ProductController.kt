package com.epam.kotlinapp.crud.controllers

import com.epam.kotlinapp.Model
import com.epam.kotlinapp.crud.business.ICommonServices
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.ProductGroupNotFoundException
import com.epam.kotlinapp.crud.model.Product
import de.nielsfalk.ktor.swagger.*
import de.nielsfalk.ktor.swagger.version.shared.Group
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
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

        call.respond(HttpStatusCode.OK, productService.getAll())

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
            val entity = productService.getEntity(id)
            if (entity != null)
                call.respond(HttpStatusCode.OK, entity)
            else
                call.respond(HttpStatusCode.BadRequest,"")
        } catch (ex: ProductGroupNotFoundException) {
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
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
            val product = productService.create(entity);
            if (product!= null)
                call.respond(HttpStatusCode.Created, product)
            else
                call.respond(HttpStatusCode.BadRequest, "")
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
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
            call.respond(HttpStatusCode.OK, "Product successfully removed")
        } catch (ex: DataException) {
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
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
            call.respond(HttpStatusCode.OK, product)
        } catch (ex: DataException) {
            call.respond(HttpStatusCode.BadRequest, ex.message ?: "")
        }
    }
}