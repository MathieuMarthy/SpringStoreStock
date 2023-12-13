package com.example.mms.repository

import assertk.assertThat
import assertk.assertions.*
import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart
import com.example.mms.services.UserService
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class CartDatabaseTest {

    lateinit var repository: CartRepository

    @MockkBean
    lateinit var userService: UserService

    private val id = "salut@cc.fr"
    private val item = ItemInCart(1, 1)
    private val item2 = ItemInCart(2, 22)

    @Nested
    inner class Create {

        @Test
        fun one() {
            val res = repository.create(id)

            assertThat(res.isSuccess).isTrue()
            assertThat(res.getOrNull()?.id).isEqualTo(id)
        }

        @Test
        fun `already exists`() {
            repository.create(id)
            val res = repository.create(id)

            assertThat(res.isFailure).isTrue()
        }
    }

    @Nested
    inner class Get {

        @Test
        fun get() {
            repository.create(id)
            val res = repository.get(id)

            assertThat(res).isNotNull()
        }

        @Test
        fun `not found`() {
            val res = repository.get(id)

            assertThat(res).isNull()
        }
    }

    @Nested
    inner class Update {

        @Test
        fun update() {
            repository.create(id)

            val cart = Cart(id, mutableListOf(ItemInCart(1, 1)))
            val res = repository.update(cart)

            assertThat(res.isSuccess).isTrue()
            assertThat(res.getOrNull()?.items?.size).isEqualTo(1)
        }

        @Test
        fun `not found`() {
            val cart = Cart(id, mutableListOf(ItemInCart(1, 1)))
            val res = repository.update(cart)

            assertThat(res.isFailure).isTrue()
        }
    }

    @Nested
    inner class Delete {

            @Test
            fun delete() {
                repository.create(id)
                val res = repository.delete(id)

                assertThat(res.isSuccess).isTrue()
                assertThat(repository.get(id)).isNull()
            }

            @Test
            fun `not found`() {
                val res = repository.delete(id)

                assertThat(res.isFailure).isTrue()
            }
    }

    @Nested
    inner class AddItem {

        @Test
        fun add() {
            repository.create(id)

            val res = repository.addItem(id, item.itemId, item.quantity)

            assertThat(res.isSuccess).isTrue()
            assertThat(res.getOrNull()?.items?.size).isEqualTo(1)
            assertThat(repository.get(id)?.items?.size).isEqualTo(1)
        }

        @Test
        fun `not found`() {
            val res = repository.addItem(id, item.itemId, item.quantity)

            assertThat(res.isFailure).isTrue()
        }

        @Test
        fun `add twice`() {
            repository.create(id)
            repository.addItem(id, item.itemId, item.quantity)

            val res = repository.addItem(id, item.itemId, item.quantity)

            assertThat(res.isFailure).isTrue()
            assertThat(repository.get(id)?.items?.size).isEqualTo(1)
        }
    }

    @Nested
    inner class UpdateItem {

        @Test
        fun update() {
            repository.create(id)
            repository.addItem(id, item.itemId, item.quantity)

            val res = repository.updateItem(id, item.itemId, 2)

            assertThat(res.isSuccess).isTrue()
            assertThat(res.getOrNull()).isNotNull()
            assertThat(repository.get(id)?.items?.size).isEqualTo(1)
            assertThat(repository.get(id)?.items?.first()?.quantity).isEqualTo(2)
        }

        @Test
        fun `item not found`() {
            val res = repository.updateItem(id, item.itemId, 2)

            assertThat(res.isFailure).isTrue()
        }

        @Test
        fun `cart not found`() {
            val res = repository.updateItem(id + "e", item.itemId, 2)

            assertThat(res.isFailure).isTrue()
        }
    }

    @Nested
    inner class DeleteItem {

        @Test
        fun delete() {
            repository.create(id)
            repository.addItem(id, item.itemId, item.quantity)
            repository.addItem(id, item2.itemId, item2.quantity)

            val res = repository.deleteItem(id, item.itemId)

            assertThat(res.isSuccess).isTrue()
            assertThat(res.getOrNull()).isNotNull()
            assertThat(repository.get(id)?.items?.size).isEqualTo(1)
        }

        @Test
        fun `item not found`() {
            val res = repository.deleteItem(id, item.itemId)

            assertThat(res.isFailure).isTrue()
        }

        @Test
        fun `cart not found`() {
            val res = repository.deleteItem(id + "e", item.itemId)

            assertThat(res.isFailure).isTrue()
        }
    }

    @Nested
    inner class Valid {

        @Test
        fun valid() {
            repository.create(id)
            repository.addItem(id, item.itemId, item.quantity)
            repository.addItem(id, item2.itemId, item2.quantity)

            val res = repository.valid(id)

            assertThat(res).isTrue()
            assertThat(repository.get(id)?.items?.size).isEqualTo(0)
        }

        @Test
        fun empty() {
            repository.create(id)

            val res = repository.valid(id)

            assertThat(res).isTrue()
            assertThat(repository.get(id)?.items?.size).isEqualTo(0)
        }

        @Test
        fun `not found`() {
            val res = repository.valid(id)

            assertThat(res).isFalse()
        }
    }
}
