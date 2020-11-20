package connection.nosql


import com.epam.kotlinapp.crud.dao.nosql.*
import jetbrains.exodus.database.*
import kotlinx.dnq.*
import kotlinx.dnq.store.container.*
import kotlinx.dnq.util.*
import java.io.*

object ConnectionToTestNoSqlDb {

    private const val pathToDatabase = "src/test/resources/db"

    val entityStore: TransientEntityStore


    init {
        XdModel.registerNodes(XdUser, XdProductGroup, XdProduct)
        deletePrevDirectory(File(pathToDatabase))
        val store = StaticStoreContainer.init(File(pathToDatabase), "db")
        initMetaData(XdModel.hierarchy, store)
        initTestDate(store)
        entityStore = store
    }

    fun initTestDate(conn: TransientEntityStore) {
        conn.transactional {
            XdProductGroup.new { groupName = "Super Duper Group" }
            XdProductGroup.new { groupName = "Super Duper Group v2" }

            XdUser.new {
                userName = "Roma"
                email = "roma@mail.ru"
                password = "123"
            }
            XdUser.new {
                userName = "Sanya"
                email = "sanya@mail.ru"
                password = "123"
            }

            XdProduct.new {
                productName = "Телефон"
                price = 228
                productDescription = "Nokia 330"
                groupId = 0
                userId = 0
            }
            XdProduct.new {
                productName = "IТелефон"
                price = 1337
                productDescription = "Samsung"
                groupId = 1
                userId = 1
            }

        }
    }

    fun deletePrevDirectory(directoryToBeDeleted: File): Boolean {
        val allContents = directoryToBeDeleted.listFiles()
        if (allContents != null) {
            for (file in allContents) {
                deletePrevDirectory(file)
            }
        }
        return directoryToBeDeleted.delete()
    }

}
