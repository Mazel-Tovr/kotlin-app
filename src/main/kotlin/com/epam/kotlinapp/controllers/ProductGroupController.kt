package com.epam.kotlinapp.controllers

import com.epam.kotlinapp.business.ICommonServices
import com.epam.kotlinapp.model.ProductGroup
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.Exception

private const val path: String = "/productgroup"

fun Route.productGroupController(productGroupService: ICommonServices<ProductGroup>) {

    route(path) {
        get("/all") {
            try {
                call.respond(productGroupService.getAll())
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        get("/{id}")
        {
            try {
                val id: Long = call.parameters["id"]!!.toLong()
                call.respond(productGroupService.getEntity(id))
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        post {
            try {
                val productGroup: ProductGroup = call.receive<ProductGroup>()
                productGroupService.create(productGroup)
                call.respond("Product group successfully created")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        delete {
            try {
                val productGroup: ProductGroup = call.receive<ProductGroup>()
                productGroupService.delete(productGroup)
                call.respond("Product group successfully removed")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        delete("/{id}") {
            try {
                val id: Long = call.parameters["id"]!!.toLong()
                productGroupService.delete(id)
                call.respond("Product group successfully removed")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
        put {
            try {
                val product: ProductGroup = call.receive<ProductGroup>()
                productGroupService.update(product)
                call.respond("Product group successfully edited")
            } catch (ex: Exception) {
                ex.message?.let { it1 -> call.respond(it1) }
            }
        }
    }
}