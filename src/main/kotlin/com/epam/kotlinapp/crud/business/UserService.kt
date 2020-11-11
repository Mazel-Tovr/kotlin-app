package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.dao.ICommonOperations
import com.epam.kotlinapp.crud.dao.UserOperations
import com.epam.kotlinapp.crud.exceptions.DataException
import com.epam.kotlinapp.crud.exceptions.UserNotFoundException
import com.epam.kotlinapp.crud.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException

object UserService : ICommonServices<User> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val userOperations: ICommonOperations<User> = UserOperations

    override fun create(entity: User): User {
        var user: User? = null
        try {
            user = userOperations.create(entity)
            logger.info("User added")
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return user ?: throw DataException("User couldn't be created")
    }

    override fun getEntity(id: Long): User {
        var user: User? = null
        try {
            user = userOperations.getEntity(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return user ?: throw UserNotFoundException("User with id = $id couldn't found")
    }

    override fun getAll(): ImmutableList<User> {
        try {
            return userOperations.getAll()
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return persistentListOf()
    }

    override fun update(entity: User) {
        try {
            if (entity.id == null || entity.id == 0L)
                throw DataException("User id can't be empty")
            else userOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }


    override fun delete(id: Long) {
        try {
            if (id == 0L)
                throw DataException("User id can't be empty")
            else userOperations.delete(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }


}