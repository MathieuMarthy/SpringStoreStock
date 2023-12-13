package com.example.mms.repository

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.example.mms.errors.CartAlreadyExistsException
import com.example.mms.errors.CartNotFoundException
import com.example.mms.errors.ItemAlreadyExistsException
import com.example.mms.errors.ItemNotFoundException
import com.example.mms.models.Cart
import com.example.mms.models.ItemInCart
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

abstract class CartDatabaseTest {

    lateinit var repository: CartRepository

    private val id = "salut@cc.fr"
    private val item = ItemInCart(1, 1)
    private val item2 = ItemInCart(2, 22)

    @Nested
    inner class Create {

        @Test
        fun one() {
            val res = repository.create(id)

            assertThat(res).isNotNull()
            assertThat(res.id).isEqualTo(id)
        }

        @Test
        fun `already exists`() {
            repository.create(id)

            assertThrows<CartAlreadyExistsException> { repository.create(id) }
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
            assertThrows<CartNotFoundException> { repository.get(id) }
        }
    }

    @Nested
    inner class Update {

        @Test
        fun update() {
            repository.create(id)

            val cart = Cart(id, mutableListOf(ItemInCart(1, 1)))
            val res = repository.update(cart)

            assertThat(res).isNotNull()
            assertThat(res.items.size).isEqualTo(1)
        }

        @Test
        fun `not found`() {
            val cart = Cart(id, mutableListOf(ItemInCart(1, 1)))

            assertThrows<CartNotFoundException> { repository.update(cart) }
        }
    }

    @Nested
    inner class Delete {

        @Test
        fun delete() {
            repository.create(id)
            val res = repository.delete(id)

            assertThat(res).isNotNull()
            assertThrows<CartNotFoundException> { repository.get(id) }
        }

        @Test
        fun `not found`() {
            assertThrows<CartNotFoundException> { repository.delete(id) }
        }
    }

    @Nested
    inner class AddItem {

        @Test
        fun add() {
            repository.create(id)

            val res = repository.addItem(id, item.itemId, item.quantity)

            assertThat(res).isNotNull()
            assertThat(res.items.size).isEqualTo(1)
            assertThat(repository.get(id).items.size).isEqualTo(1)
        }

        @Test
        fun `not found`() {
            assertThrows<CartNotFoundException> { repository.addItem(id, item.itemId, item.quantity) }
        }

        @Test
        fun `add twice`() {
            repository.create(id)
            repository.addItem(id, item.itemId, item.quantity)

            assertThrows<ItemAlreadyExistsException> { repository.addItem(id, item.itemId, item.quantity) }
            assertThat(repository.get(id).items.size).isEqualTo(1)
        }
    }

    @Nested
    inner class UpdateItem {

        @Test
        fun update() {
            repository.create(id)
            repository.addItem(id, item.itemId, item.quantity)

            val res = repository.updateItem(id, item.itemId, 2)

            assertThat(res).isNotNull()
            assertThat(repository.get(id).items.size).isEqualTo(1)
            assertThat(repository.get(id).items.first().quantity).isEqualTo(2)
        }

        @Test
        fun `item not found`() {
            repository.create(id)
            assertThrows<ItemNotFoundException> { repository.updateItem(id, item.itemId, 2) }
        }

        @Test
        fun `cart not found`() {
            assertThrows<CartNotFoundException> { repository.updateItem(id + "e", item.itemId, 2) }
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

            assertThat(res).isNotNull()
            assertThat(repository.get(id).items.size).isEqualTo(1)
        }

        @Test
        fun `item not found`() {
            repository.create(id)
            assertThrows<ItemNotFoundException> { repository.deleteItem(id, item.itemId) }
        }

        @Test
        fun `cart not found`() {
            assertThrows<CartNotFoundException> { repository.deleteItem(id + "e", item.itemId) }
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
            assertThat(repository.get(id).items.size).isEqualTo(0)
        }

        @Test
        fun empty() {
            repository.create(id)

            val res = repository.valid(id)

            assertThat(res).isTrue()
            assertThat(repository.get(id).items.size).isEqualTo(0)
        }

        @Test
        fun `not found`() {
            assertThrows<CartNotFoundException> { repository.valid(id) }
        }
    }
}
