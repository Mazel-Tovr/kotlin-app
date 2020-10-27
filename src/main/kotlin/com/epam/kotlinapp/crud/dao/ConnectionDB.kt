package com.epam.kotlinapp.crud.dao


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

internal object ConnectionDB {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val username = "sa"
    private val password = ""
    lateinit var conn: Connection
    private val path: String = "jdbc:h2:".plus(System.getProperty("user.dir")).plus("\\db\\db")


    init {
        try {
            logger.info("Connecting to Database")
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(path, username, password)
            logger.info("Connection is established")
        } catch (ex: SQLException) {
            logger.error(ex.message)
//            ex.printStackTrace()
        } catch (ex: Exception) {
            logger.error(ex.message)
//            ex.printStackTrace()
        }
    }

}