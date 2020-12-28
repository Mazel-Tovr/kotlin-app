package mockito

import com.epam.kotlinapp.crud.business.*
import com.epam.kotlinapp.crud.dao.*
import com.epam.kotlinapp.crud.model.*
import io.mockk.*
import io.mockk.impl.annotations.*
import io.mockk.impl.annotations.MockK
import kotlinx.collections.immutable.*
import kotlinx.collections.immutable.adapters.*
import kotlin.test.*

class MockitoPractice {

    @MockK
    private lateinit var dao: ICommonOperations<UserStub>

    @SpyK
    private var daoImpl: ICommonOperations<UserStub> = Dao()

    @BeforeTest
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `test service creation positive`() {
        val stub = UserStub(1, "one")
        every { dao.create(stub) }.returns(stub)
        assertEquals(stub, Service(dao).create(stub))
    }

    @Test(expected = Exception::class)
    fun `test service creation negative`() {
        val stub = UserStub(1, "")
        Service(dao).create(stub)
    }

    @Test
    fun `test service update positive`() {
        val stub = UserStub(1, "one")
        every { daoImpl.update(stub) } returns Unit
        Service(daoImpl).update(stub)
    }

    @Test(expected = Exception::class)
    fun `test service delete negative`() {
        val stub = UserStub(-1, "one")
        every { daoImpl.update(stub) } returns Unit
        Service(daoImpl).update(stub)
    }
}

class Service(val dao: ICommonOperations<UserStub>) : ICommonServices<UserStub> {
    override fun create(entity: UserStub): UserStub {
        if (entity.id < 0 || entity.name.isEmpty()) throw Exception("User could not be crated")

        return dao.create(entity)
    }

    override fun getEntity(id: Long): UserStub {
        return dao.getEntity(id) ?: throw Exception("User not found")
    }

    override fun getAll(): ImmutableList<UserStub> = dao.getAll()

    override fun update(entity: UserStub) {
        if (dao.getEntity(entity.id) == null) throw Exception("User not found")
        dao.update(entity)
    }

    override fun delete(id: Long) {
        dao.delete(id)
    }
}

class Dao : ICommonOperations<UserStub> {

    override fun create(entity: UserStub): UserStub = entity

    override fun getEntity(id: Long): UserStub? {
        if (id != 1L) return null
        return UserStub(1, "Sanya")
    }

    override fun getAll(): ImmutableList<UserStub> =
        ImmutableListAdapter(listOf(UserStub(1, "Maksim"), UserStub(2, "Vika")))

    override fun update(entity: UserStub) {
        println("Super duper logic")
    }

    override fun delete(id: Long) {
        println("Super duper logic")
    }

}

data class UserStub(val id: Long, val name: String) : Entity()
