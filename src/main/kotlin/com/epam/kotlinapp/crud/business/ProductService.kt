package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.dao.*
import com.epam.kotlinapp.crud.dao.nosql.*
import com.epam.kotlinapp.crud.exceptions.*
import com.epam.kotlinapp.crud.model.*
import kotlinx.collections.immutable.*
import org.slf4j.*

object ProductService : ICommonServices<Product> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val productOperations: ICommonOperations<Product> = ProductOperationImpl

    override fun create(entity: Product): Product {
        return kotlin.runCatching { productOperations.create(entity) }
            .onSuccess { logger.info("Product added") }
            .onFailure { logger.error(it.message) }
            .getOrElse { throw DataException("Product couldn't be created") }
    }

    override fun getEntity(id: Long): Product {
        return kotlin.runCatching { productOperations.getEntity(id) }
            .onSuccess { logger.info("Getting product") }
            .onFailure { logger.error(it.message) }
            .getOrNull() ?: throw ProductNotFoundException("Product with id = $id couldn't found")

    }

    override fun getAll(): ImmutableList<Product> {
        return kotlin.runCatching { productOperations.getAll() }
            .onSuccess { logger.info("Getting products") }
            .onFailure { logger.error(it.message) }
            .getOrDefault(persistentListOf())
    }

    override fun update(entity: Product) {
        if (entity.id == -1L)
            throw DataException("Product id can't be empty")
        kotlin.runCatching { productOperations.update(entity) }
            .onSuccess { logger.info("Product updated") }
            .onFailure {
                logger.error(it.message)
                throw DataException("Product wasn't updated")
            }
    }

    override fun delete(id: Long) {
        kotlin.runCatching { productOperations.delete(id) }
            .onSuccess { logger.info("Product deleted") }
            .onFailure {
                logger.error(it.message)
                throw DataException("Product wasn't deleted")
            }
    }
}
