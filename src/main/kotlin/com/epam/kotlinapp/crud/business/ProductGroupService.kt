package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.dao.*
import com.epam.kotlinapp.crud.dao.nosql.*
import com.epam.kotlinapp.crud.exceptions.*
import com.epam.kotlinapp.crud.model.*
import kotlinx.collections.immutable.*
import org.slf4j.*

class ProductGroupService(private val productGroupOperations: ICommonOperations<ProductGroup>) : ICommonServices<ProductGroup> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun create(entity: ProductGroup): ProductGroup {
        return kotlin.runCatching { productGroupOperations.create(entity) }
            .onSuccess { logger.info("Product group added") }
            .onFailure { logger.error(it.message) }
            .getOrElse { throw DataException("Product group couldn't be created") }
    }

    override fun getEntity(id: Long): ProductGroup {
        return kotlin.runCatching { productGroupOperations.getEntity(id) }
            .onSuccess { logger.info("Getting product group") }
            .onFailure { logger.error(it.message) }
            .getOrNull() ?: throw ProductGroupNotFoundException("Product group with id = $id couldn't found")

    }

    override fun getAll(): ImmutableList<ProductGroup> {
        return kotlin.runCatching { productGroupOperations.getAll() }
            .onSuccess { logger.info("Getting product groups") }
            .onFailure { logger.error(it.message) }
            .getOrDefault(persistentListOf())
    }

    override fun update(entity: ProductGroup) {
        if (entity.id == -1L)
            throw DataException("Product group id can't be empty")
        kotlin.runCatching { productGroupOperations.update(entity) }
            .onSuccess { logger.info("Product group updated") }
            .onFailure {
                logger.error(it.message)
                throw DataException("Product group wasn't updated")
            }

    }

    override fun delete(id: Long) {
        if (id == 0L)
            throw DataException("Product group id can't be empty")
        kotlin.runCatching { productGroupOperations.delete(id) }
            .onSuccess { logger.info("Product group deleted") }
            .onFailure {
                logger.error(it.message)
                throw DataException("Product group wasn't deleted")
            }
    }
}
