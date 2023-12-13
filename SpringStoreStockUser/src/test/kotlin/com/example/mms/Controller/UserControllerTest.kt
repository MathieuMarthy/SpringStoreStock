package com.example.mms.Controller

/**
@SpringBootTest
class UserControllerTest {
private fun defaultUser(
email : String = "email@test.fr",
firstName : String = "firstName",
lastName : String = "lastName",
address : String = "address",
followingNewsletter : Boolean = true,
lastCommand : Date? = null
) : com.example.mms.Model.User {
return com.example.mms.Model.User(email, firstName, lastName, address, followingNewsletter, lastCommand)
}


@MockBean
lateinit var repo : UserRepository

@Autowired
lateinit var controller : UserController

@Nested
inner class Update {
@Test
fun `update valid`(){
every { repo.update(any(), any()) } returns Result.success(defaultUser())
val updateUserDTO = UserUpdateDTO("firstName", "lastName", "address", true)
val result = controller.updateUser("email@test.fr", updateUserDTO)
assertThat(result).isEqualTo(ResponseEntity.ok(defaultUser().asEntity()))
}

}

}
 **/