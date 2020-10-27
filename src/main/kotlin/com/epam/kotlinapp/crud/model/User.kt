package com.epam.kotlinapp.crud.model


data class User(
    var id: Long?,
    var name: String,
    var email: String,
    var password: String
) : Entity()
