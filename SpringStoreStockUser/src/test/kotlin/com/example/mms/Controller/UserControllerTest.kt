package com.example.mms.Controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.example.mms.Controller.DTO.UserUpdateDTO
import com.example.mms.Repository.UserRepository
import com.example.mms.Repository.asEntity
import io.mockk.every
import org.apache.catalina.User
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity
import java.util.*

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