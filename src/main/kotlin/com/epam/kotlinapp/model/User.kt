package com.epam.kotlinapp.model

import com.papsign.ktor.openapigen.annotations.Response

@Response("User")
data class User(
    var id: Long?,
    var name: String,
    var email: String,
    var password: String
) : Entity()
