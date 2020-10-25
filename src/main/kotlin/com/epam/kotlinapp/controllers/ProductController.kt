package com.epam.kotlinapp.controllers

import com.epam.kotlinapp.business.ICommonServices
import com.epam.kotlinapp.model.Product
import com.epam.kotlinapp.model.User
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Exception

private const val path: String = "/product"

fun Route.productController(productService: ICommonServices<Product>) {

    route(path) {
        get("/all") {
            try {
                call.respond(productService.getAll())
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        get("/{id}")
        {
            try {
                val id: Long = call.parameters["id"]!!.toLong()
                call.respond(productService.getEntity(id))
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        post {
            try {
                val product: Product = call.receive<Product>()
                productService.create(product)
                call.respond("Product successfully created")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        delete {
            try {
                val product: Product = call.receive<Product>()
                productService.delete(product)
                call.respond("Product successfully removed")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        delete("/{id}") {
            try {
                val id: Long = call.parameters["id"]!!.toLong()
                productService.delete(id)
                call.respond("Product successfully removed")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        put {
            try {
                val product: Product = call.receive<Product>()
                productService.update(product)
                call.respond("Product successfully edited")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
    }
}