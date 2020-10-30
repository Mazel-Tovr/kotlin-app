package com.epam.kotlinapp.crud.exceptions

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DataException(message:String):Exception(message) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.error(message)
    }
}