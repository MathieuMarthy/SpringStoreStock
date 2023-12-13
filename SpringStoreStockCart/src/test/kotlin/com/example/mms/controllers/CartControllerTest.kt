package com.example.mms.controllers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.mms.repository.CartRepository
import com.example.mms.services.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CartControllerTest {

    @MockkBean
    lateinit var cartRepository: CartRepository

    @MockkBean
    lateinit var userService: UserService

    @Autowired
    lateinit var cartController: CartController

    @Test
    fun create() {
        every { userService.userExists(any()) } returns true
        val id = "salut@cc.fr"

        val res = cartController.create(id)

        assertThat(res.statusCode).isEqualTo(201)
    }

    @Test
    fun get() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun addItem() {
    }

    @Test
    fun updateItem() {
    }

    @Test
    fun deleteItem() {
    }

    @Test
    fun valid() {
    }
}
