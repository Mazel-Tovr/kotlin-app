import com.epam.kotlinapp.crud.model.User
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.sql.Connection
import java.sql.DriverManager
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DataBaseIntegrationTests {

    //Лучшеб я ничего не писал , чем это .....

    private val testUser: User = User(1, "Roma", "roma@mail.ru", "123")
    private val testUser2: User = User(2, "Sanya", "sanya@mail.ru", "123")

    companion object {
        val url: String = "jdbc:h2:./src/test/resources/test"
        val userName: String = "sa"
        val password: String = ""
        var conn: Connection? = null;

        //InitDateBase
        @BeforeClass
        @JvmStatic
        fun isDrivePresent() {
            Class.forName("org.h2.Driver")
        }

        @BeforeClass
        @JvmStatic
        fun isConnectionEstablish() {
            conn = DriverManager.getConnection(
                url,
                userName,
                password
            )
            assertTrue { conn != null }
        }
    }

    @Before
    fun init() {
        conn!!.prepareStatement(
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
                    "DROP TABLE IF EXISTS `user`;\n" +
                    "CREATE TABLE `user` (\n" +
                    "  `id` IDENTITY NOT NULL PRIMARY KEY,\n" +
                    "  `name` varchar(50) DEFAULT NULL,\n" +
                    "  `email` varchar(50) DEFAULT NULL,\n" +
                    "  `password` varchar(50) DEFAULT NULL\n" +
                    ");\n" +
                    "INSERT INTO `user` (`id`, `name`, `email`, `password`) VALUES\n" +
                    "(1, 'Roma', 'roma@mail.ru', '123');"
        ).execute()
    }

    @Test
    fun writingToDatabase() {
        val prepareStatement = conn!!
            .prepareStatement("INSERT INTO USER VALUES (?,?,?,?)")
        prepareStatement.setLong(1, testUser2.id!!)
        prepareStatement.setString(2, testUser2.name)
        prepareStatement.setString(3, testUser2.email)
        prepareStatement.setString(4, testUser2.password)
        prepareStatement.executeUpdate()
    }

    @Test
    fun readingFromDatabase() {

        val prepareStatement = conn!!
            .prepareStatement("SELECT * FROM USER WHERE id=?")
        prepareStatement.setLong(1, 1)
        var userFromDb: User? = null
        val resultSet = prepareStatement.executeQuery()
        while (resultSet.next()) {
            userFromDb = User(
                resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("email"), resultSet.getString("password")
            )
        }

        assertEquals(testUser, userFromDb)
    }

    @Test
    fun deleteFromDatabase() {
        val prepareStatement = conn!!
            .prepareStatement("DELETE FROM USER WHERE id = ?")
        prepareStatement.setLong(1, testUser.id!!);
        prepareStatement.executeUpdate();
    }
}