package com.epam.kotlinapp.model

import com.papsign.ktor.openapigen.annotations.Response

@Response("ProductGroup")
data class ProductGroup(
    var id: Long?,
    var groupName: String
) : Entity()