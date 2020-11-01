package api.db

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object ConnectionToTestDB {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val username = "sa"
    private val password = ""
    lateinit var conn: Connection
    private val path: String = "jdbc:h2:".plus(System.getProperty("user.dir")).plus("\\src\\test\\resources\\test")


    init {
        try {
            logger.info("Connecting to test Database")
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(path, username, password)
            logger.info("Connection to test is established")
            logger.info("Executing init script")
            init(conn)

        } catch (ex: SQLException) {
            logger.error(ex.message)

        } catch (ex: Exception) {
            logger.error(ex.message)
        }
    }


    private fun init(conn:Connection) {
        conn.prepareStatement(
                "DROP TABLE IF EXISTS `product`;\n" +
                        "CREATE TABLE `product` (\n" +
                        "  `id` IDENTITY NOT NULL PRIMARY KEY,\n" +
                        "  `product_name` varchar(50) DEFAULT NULL,\n" +
                        "  `price` int(11) DEFAULT NULL,\n" +
                        "  `description` text,\n" +
                        "  `group_id` bigint(20) DEFAULT NULL,\n" +
                        "  `user_id` bigint(20) DEFAULT NULL\n" +
                        ");\n" +
                        "DROP TABLE IF EXISTS `product_group`;\n" +
                        "CREATE TABLE `product_group` (\n" +
                        "  `id` IDENTITY NOT NULL PRIMARY KEY,\n" +
                        "  `group_name` varchar(50) DEFAULT NULL\n" +
                        ");\n" +
                        "INSERT INTO PRODUCT_GROUP VALUES (1,'Super Duper Group');\n"+
                        "DROP TABLE IF EXISTS `user`;\n" +
                        "CREATE TABLE `user` (\n" +
                        "  `id` IDENTITY NOT NULL PRIMARY KEY,\n" +
                        "  `name` varchar(50) DEFAULT NULL,\n" +
                        "  `email` varchar(50) DEFAULT NULL,\n" +
                        "  `password` varchar(50) DEFAULT NULL\n" +
                        ");\n" +
                        "INSERT INTO `user` (`id`, `name`, `email`, `password`) VALUES\n" +
                        "(1, 'Roma', 'roma@mail.ru', '123');"+
                        "INSERT INTO PRODUCT VALUES (1,'Телефон','228','Nokia 330','1','1');\n"
        ).execute()
    }


}