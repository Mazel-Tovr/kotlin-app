package com.epam.kotlinapp.crud.controllers.roots

import de.nielsfalk.ktor.swagger.version.shared.*
import io.ktor.locations.*


@KtorExperimentalLocationsAPI
class ApiRoot {

    companion object {
        //Roots
        private const val productsPath: String = "/products"
        private const val productGroupsPath: String = "/productgroups"
        private const val usersPath: String = "/users"

        //Group descriptions
        private const val productGroupGroupDesc = "Product operations"
        private const val productGroupDesc = "Product group operations"
        private const val userGroupDesc = "User operations"
    }

    @Group(productGroupGroupDesc)
    @Location(productsPath)
    class Products {

        @Group(productGroupGroupDesc)
        @Location("/{id}")
        data class CurrentProduct(val id: Long, val products: Products)
    }

    @Group(productGroupDesc)
    @Location(productGroupsPath)
    class ProductGroups {

        @Group(productGroupDesc)
        @Location("/{id}")
        class CurrentProductGroup(val id: Long, val productGroups: ProductGroups)
    }


    @Group(userGroupDesc)
    @Location(usersPath)
    class Users {

        @Group(userGroupDesc)
        @Location("/{id}")
        class CurrentUser(val id: Long, val users: Users)
    }
}

