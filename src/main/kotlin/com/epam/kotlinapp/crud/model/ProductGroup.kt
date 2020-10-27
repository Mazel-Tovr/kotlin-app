package com.epam.kotlinapp.crud.model


data class ProductGroup(
    var id: Long?,
    var groupName: String
) : Entity()