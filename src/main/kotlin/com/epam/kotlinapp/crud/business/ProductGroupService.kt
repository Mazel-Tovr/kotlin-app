package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.dao.ICommonOperations
import com.epam.kotlinapp.crud.dao.ProductGroupOperations
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.ProductGroupNotFoundException
import com.epam.kotlinapp.crud.model.ProductGroup
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException

object ProductGroupService : ICommonServices<ProductGroup> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val productGroupOperations: ICommonOperations<ProductGroup> = ProductGroupOperations

    override fun create(entity: ProductGroup): ProductGroup {
        var create: ProductGroup? = null
        try {
            create = productGroupOperations.create(entity)
            logger.info("ProductGroup group added")
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return create ?: throw DataException("ProductGroup couldn't be created")
    }

    override fun getEntity(id: Long): ProductGroup {
        var productGroup: ProductGroup? = null
        try {
            productGroup = productGroupOperations.getEntity(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return productGroup ?: throw ProductGroupNotFoundException("ProductGroup with id = $id couldn't found")

    }

    override fun getAll(): ImmutableList<ProductGroup> {
        try {
            return productGroupOperations.getAll()
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return persistentListOf()
    }

    override fun update(entity: ProductGroup) {
        try {
            if (entity.id == null || entity.id == 0L)
                throw DataException("Product group id can't be empty")
            else productGroupOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }


    override fun delete(id: Long) {
        try {
            if (id == 0L)
                throw DataException("Product group id can't be empty")
            else productGroupOperations.delete(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }
}