package com.epam.kotlinapp.model

import com.papsign.ktor.openapigen.annotations.Response

@Response("Product")
data class Product(
    var id: Long?,
    var productName: String,
    var price: Int,
    var description: String,
    var groupID: Long,
    var userId: Long
) : Entity()