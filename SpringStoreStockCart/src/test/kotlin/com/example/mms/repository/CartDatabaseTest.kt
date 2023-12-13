package com.example.mms.repository

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.example.mms.services.UserService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class CartDatabaseTest {

    lateinit var repository: CartRepository

    @MockkBean
    lateinit var userService: UserService

    @Nested
    inner class Create {

        @Test
        fun `create one`() {
            every { userService.userExists(any()) } returns true
            val id = "salut@cc.fr"

            val res = repository.create(id)

            assertThat(res.isSuccess).isTrue()
            assertThat(res.getOrNull()?.id).isEqualTo(id)
        }
    }

    @Nested
    inner class Get {

    }

    @Nested
    inner class Update {

    }

    @Nested
    inner class Delete {

    }

    @Nested
    inner class Item {

    }
}
