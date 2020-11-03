package com.epam.kotlinapp.crud.business

import com.epam.kotlinapp.crud.dao.ICommonOperations
import com.epam.kotlinapp.crud.dao.UserOperations
import com.epam.kotlinapp.crud.exceptions.UserNotFoundException
import com.epam.kotlinapp.crud.model.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException

object UserService : ICommonServices<User> {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val userOperations: ICommonOperations<User> = UserOperations

    override fun create(entity: User): User? {
        return try {
            val user = userOperations.create(entity);
            logger.info("User added")
            user
        } catch (ex: SQLException) {
            logger.error(ex.message)
            null;
        }
    }

    override fun getEntity(id: Long): User {
        var user: User? = null
        try {
            user = userOperations.getEntity(id)
            return user ?: throw UserNotFoundException("User with id = $id couldn't found")

        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        user.let { return it!! }
    }

    override fun getAll(): List<User> {
        var list: List<User> = emptyList();
        try {
            list = userOperations.getAll();
            return list
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
        return list
    }

    override fun update(entity: User) {
        try {
            userOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    override fun delete(entity: User) {
        try {
            userOperations.update(entity)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    override fun delete(id: Long) {
        try {
            userOperations.delete(id)
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }


}