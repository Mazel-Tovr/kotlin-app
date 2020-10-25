package com.epam.kotlinapp.business

import com.epam.kotlinapp.dao.ICommonOperations
import com.epam.kotlinapp.dao.ProductOperations
import com.epam.kotlinapp.exceptions.DataException
import com.epam.kotlinapp.exceptions.ProductNotFoundException
import com.epam.kotlinapp.model.Product
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException

object ProductService : ICommonServices<Product> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val productOperations: ICommonOperations<Product> = ProductOperations

    override fun create(entity: Product) {
        try {
            productOperations.create(entity)
            logger.info("User added")
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    override fun getEntity(id: Long): Product {
        var product: Product? = null;
        try {
            product = productOperations.getEntity(id);
            return product ?: throw ProductNotFoundException("Product with id = $id couldn't found")

        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        product.let { return it!! }
    }

    override fun getAll(): List<Product> {
        var list: List<Product> = emptyList();
        try {
            list = productOperations.getAll();
            return list.ifEmpty { throw ProductNotFoundException("Products couldn't be found") }
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return list
    }

    override fun update(entity: Product) {
        try {
            if (entity.id == null)
                throw DataException("Product id can't be empty")
            else productOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    override fun delete(entity: Product) {
        try {
            productOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    override fun delete(id: Long) {
        try {
            productOperations.delete(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }
}