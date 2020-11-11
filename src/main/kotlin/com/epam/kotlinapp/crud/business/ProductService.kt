package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.dao.ICommonOperations
import com.epam.kotlinapp.crud.dao.ProductOperations
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.ProductNotFoundException
import com.epam.kotlinapp.crud.model.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException

object ProductService : ICommonServices<Product> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val productOperations: ICommonOperations<Product> = ProductOperations

    override fun create(entity: Product): Product {
        var create: Product? = null
        try {
            create = productOperations.create(entity)
            logger.info("Product added")
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return create ?: throw DataException("Product couldn't be created")
    }

    override fun getEntity(id: Long): Product {
        var product: Product? = null
        try {
            product = productOperations.getEntity(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return product ?: throw ProductNotFoundException("Product with id = $id couldn't found")
    }

    override fun getAll(): ImmutableList<Product> {
        try {
            return productOperations.getAll()
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return persistentListOf()
    }

    override fun update(entity: Product) {
        try {
            if (entity.id == null || entity.id == 0L)
                throw DataException("Product id can't be empty")
            else productOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    override fun delete(id: Long) {
        try {
            if (id == 0L)
                throw DataException("Product id can't be empty")
            else productOperations.delete(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }
}