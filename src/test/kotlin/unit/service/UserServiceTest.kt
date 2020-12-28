//package unit.service
//
//import com.epam.kotlinapp.crud.business.*
//import com.epam.kotlinapp.crud.dao.*
//import com.epam.kotlinapp.crud.dao.nosql.*
//import com.epam.kotlinapp.crud.model.*
//import org.junit.*
//import org.junit.Test
//import org.junit.runner.*
//import org.mockito.*
//import org.mockito.BDDMockito.*
//import org.mockito.junit.*
//import kotlin.test.*
//
////@RunWith(value = MockitoJUnitRunner::class)
//class UserServiceTest {
//
//
//    //private var dao :ICommonOperations<User> = Mockito.mock(UserOperationImpl::class.java)
////    private var dao :ICommonOperations<User> = Mockito.spy(UserOperationImpl())
//    private lateinit var dao: ICommonOperations<User>
//    private var userService: ICommonServices<User>
//
//    init {
//        // MockitoAnnotations.initMocks(this)
//        userService = UserService(dao)
//    }
//
//    @Before
//    fun setup() {
//        dao = Mockito.doAnswer { }.`when`(mock(ICommonOperations<User>))
//    }
//
//    @Test
//    fun createUser() {
//        val user: User = User(-1L, "Sanya", "@", "com")
//        given(dao.create(user)).willReturn(user)
//        assertEquals(userService.create(user), user)
//
//    }
//
//}
