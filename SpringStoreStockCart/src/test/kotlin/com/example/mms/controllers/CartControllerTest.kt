package com.example.mms.controllers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.mms.controllerAdvice.CartControllerAdvice
import com.example.mms.repository.CartRepository
import com.example.mms.services.ItemService
import com.example.mms.services.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(CartController::class)
class CartControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var cartRepository: CartRepository

    @MockkBean
    lateinit var userService: UserService

    @MockkBean
    lateinit var itemService: ItemService

    @Autowired
    lateinit var cartController: CartController

    private val id = "salut@cc.fr"

    @Nested
    inner class Create {
        private val url = "/api/cart/"

        @Test
        fun `with valid user`() {
            every { userService.userExists(any()) } returns true

            mockMvc.get(url + id)
                    .andExpect { status { isOk() } }
        }

        @Test
        fun `with invalid user`() {
            every { userService.userExists(any()) } returns false

            mockMvc.get(url + id)
                    .andExpect { status { isNotFound() } }
        }

        @Test
        fun `create twice`() {
            every { userService.userExists(any()) } returns true

            mockMvc.get(url + id)
                    .andExpect { status { isOk() } }

            mockMvc.get(url + id)
                    .andExpect { status { isConflict() } }
        }
    }
}
